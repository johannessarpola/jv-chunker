/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chunker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.System.exit;

/**
 *
 * @author Johannes töissä
 */
public class Chunker {

    private int bufferSize = 100000000; // For NIO 100 mb
    private int rowsize = 25000; // For IO
    private String outputFolder = System.getProperty("user.dir") + "/chunks/";
    private String inputFolder = System.getProperty("user.dir") + "/raw/";
    private String filename = "headlines-docs.csv";
    private String filepath = System.getProperty("user.dir") + "/raw/" + filename;

    public Chunker() {
        init();
    }

    public Chunker(String filename) {
        this.filename = filename;
        init();

    }

    public Chunker(String input, String outputFolder) {
        File f = null, o = null;
        f = new File(input);
        o = new File(outputFolder);
        if (!f.exists()) {
            f = new File(System.getProperty("user.dir") + input);
            // TODO Case where there's only filename defined in input (root)
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

    private void init() {

    }

    public void createChunksIO() throws FileNotFoundException, IOException {
        CustomFileReader.createChunksByRows(filepath, outputFolder, bufferSize, rowsize);
    }

    // TODO Create Ui which has the browse for source and output folder
    public static void main(String[] args) throws IOException {
        System.out.println("");
        long statTime = System.currentTimeMillis();
        long endTime = 0;
        if (args.length == 3) {
            System.out.println("Let's create the chunks.");
            String input = args[0];
            String outputFolder = args[1];
            int rowsize = Integer.parseInt(args[2]);
            int rows = CustomFileReader.countLines(input);
            System.out.println("Total number of rows in input:" + rows);
            Chunker cr = new Chunker(input, outputFolder);
            cr.setRowsize(rowsize);
            //cr.createChunksNIO();
            cr.run();
            endTime = System.currentTimeMillis();
            System.out.println("Operation took :" + (double) ((endTime - statTime) / 1000) + " seconds");
            exit(0);
        }
        if (args.length == 2) {
            String input = args[0];
            String outputFolder = args[1];
            int rows = CustomFileReader.countLines(input);
            System.out.println("Total number of rows in input:" + rows);
            Chunker cr = new Chunker(input, outputFolder);
            //cr.createChunksNIO();
            cr.run();
            endTime = System.currentTimeMillis();
            System.out.println("Operation took :" + (double) ((endTime - statTime) / 1000) + " seconds");;
            exit(0);
        }
        if (args.length == 1) {
            if (args[0].equals("-h") || args[0].equals("-help")) {
                System.out.println("First argument is input folder or file (Looks in root if no absolute path is defined)");
                System.out.println("Second argument is output folder or file (Looks in root if no absolute path is defined)");
                System.out.println("Third argument is how many rows chunks in the end should be. 25 000 is default");
                exit(0);
            }

        }
        if (args.length == 0) {
            System.out.println("Please add arguments, use -h or -help for help");
            exit(1);
        }
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public void setRowsize(int rowsize) {
        this.rowsize = rowsize;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public int getRowsize() {
        return rowsize;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public String getInputFolder() {
        return inputFolder;
    }

    public String getFilename() {
        return filename;
    }

    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    public void setInputFolder(String inputFolder) {
        this.inputFolder = inputFolder;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void run() throws IOException {
        this.createChunksIO();
    }

    private void printPaths() {
        System.out.println("Input folder is : " + this.inputFolder);
        System.out.println("Output folder is : " + this.outputFolder);
        System.out.println("Input file is : " + this.filename);

    }
}
