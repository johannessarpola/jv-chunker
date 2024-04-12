package fi.johannes;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

@AllArgsConstructor
@Builder
public class PrinterBoyo {

    @Builder.Default
    private final int waitMs = 250;
    private final List<ProgressBoyo> progressBoyos;

    public CompletableFuture<Void> run(AtomicBoolean cancelSignal, ExecutorService ec) {
        return CompletableFuture.runAsync(() -> {
            while (cancelSignal.get()) {
                System.out.print("\r");
                StringBuilder sb = new StringBuilder();
                for (var pb : progressBoyos) {
                    if (pb.isStarted()) {
                        sb.append(pb.progressBar());
                    }
                }
                System.out.println(sb);
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, ec);
    }

}
