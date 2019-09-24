package com.andcreations.lang;

import java.io.IOException;
import java.io.OutputStream;

import com.andcreations.io.StreamRunnable;

/**
 * @author Mikolaj Gucki
 */
public class OutputStreamProcessRunner {
    /** The process builder. */
    private ProcessBuilder processBuilder;
    
    /** The output connected to the process standard stream. */
    private OutputStream stdOutput;
    
    /** The output connected to the process error stream. */
    private OutputStream errOutput;
    
    /** The process. */
    private Process process;
    
    /** The process exit value. */
    private int exitValue;
    
    /** */
    private Thread stdOutputThread;
    
    /** */
    private StreamRunnable stdOutputRunnable;
    
    /** */
    private Thread errOutputThread;
    
    /** */
    private StreamRunnable errOutputRunnable;
    
    /** */     
    protected OutputStreamProcessRunner(ProcessBuilder processBuilder,
        OutputStream stdOutput,OutputStream errOutput) {
    //
        this.processBuilder = processBuilder;
        this.stdOutput = stdOutput;
        this.errOutput = errOutput;
    }
    
    /**
     * Starts the Ant process.
     *
     * @throws IOException if it fails to start the Ant process.
     */
    public synchronized void start() throws IOException {
        if (process != null) {
            throw new IllegalStateException("Process already run");
        }
        process = processBuilder.start();
            
    // stream the standard output
        if (stdOutput != null) {
            stdOutputRunnable = new StreamRunnable(
                process.getInputStream(),stdOutput);
            stdOutputThread = new Thread(
                stdOutputRunnable,"AntRunnerStdStream");
            stdOutputThread.start();
        }
        
    // stream the error output
        if (errOutput != null) {
            errOutputRunnable = new StreamRunnable(
                process.getErrorStream(),errOutput);
            errOutputThread = new Thread(
                errOutputRunnable,"AntRunnerErrStream");
            errOutputThread.start();
        }
    }
    
    /**
     * Waits for the process to terminate.
     */
    public void waitFor() {
    // wait for the process to exit
        try {
            exitValue = process.waitFor();
        } catch (InterruptedException exception) {
        }        
    }
    
    /**
     * Terminates the process.
     */
    public void terminate() {
        process.destroy();
    }
    
    /**
     * Cleans up the resources. Must be called after the process is terminated.
     *
     * @throws IOException if streaming failed.
     */
    public void cleanUp() throws IOException {
    // wait for the output stream thread
        if (stdOutputThread != null) {
            try {
                stdOutputThread.join();
            } catch (InterruptedException exception) {
            }
            
            stdOutput.flush();
        // check if streaming failed
            if (stdOutputRunnable.getException() != null) {
                throw stdOutputRunnable.getException();
            }
        }
        
    // wait for the error output stream thread
        if (errOutputThread != null) {
            try {
                errOutputThread.join();
            } catch (InterruptedException exception) {
            }
            
            errOutput.flush();
        // check if streaming failed
            if (errOutputRunnable.getException() != null) {
                throw errOutputRunnable.getException();
            }
        }        
    }
    
    /**
     * Gets the exit value.
     *
     * @return The exit value.
     */
    public int getExitValue() {
        return exitValue;
    }
}