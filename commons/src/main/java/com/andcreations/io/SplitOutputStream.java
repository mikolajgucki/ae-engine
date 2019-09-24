package com.andcreations.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Mikolaj Gucki
 */
public class SplitOutputStream extends OutputStream {
    /** */
    private OutputStream[] streams;
    
    /** */
    public SplitOutputStream(OutputStream... streams) {
        this.streams = streams;
    }
    
    /** */
    @Override
    public void write(int value) throws IOException {
        for (OutputStream stream:streams) {
            if (stream != null) {
                stream.write(value);
            }
        }
    }
    
    /** */
    @Override
    public void close() throws IOException {
        for (OutputStream stream:streams) {
            if (stream != null) {
                stream.close();
            }
        }
    }
    
    /** */
    @Override
    public void flush() throws IOException {
        for (OutputStream stream:streams) {
            if (stream != null) {
                stream.flush();
            }
        }
    }    
}