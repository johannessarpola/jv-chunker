package fi.johannes;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@AllArgsConstructor
@Builder
public class PrinterBoyo implements Runnable {

    @Builder.Default
    private final int updateIntervalMs = 250;
    @Builder.Default
    private final int refreshMs = 5;
    @Builder.Default
    private final List<ProgressBoyo> progressBoyos = new ArrayList<>();
    private final AtomicBoolean cancelSignal;

    public void completedTask(Path file) {
        var fn = file.getFileName().toString();
        System.out.println(String.format("completed %s", fn));
    }
    public void addProgessBoyos(List<ProgressBoyo> progressBoyos) {
        this.progressBoyos.addAll(progressBoyos);
        this.addedProgressWatch(progressBoyos);
    }

    public void addedProgressWatch(List<ProgressBoyo> progressBoyos) {
        StringBuilder sb = new StringBuilder();

        if(!progressBoyos.isEmpty()) {
            sb.append("Addedd progress watches for: ");
        }
        for (var pb : progressBoyos) {
            var op = pb.getOutputPath();
            sb.append("  ");
            sb.append(op);
            sb.append("\n");
        }
        if (!sb.isEmpty()) {
            System.out.print("\r");
            System.out.println(sb);
        }
    }
    public void print() {
        StringBuilder sb = new StringBuilder();
        for (var pb : progressBoyos) {
            if (pb.isStarted()) {
                sb.append(pb.progressBar());
            }
        }
        if (!sb.isEmpty()) {
            System.out.print("\r");
            System.out.println(sb);
        }
    }

    @Override
    public void run() {
        do {
            this.print();
            try {
                Thread.sleep(updateIntervalMs);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while (!cancelSignal.get());
    }

}
