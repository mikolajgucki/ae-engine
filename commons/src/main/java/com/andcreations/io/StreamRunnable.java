package com.andcreations.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A runnable which streams data from an input stream to an output stream.
 *
 * @author Mikolaj Gucki
 */
public class StreamRunnable implements Runnable {
    /** */
    private InputStream input;
    
    /** */
    private OutputStream output;
    
    /** */
    private IOException exception;
    
    /** */
    public StreamRunnable(InputStream input,OutputStream output) {
        this.input = input;
        this.output = output;
    }
    
    /** */
    public IOException getException() {
        return exception;
    }
    
    /** */
    @Override
    public void run() {
        while (true) {                        
            try {
                int value = input.read();
                if (value == -1) {
                    break;
                }
                output.write(value);
            } catch (IOException exception) {
                this.exception = exception;
                break;
            }
        }
    }
}