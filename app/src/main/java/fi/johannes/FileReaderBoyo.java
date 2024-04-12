/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.johannes;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Johannes töissä
 */
public class FileReaderBoyo {

    public static String fileContentToString(String filepath) throws IOException {
        Path p = Paths.get(filepath);
        byte[] contents = Files.readAllBytes(p);
        return new String(contents, StandardCharsets.UTF_8);
    }

    public static List<byte[]> fileContentUTF8(String s) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(s));
        ArrayList<byte[]> bList = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            bList.add(line.getBytes(StandardCharsets.UTF_8));
        }
        return bList;
    }

    public static int countLines(String filepath) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filepath));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }

    /**
     * Merges files from
     *
     * @param files
     * @param mergedFile
     * @throws IOException
     */
    public static void mergeFiles(File[] files, File mergedFile) throws IOException {

        FileWriter fstream = null;
        BufferedWriter out = null;
        fstream = new FileWriter(mergedFile, true);
        out = new BufferedWriter(fstream);
        for (File f : files) {
            FileInputStream fis;
            fis = new FileInputStream(f);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            String aLine;
            while ((aLine = in.readLine()) != null) {
                
                out.write(aLine);
                out.newLine();
            }
            in.close();

        }
        out.close();
    }

    public static File[] getFilesInFolder(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        return listOfFiles;
    }

    public static List<String> getFilenamesInFolder(String path) {
        File[] files = getFilesInFolder(path);
        List<String> names = new ArrayList<>();
        for (File file : files) {
            if (file.isFile()) {
                names.add(file.getName());
            }
        }
        return names;
    }

    public static void createChunksByRows(String inputFile, String outputFolder, int bufferSize, int rows) throws FileNotFoundException, IOException {
        File f = new File(inputFile);
        String filename = f.getName();
        BufferedReader bw = new BufferedReader(new FileReader(f));
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        int j = 0, i = 0, iTotal=0;
        for (i = 0; i <= rows; i++) {
            String lineStr = bw.readLine();
            // TODO Process each line
            if (lineStr != null) {
                byte[] line = lineStr.getBytes(StandardCharsets.UTF_8);
                if (i == rows) {
                    String outputfile = outputFolder + j + "-" + filename;
                    writeChunk(buffer, outputfile);
                    buffer.clear();
                    j++;
                    iTotal=+i;
                    i = 0;
                }
                buffer.put(line);
                buffer.put(System.getProperty("line.separator").getBytes(StandardCharsets.UTF_8));
            } else {
                break;
            }
        }
        System.out.println("Total numer of written rows is :" + iTotal);
        System.out.println("Total number of created chunks is :" + j);

    }

    public static void writeChunk(ByteBuffer bb, String outputFile) throws IOException {
        bb.flip();
        try (BufferedWriter bw = new BufferedWriter(new PrintWriter(outputFile, "UTF-8"))) {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(bb);
            bw.write(charBuffer.toString());
            charBuffer.clear();
        } catch (Exception e) {
            System.out.println("Chunking failed for file :"+outputFile);
        }
    }
}
