package fi.johannes;

import lombok.Builder;
import lombok.Getter;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.concurrent.Callable;

public class WriterBoyo implements Callable<Path>, AutoCloseable {
    private final Path outputPath;
    private final Deque<byte[]> source;
    @Getter
    private final ProgressBoyo progressBoyo;

    private FileOutputStream fos;

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
            progressBoyo.tick(Thread.currentThread().getName());
            Thread.sleep(500); // TODO rm
        }
        fos.close();
        return outputPath;
    }

    private ProgressBoyo initializeProgress() {
        int checkPoints = Math.max(2, source.size() / 2);
        return ProgressBoyo
                .builder()
                .outputPath(this.outputPath.getFileName().toString())
                .total(source.size())
                .build();
    }

    @Override
    public void close() throws Exception {
        fos.close();
    }
}
