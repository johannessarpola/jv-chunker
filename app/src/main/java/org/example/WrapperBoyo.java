package org.example;

import lombok.Builder;
import lombok.Getter;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Builder
public class WrapperBoyo<T> {

    @Getter
    private final CompletableFuture<T> future;
    @Getter
    private final ProgressBoyo progressBoyo;
}
