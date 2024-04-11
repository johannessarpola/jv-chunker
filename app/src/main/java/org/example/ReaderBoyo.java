package org.example;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Builder
@AllArgsConstructor
public class ReaderBoyo implements Callable<List<String>> {

    private final ChunkyBoyoConfig config;
    private ExecutorService threadPoolExecutor;
    @Override
    public List<String> call() throws Exception {
        List<Future<List<String>>> fus = new ArrayList<>();
        // Specify the directory path
        Path directoryPath = Paths.get(config.inputFolder);

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directoryPath)) {
            for (Path filePath : directoryStream) {
                if (Files.isRegularFile(filePath)) {
                    // TODO Remove debug
                    System.out.println("File: " + filePath.getFileName());
                    var sb = SplitterBoyo
                            .builder()
                            .chunkSize(this.config.getChunkSize())
                            .threadPoolExecutor(this.threadPoolExecutor)
                            .file(filePath)
                            .outputPath(this.config.getOutputFolder())
                            .build();

                    var f = this.threadPoolExecutor.submit(sb);
                    fus.add(f);
                }
            }
        } catch (IOException e) {
            // Handle exceptions
            e.printStackTrace();
        }

        for(var f : fus) {
            // TODO
            Printer.println(f.get());
        }

        // TODO
        return new ArrayList<>();
    }
}
