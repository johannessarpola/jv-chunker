package org.example;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
public class ReaderBoyo implements Callable<List<String>> {

    private final ChunkyBoyoConfig config;
    @Getter
    private ExecutorService threadPoolExecutor;
    @Getter
    @Builder.Default
    private ArrayList<WrapperBoyo<List<String>>> wrapperBoyos = new ArrayList<>();

    @Override
    public List<String> call() throws Exception {
        List<List<WrapperBoyo<?>>> fus = new ArrayList<>();
        // Specify the directory path
        Path directoryPath = Paths.get(config.inputFolder);

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directoryPath)) {
            for (Path filePath : directoryStream) {
                if (Files.isRegularFile(filePath)) {
                    var sb = SplitterBoyo
                            .builder()
                            .chunkSize(this.config.getChunkSize())
                            .threadPoolExecutor(this.getThreadPoolExecutor())
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

        return fus.stream()
                .map(FutureUtils::getFutureResult)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }



}
