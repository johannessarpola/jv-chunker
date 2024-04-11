package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ChunkyBoyo {
    private ChunkyBoyoConfig config;

    public ChunkyBoyo(ChunkyBoyoConfig config) {
        init(config);
    }

    public ChunkyBoyo(String input, String outputFolder) {
        File f = null, o = null;
        f = new File(input);
        o = new File(outputFolder);
        if (!f.exists()) {
            f = new File(System.getProperty("user.dir") + input);
            System.out.println("Input file found!");
        }
        if (!o.exists()) {
            System.out.println("Absolute path not defined (" + outputFolder + ") using same folder as jar");
            o = new File(System.getProperty("user.dir") + outputFolder);
            if (!o.exists()) {
                System.out.println("Creating folder for output");
                o.mkdir();
            }
        }
        this.inputFolder = f.getAbsolutePath();
        this.filename = f.getName();
        this.outputFolder = o.getAbsolutePath() + "\\";
        this.filepath = f.getAbsolutePath();
        printPaths();
    }

    private void init(ChunkyBoyoConfig config) {
        this.config = config;
    }

    public void createChunksIO() throws FileNotFoundException, IOException {
        // TODO
        // FileReaderBoyo.createChunksByRows(filepath, outputFolder, bufferSize, rowsize);
    }

    public void run() throws IOException {
        this.createChunksIO();
    }

    private void printPaths() {
        System.out.println("Input folder is : " + this.config.getInputFolder().toString());
        System.out.println("Output folder is : " + this.config.getOutputFolder().toString());
    }
}
