package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.file.Path;
import java.nio.file.Paths;
@Data
@AllArgsConstructor
public class ChunkyBoyoConfig {
    int bufferSize; // For NIO 100 mb
    int rowsize; // For IO
    Path outputFolder;
    Path inputFolder;

    // Some default configs
    public ChunkyBoyoConfig() {
        var user_dir = System.getProperty("user.dir");
        bufferSize = 100000000;
        rowsize = 25000;
        outputFolder = Paths.get(user_dir, "/chunks/");
        inputFolder = Paths.get(user_dir, "/raw/");
    }
}
