package com.andcreations.ae.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;


/**
 * @author Mikolaj Gucki
 */
public class HashUtils {
    /** */
    public static void hash(int input,int[] output) {
        int random = (int)(Math.random() * 0xff);
        
        output[0] = (input & 0x0f) | (random & 0xf0);
        output[1] = (random & 0x0f) | (input & 0xf0);
    }
    
    /** */
    public static int hash(int input) {
    	return ((input & 0x0f) << 4) + ((input & 0xf0) >> 4); 
    }
    
    /** */
    public static void hash(InputStream input,OutputStream output)
        throws IOException {
    //
        while (true) {
        // read
            int in = input.read();
            if (in == -1) {
                break;
            }
            
        // hash
            int out = hash(in);
            
        // write
            output.write(out);
        }
    }
    
    /** */
    public static void hash(File srcFile,File dstFile) throws IOException {
        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            input = FileUtils.openInputStream(srcFile); 
            output = FileUtils.openOutputStream(dstFile);
            hash(input,output);
        } catch (IOException exception) {
            throw exception;
        }
        finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException exception) {
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException exception) {
                }
            }
        }
    }
}
