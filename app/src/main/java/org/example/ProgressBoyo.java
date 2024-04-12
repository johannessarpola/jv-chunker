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
    private long total = 0;

    private String currentThread()  {
        return Thread.currentThread().getName();
    }
    void tick() {
        n += 1;
    }
    private void carriageReturn() {
        System.out.print("\r");
    }
    public String progressBar() {
        double progress = (double) n / total * 100;
        StringBuilder sb = new StringBuilder();

        sb.append(this.currentThread());
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
