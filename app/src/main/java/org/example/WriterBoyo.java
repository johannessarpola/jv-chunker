package org.example;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

@Data
public class WriterBoyo implements Callable<Path> {
    private Path outputPath;
    private Deque<byte[]> source;
    private ProgressBoyo pb;

    public WriterBoyo(Deque<byte[]> source, String path, String... paths) {
        this.source = source;
        this.outputPath = Paths.get(path, paths);
        int checkPoints = Math.max(2, source.size() / 2);
        pb = ProgressBoyo
                .builder()
                .nCheckpoint(checkPoints)
                .build();
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
            pb.tick();
        }
        fos.close();
        return outputPath;
    }
}
