/*
 * This source file was generated by the Gradle 'init' task
 */
package fi.johannes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.System.exit;

public class App {
    public static void main(String[] args) throws IOException, InterruptedException {
        long statTime = System.currentTimeMillis();

        var cli = new ChunkyBoyoCli(args);
        var conf = cli.asConfiguration();
        var ec = Executors.newFixedThreadPool(conf.executorSize);
        var reader = ReaderBoyo.builder()
                .config(conf)
                .build();
        try {
            var pbs = new ArrayList<ProgressBoyo>();
            var splitters = reader.splitters();
            var splitterJobs = new ArrayList<CompletableFuture<Void>>(splitters.size());
            for (var splitter : splitters) {

                var sf = CompletableFuture.runAsync(() -> {

                    var wbsf = FutureUtils.callableToCompletable(splitter, ec);
                    AtomicBoolean splitterDone = new AtomicBoolean(true);
                    if (conf.verbose) {
                        var prnt = PrinterBoyo.builder()
                                .progressBoyos(pbs)
                                .build();
                        var ignored = prnt.run(splitterDone, ec);
                    }


                    var splitProcess = wbsf.thenApply(writerBoyos -> {
                        // gather progressboyos
                        for (var wb : writerBoyos) {
                            pbs.add(wb.getProgressBoyo());
                        }
                        return writerBoyos.stream().map(f -> FutureUtils.callableToCompletable(f, ec)).toList();
                    });

                    var v = splitProcess.join();
                    for (var f : v) {
                        f.join();
                    }
                    splitterDone.set(false);

                });
                splitterJobs.add(sf);
            }

            splitterJobs.forEach(CompletableFuture::join);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        ec.shutdown();
        var r = ec.awaitTermination(1, TimeUnit.MINUTES);
        Printer.println("Took %d ms", System.currentTimeMillis() - statTime);

        exit(0);

    }


}
