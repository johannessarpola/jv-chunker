/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static java.lang.System.exit;

public class App {
    public static String getGreeting() {
        return "Hello World!";
    }


    public static void main(String[] args) throws IOException {
        System.out.println(getGreeting());
        long statTime = System.currentTimeMillis();
        long endTime = 0;

        var cli = new ChunkyBoyoCli(args);
        var conf = cli.asConfiguration();
        var ec = Executors.newFixedThreadPool(10);
        var sb = new ReaderBoyo(conf, ec);
        var f = ec.submit(sb);

        try {
            f.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        ec.shutdown();
        /**
         * Sketch:
         * - take in input folder
         * - take in output folder
         * - start a thread for each file in input folder to divide it into equal sized chunks
         *  - write said chunks into output folder while preserving the original filename and adding 1..n suffix
         *
         */
        /*
        if (args.length == 3) {
            System.out.println("Let's create the chunks.");
            String input = args[0];
            String outputFolder = args[1];
            int rowsize = Integer.parseInt(args[2]);
            int rows = FileReaderBoyo.countLines(input);
            System.out.println("Total number of rows in input:" + rows);
            ChunkyBoyo cr = new ChunkyBoyo(input, outputFolder);
            cr.setRowsize(rowsize);
            //cr.createChunksNIO();
            cr.run();
            endTime = System.currentTimeMillis();
            System.out.println("Operation took :" + (double) ((endTime - statTime) / 1000) + " seconds");
        }
        if (args.length == 2) {
            String input = args[0];
            String outputFolder = args[1];
            int rows = FileReaderBoyo.countLines(input);
            System.out.println("Total number of rows in input:" + rows);
            ChunkyBoyo cr = new ChunkyBoyo(input, outputFolder);
            //cr.createChunksNIO();
            cr.run();
            endTime = System.currentTimeMillis();
            System.out.println("Operation took :" + (double) ((endTime - statTime) / 1000) + " seconds");
            ;
        }
        if (args.length == 1) {
            if (args[0].equals("-h") || args[0].equals("-help")) {
                System.out.println("First argument is input folder or file (Looks in root if no absolute path is defined)");
                System.out.println("Second argument is output folder or file (Looks in root if no absolute path is defined)");
                System.out.println("Third argument is how many rows chunks in the end should be. 25 000 is default");
            }

        }
        if (args.length == 0) {
            System.out.println("Please add arguments, use -h or -help for help");
        }
        */
        exit(0);

    }


}
