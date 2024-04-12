package fi.johannes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.nio.file.Path;
import java.nio.file.Paths;
@Data
@Builder
@AllArgsConstructor
public class ChunkyBoyoConfig {
    @Builder.Default
    int executorSize = 10;
    @Builder.Default
    int chunkSize = 100;
    String outputFolder;
    String inputFolder;
    @Builder.Default
    boolean verbose = false;
}
