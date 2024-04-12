package fi.johannes;

import java.util.concurrent.*;

public class FutureUtils {
    public static <T> T getFutureResult(Future<T> future) {
        try {
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> CompletableFuture<T> callableToCompletable(Callable<T> callable, ExecutorService ec) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, ec);
    }
}
