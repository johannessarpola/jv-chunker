package fi.johannes;

import com.google.common.collect.Streams;
import lombok.Builder;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Builder
public class ReaderBoyo {

    private final ChunkyBoyoConfig config;

    public List<SplitterBoyo> splitters() {
        // Specify the directory path
        Path directoryPath = Paths.get(config.inputFolder);
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directoryPath)) {
            // Create direcotry if it does not exist
            var p = Paths.get(this.config.outputFolder);
            if (!Files.isDirectory(p)) {
                Files.createDirectory(p);
            }
            // splits up each file in the directory
            return Streams.stream(directoryStream.iterator())
                    .filter(Files::isRegularFile)
                    .map(this::initializeSplitter)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private SplitterBoyo initializeSplitter(Path filePath) {
        var sb = SplitterBoyo
                .builder()
                .chunkSize(this.config.getChunkSize())
                .threadPoolExecutorSize(this.config.executorSize)
                .separator(this.config.separator)
                .file(filePath)
                .outputPath(this.config.getOutputFolder())
                .build();
        return sb;
    }

}
