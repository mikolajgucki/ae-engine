package com.andcreations.ae.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An output stream encoding using Base16 encoding.
 *
 * @author Mikolaj Gucki
 */
public class Base16OutputStream extends OutputStream {
    /** The wrapped output stream. */
    private OutputStream output;
    
    /**
     * Constructs a {@link Base16OutputStream}.
     *
     * @param output The wrapped output stream.
     */
    public Base16OutputStream(OutputStream output) {
        this.output = output;
    }
    
    /** */
    @Override
    public void write(int value) throws IOException {
        int v0 = value & 0x0f;
        int v1 = (value >> 4) & 0x0f;
        
        int ch0 = Base16.getDigit(v0);
        int ch1 = Base16.getDigit(v1);
        
        output.write(ch0);
        output.write(ch1);
    }
    
    /** */
    @Override
    public void flush() throws IOException {
        output.flush();
    }
}