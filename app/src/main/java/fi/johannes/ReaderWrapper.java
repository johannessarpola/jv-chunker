package fi.johannes;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class ReaderWrapper {
    @Getter
    private List<CompletableFuture<List<WriterWrapper<Path>>>> result;
}

