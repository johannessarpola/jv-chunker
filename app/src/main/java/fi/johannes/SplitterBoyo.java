package fi.johannes;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;

@Builder
@AllArgsConstructor
public class SplitterBoyo implements Callable<List<WriterBoyo>> {

    @Builder.Default
    private int chunkSize = 10_000;
    private final Path file;
    private final String outputPath;
    private final int threadPoolExecutorSize;
    private FileInputStream fis;

    @Override
    public List<WriterBoyo> call() throws Exception {
        // Specify the directory path
        try (var fis = new FileInputStream(this.file.toFile())) {


            var wbs = new ArrayList<WriterBoyo>();

            // default to total / poolSize
            if (chunkSize <= 0) {
                // Prettu ugly
                this.chunkSize = Files.readAllLines(this.file).size() % threadPoolExecutorSize;
            }

            var chunk = new LinkedBlockingDeque<byte[]>(this.chunkSize);
            // Read whole file content
            // Split into smaller files
            // -> Create WriterBoyo to write the chunk
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int byteRead;
            short workerNumber = 0;

            while ((byteRead = fis.read()) != -1) {
                byteArrayOutputStream.write(byteRead);

                // Send the chunk to writer
                if (chunk.remainingCapacity() == 0) {
                    wbs.add(this.initializeWriter(chunk, this.file, ++workerNumber));
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
            wbs.add(this.initializeWriter(chunk, this.file, ++workerNumber));

            return wbs;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
