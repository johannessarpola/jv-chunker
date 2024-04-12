package fi.johannes;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@AllArgsConstructor
@Builder
public class PrinterBoyo implements Runnable {

    @Builder.Default
    private final int updateIntervalMs = 250;
    @Builder.Default
    private final int refreshMs = 5;
    private final List<ProgressBoyo> progressBoyos;
    private final AtomicBoolean cancelSignal;

    @Override
    public void run() {
        do {
            StringBuilder sb = new StringBuilder();
            for (var pb : progressBoyos) {
                if (pb.isStarted()) {
                    sb.append(pb.progressBar());
                }
            }
            if(!sb.isEmpty()) {
                System.out.print("\r");
                System.out.println(sb);
            }
            try {
               if(!progressBoyos.isEmpty()) {
                   Thread.sleep(updateIntervalMs);
               }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while (!cancelSignal.get());
    }

}
