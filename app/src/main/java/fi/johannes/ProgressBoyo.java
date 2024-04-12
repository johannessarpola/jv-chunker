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

    public String progressBar() {
        double progress = (double) n / total * 100;
        StringBuilder sb = new StringBuilder();

        sb.append(this.threadName);
        sb.append(" ");
        sb.append(this.outputPath);
        sb.append(" ");
        sb.append(String.format("%.2f ", progress));
        sb.append("%");
        sb.append(" ");
        sb.append(String.format("of total %d", total));
        for (int i = 0; i < total; i++) {
            if(i <= n) {
                sb.append("=");
            } else {
                sb.append(" ");
            }
        }
        sb.append("|\n");
        return sb.toString();
    }
}
