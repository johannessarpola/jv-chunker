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
    // TODO configure logger or whatever output stream at some point

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
        if(n % nCheckpoint == 0) {
            progressCheckpoint(n);
        }
    }

    private void progressCheckpoint(long n) {
        Printer.println(messageFormat, n, Thread.currentThread().getName());
    }
}
