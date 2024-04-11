package org.example;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

@Builder
@AllArgsConstructor
public class SplitterBoyo implements Callable<List<String>> {

    private int byteSize = 1024;
    private int chunkSize = 10_000;
    private final Path file;
    private final String outputPath;
    private ExecutorService threadPoolExecutor;

    @Override
    public List<String> call() throws Exception {
        // Specify the directory path
        var fis = new FileInputStream(this.file.toFile());
        var fus = new ArrayList<Future<Path>>();

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
                    var wb = this.createWriter(chunk, this.file, threadNumber);
                    var f = this.threadPoolExecutor.submit(wb);
                    fus.add(f);
                    chunk = new LinkedBlockingDeque<>(this.chunkSize);
                }

                // TODO There's no failsafe if there is no \n in the input
                if (byteRead == '\n') {
                    chunk.add(byteArrayOutputStream.toByteArray());
                    byteArrayOutputStream.reset();
                }
            }

        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        } finally {
            fis.close();
        }

        List<String> rs = new ArrayList<String>();
        // TODO Cleanup
        // Wait for all Future objects to complete
        for (Future<Path> f : fus) {
            try {
                Path result = f.get(); // This blocks until the computation is complete
                System.out.println("Result: " + result.toString());
                rs.add(result.toString());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return rs;
    }


    private <T> WriterBoyo createWriter(Deque<byte[]> chunk, Path fileName, short threadNumber) {
        var fn = fileName.getFileName().toString();
        var base = fn.substring(0, fn.lastIndexOf('.'));
        var ext = fn.substring(fn.lastIndexOf('.') + 1);
        var nfn = String.format("%s_%d.%s", base, threadNumber, ext);
        var wb = new WriterBoyo(chunk, this.outputPath, nfn);
        return wb;
    }
}
