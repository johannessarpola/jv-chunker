package org.example;

import lombok.Getter;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.concurrent.Callable;

public class WriterBoyo implements Callable<Path> {
    private final Path outputPath;
    private final Deque<byte[]> source;
    @Getter
    private final ProgressBoyo progressBoyo;

    public WriterBoyo(Deque<byte[]> source, String path, String... paths) {
        this.source = source;
        this.outputPath = Paths.get(path, paths);
        progressBoyo = initializeProgress();
    }

    @Override
    public Path call() throws Exception {

        // Create direcotry if it does not exist
        if(!Files.isDirectory(this.outputPath.getParent())) {
            Files.createDirectory(this.outputPath.getParent());
        }

        var fos = new FileOutputStream(this.outputPath.toFile(), false);
        byte[] b;
        while((b = source.poll()) != null) {
            fos.write(b);
            progressBoyo.tick();
        }
        fos.close();
        return outputPath;
    }

    private ProgressBoyo initializeProgress() {
        int checkPoints = Math.max(2, source.size() / 2);
        return ProgressBoyo
                .builder()
                .total(source.size())
                .build();
    }
}
