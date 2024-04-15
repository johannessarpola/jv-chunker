package fi.johannes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProgressBoyo {
    @Builder.Default
    private boolean started = false;

    @Builder.Default
    private String threadName = "not-scheduled-yet";
    private String outputPath;
    @Builder.Default
    private long n = 0;
    @Builder.Default
    private long total = 0;
    void tick(String threadName) {
        this.threadName = threadName;
        started = true;
        n += 1;
    }
}
