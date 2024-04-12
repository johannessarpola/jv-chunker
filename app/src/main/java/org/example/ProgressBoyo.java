package org.example;

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
    private long n = 0;
    @Builder.Default
    private long nCheckpoint = 10_000;
    @Builder.Default
    private long total = 0;
    @Builder.Default
    private String messageFormat = "written %d lines on %s thread";
    void tick() {
        n += 1;
        var tn = Thread.currentThread().getName();
        carriageReturn();
        var pb = progressBar(tn, n, total);
        System.out.print(pb);
    }
    private void carriageReturn() {
        System.out.print("\r");
    }
    private String progressBar(String thread, long n, long total) {
        double progress = (double) n / total * 100;
        StringBuilder sb = new StringBuilder();

        sb.append(thread);
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
