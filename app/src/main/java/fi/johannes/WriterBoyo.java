package fi.johannes;

import lombok.Builder;
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
        if (!Files.isDirectory(this.outputPath.getParent())) {
            Files.createDirectory(this.outputPath.getParent());
        }

        try (var fos = new FileOutputStream(this.outputPath.toFile(), false)) {
            byte[] b;
            while ((b = source.poll()) != null) {
                fos.write(b);
                progressBoyo.tick(Thread.currentThread().getName());
            }
            return outputPath;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ProgressBoyo initializeProgress() {
        return ProgressBoyo
                .builder()
                .outputPath(this.outputPath.getFileName().toString())
                .total(source.size())
                .build();
    }

}
