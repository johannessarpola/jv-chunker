package org.example;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

@Data
@Builder
public class WriterBoyo implements Callable<Path> {
    private Path outputPath;
    private Iterator<byte[]> source;
    private ProgressBoyo pb = new ProgressBoyo();

    public WriterBoyo(Iterator<byte[]> source, String path, String... paths) {
        this.source = source;
        this.outputPath = Paths.get(path, paths);
    }

    @Override
    public Path call() throws Exception {

        // Create direcotry if it does not exist
        if(!Files.isDirectory(this.outputPath.getParent())) {
            Files.createDirectory(this.outputPath.getParent());
        }

        var fos = new FileOutputStream(this.outputPath.toFile());
        while(this.source.hasNext()) {
            var next = source.next();
            fos.write(next);
            pb.tick();
        }
        fos.close();
        return outputPath;
    }
}
