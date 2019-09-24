package com.andcreations.io;

import java.io.OutputStream;

/**
 * @author Mikolaj Gucki
 */
public abstract class OutputStreamLineReader extends OutputStream {
    /** */
    private StringBuilder line = new StringBuilder();
    
    /** */
    private void lineRead() {
        if (line.length() == 0) {
            return;
        }            
        
        lineRead(line.toString());
        line.delete(0,line.length());
    }
    
    /** */
    @Override
    public void write(int value) {
        if (value == 13) {
            return;
        }
        if (value == 10) {
            lineRead();
            return;
        }
        line.append((char)value);
    }
    
    /** */
    @Override
    public void close() {
        lineRead();
    }
    
    /** */
    @Override
    public void flush() {
        lineRead();
    }
    
    /**
     * Gets the current line.
     *
     * @return The current line.
     */
    public String getCurrentLine() {
        return line.toString();
    }
    
    /**
     * Called when a line has been read.
     *
     * @param The line. 
     */
    public abstract void lineRead(String line);
}