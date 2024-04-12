package org.example;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;

@Builder
@AllArgsConstructor
public class SplitterBoyo implements Callable<ArrayList<WrapperBoyo<Path>>> {

    @Builder.Default
    private int chunkSize = 10_000;
    private final Path file;
    private final String outputPath;
    private final ExecutorService threadPoolExecutor;

    @Getter
    @Builder.Default
    private ArrayList<ProgressBoyo> progressBoyos = new ArrayList<>();

    @Override
    public ArrayList<WrapperBoyo<Path>> call() throws Exception {
        // Specify the directory path
        var fis = new FileInputStream(this.file.toFile());
        var fus = new ArrayList<WrapperBoyo<Path>>();

        // default to total / poolSize
        if (chunkSize <= 0) {
            // Prettu ugly
            this.chunkSize = Files.readAllLines(this.file).size() % ((ThreadPoolExecutor) this.threadPoolExecutor).getPoolSize();
        }

        var chunk = new LinkedBlockingDeque<byte[]>(this.chunkSize);
        try {
            // Read whole file content
            // Split into smaller files
            // -> Create WriterBoyo to write the chunk
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int byteRead;
            short threadNumber = 0;

            while ((byteRead = fis.read()) != -1) {
                byteArrayOutputStream.write(byteRead);

                // Send the chunk to writer
                if (chunk.remainingCapacity() == 0) {
                    threadNumber++;
                    this.queueWriter(chunk, threadNumber, fus);
                    chunk = new LinkedBlockingDeque<>(this.chunkSize);
                }

                // TODO There's no failsafe if there is no \n in the input
                if (byteRead == '\n') {
                    chunk.add(byteArrayOutputStream.toByteArray());
                    byteArrayOutputStream.reset();
                }
            }

            // write the tail
            var arr = byteArrayOutputStream.toByteArray();
            chunk.add(arr);
            this.queueWriter(chunk, threadNumber, fus);

        } catch (Exception e) {
            // Handle exceptions
            throw new RuntimeException(e);
        } finally {
            fis.close();
        }


        return fus;
    }

    private void queueWriter(LinkedBlockingDeque<byte[]> chunk,
                             short threadNumber,
                             Collection<WrapperBoyo<Path>> fus) {
        var wb = this.initializeWriter(chunk, this.file, ++threadNumber);
        fus.add(this.startWriter(wb));
    }

    private WrapperBoyo<Path> startWriter(WriterBoyo wb) {

        var cf = FutureUtils.futureToCompletable(wb, this.threadPoolExecutor);
        this.progressBoyos.add(wb.getProgressBoyo());
        return WrapperBoyo.<Path>builder()
                .progressBoyo(wb.getProgressBoyo())
                .future(cf)
                .build();
    }

    private <T> WriterBoyo initializeWriter(Deque<byte[]> chunk, Path fileName, short threadNumber) {
        var fn = fileName.getFileName().toString();
        var base = fn.substring(0, fn.lastIndexOf('.'));
        var ext = fn.substring(fn.lastIndexOf('.') + 1);
        var nfn = String.format("%s_%d.%s", base, threadNumber, ext);
        var wb = new WriterBoyo(chunk, this.outputPath, nfn);
        return wb;
    }
}
