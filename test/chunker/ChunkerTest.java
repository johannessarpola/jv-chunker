/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chunker;

import junit.framework.TestCase;

/**
 *
 * @author Johannes Sarpola <johannes.sarpola@gmail.com>
 */
public class ChunkerTest extends TestCase {
    
    public ChunkerTest(String testName) {
        super(testName);
    }
    /**
     * Test of main method, of class Chunker.
     */
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = {"/res/0-headlines-docs.csv", "/output/"};
        Chunker cr = new Chunker(args[0], args[1]);
        cr.setRowsize(1000);
        cr.run();
    }

}
