package fi.johannes;

import com.google.common.collect.Streams;
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
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Builder
@AllArgsConstructor
public class ReaderBoyo implements Callable<ReaderWrapper> {

    private final ChunkyBoyoConfig config;
    @Getter
    private ExecutorService threadPoolExecutor;
    @Getter
    @Builder.Default
    private ArrayList<WriterWrapper<List<String>>> wrapperBoyos = new ArrayList<>();

    @Override
    public ReaderWrapper call() throws Exception {
        List<CompletableFuture<List<WriterWrapper<Path>>>> fus = new ArrayList<>();
        // Specify the directory path
        Path directoryPath = Paths.get(config.inputFolder);
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directoryPath)) {
            var cfs = Streams.stream(directoryStream.iterator())
                    .filter(Files::isRegularFile)
                    .map(this::initializeSplitter)
                    .map( sb -> FutureUtils.callableToCompletable(sb, this.getThreadPoolExecutor()))
                    .toList();

            return new ReaderWrapper(cfs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private SplitterBoyo initializeSplitter(Path filePath) {
        var sb = SplitterBoyo
                .builder()
                .chunkSize(this.config.getChunkSize())
                .threadPoolExecutor(this.getThreadPoolExecutor())
                .file(filePath)
                .outputPath(this.config.getOutputFolder())
                .build();
        return sb;
    }
}
