package com.andcreations.msgpack;

import java.io.IOException;
import java.io.InputStream;

import com.andcreations.resources.BundleResources;
import com.andcreations.resources.StrResources;

/**
 * Reads message pack values.
 *
 * @author Mikolaj Gucki 
 */
public class MessagePackUnpacker {
    /** The string resources. */
    private StrResources res = new BundleResources(MessagePackUnpacker.class);   
    
    /**
     * Reads a byte.
     *
     * @param input The stream from which to read.
     * @return The read byte.
     * @throws IOException on I/O error.
     */
    private int read(InputStream input) throws IOException {
        int value = input.read();
        if (value == -1) {
            throw new IOException(res.getStr("end.of.file"));
        }
        
        return value;
    }
    
    /**
     * Reads a length.
     *
     * @param input The stream from which to read.
     * @return The read length.
     * @throws IOException on I/O error.
     */
    public int readLength(InputStream input) throws IOException {
        int v0 = read(input);
        int v1 = read(input); 
        
        return (v0 << 8) + v1;
    }
    
    /**
     * Reads the type of the next value.
     *
     * @param input The stream from which to read.
     * @return The read type.
     * @throws IOException on I/O error.
     */
    public int readType(InputStream input) throws IOException {
        return read(input);
    }
    
    /**
     * Reads a 16-bit signed integer.
     *
     * @param input The stream from which to read.
     * @return The read value.
     * @throws IOException on I/O error.
     */
    public int readInt16(InputStream input) throws IOException {
        int v0 = read(input);
        int v1 = read(input); 
        
        return (v0 << 8) + v1;
    }
    
    /**
     * Reads a 32-bit signed integer.
     *
     * @param input The stream from which to read.
     * @return The read value.
     * @throws IOException on I/O error.
     */
    public int readInt32(InputStream input) throws IOException {
        int v0 = read(input);
        int v1 = read(input); 
        int v2 = read(input);
        int v3 = read(input); 
        
        return (v0 << 24) + (v1 << 16) + (v2 << 8) + v3;
    }
    
    /**
     * Reads a 16-bit string.
     *
     * @param input The stream from which to read.
     * @return The read value.
     * @throws IOException on I/O error.
     */
    public String readStr16(InputStream input) throws IOException {
        int length = readLength(input);
        
        StringBuffer buffer = new StringBuffer();
        for (int index = 0; index < length; index++) {
            buffer.append((char)read(input));
        }
        
        return buffer.toString();
    }
    
    /**
     * Reads the length of a 16-bit map.
     *
     * @param input The stream from which to read.
     * @return The read length.
     * @throws IOException on I/O error.
     */
    public int readMap16(InputStream input) throws IOException {
        return readLength(input);
    }
}
