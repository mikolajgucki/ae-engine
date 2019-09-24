package com.andcreations.ae.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Input stream decoding using Base16 encoding.
 *
 * @author Mikolaj Gucki
 */
public class Base16InputStream extends InputStream {
    /** The wrapped input stream. */
    private InputStream input;
    
    /**
     * Constructs a {@link Base16InputStream}.
     *
     * @param input The wrapped input stream.
     */
    public Base16InputStream(InputStream input) {
        this.input = input;
    }
    
    /** */
    @Override
    public int read() throws IOException {
        int v0 = input.read();
        if (v0 == -1) {
            return -1;
        }
        
        int v1 = input.read();
        if (v1 == -1) {
            return -1;
        }
        
        int value = (Base16.getDigitValue(v0) << 4) + Base16.getDigitValue(v1);
        return value;
    }
}