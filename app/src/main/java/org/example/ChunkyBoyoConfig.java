package org.example;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.nio.file.Path;
import java.nio.file.Paths;
@Data
@Builder
@AllArgsConstructor
public class ChunkyBoyoConfig {
    int chunkSize;
    String outputFolder;
    String inputFolder;

    // Some default configs
//    public ChunkyBoyoConfig() {
//        var user_dir = System.getProperty("user.dir");
//        bufferSize = 100000000;
//        rowsize = 25000;
//        outputFolder = Paths.get(user_dir, "/chunks/");
//        inputFolder = Paths.get(user_dir, "/raw/");
//    }
}
