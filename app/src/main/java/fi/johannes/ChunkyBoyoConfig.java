package fi.johannes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChunkyBoyoConfig {
    @Builder.Default
    int executorSize = 10;
    @Builder.Default
    int chunkSize = 100;
    @Builder.Default
    String outputFolder = ".out";
    @Builder.Default
    String inputFolder = ".in";
    @Builder.Default
    String separator = System.lineSeparator();
    @Builder.Default
    boolean verbose = false;
}
