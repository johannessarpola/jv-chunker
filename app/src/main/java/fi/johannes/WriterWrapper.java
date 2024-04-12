package fi.johannes;

import lombok.Builder;
import lombok.Getter;

import java.util.concurrent.CompletableFuture;

@Builder
public class WriterWrapper<T> {

    @Getter
    private final CompletableFuture<T> future;
    @Getter
    private final ProgressBoyo progressBoyo;
}
