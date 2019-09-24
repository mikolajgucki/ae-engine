package com.andcreations.process;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for running child processes.
 * 
 * @author Mikolaj Gucki
 */
public class ProcessRunner {
    /** */
    private static ProcessBuilder buildProcess(File baseDir,String[] args)
        throws IOException {
	// process arguments
    	List<String> processArgs = new ArrayList<>();
	
	// arguments
    	for (String arg:args) {
    		processArgs.add(arg);
    	}
    	
    // build the process
        ProcessBuilder builder = new ProcessBuilder(processArgs);
        builder.directory(baseDir);
        
        return builder;
    }
	
    /**
     * Creates a child process.
     *
     * @param stdOutput The output connected to the process standard stream.
     * @param errOutput The output connected to the process error stream.
     * @param baseDir The directory containing the build file.
     * @param args The arguments.
     * @return The process.
     */
    public static ProcessRunnerProcess createProcess(OutputStream stdOutput,
		OutputStream errOutput,File baseDir,String[] args) throws IOException {
	//
        ProcessBuilder processBuilder = buildProcess(baseDir,args);
        return new ProcessRunnerProcess(processBuilder,stdOutput,errOutput);    	
    }
    
    /**
     * Synchronously runs a child process.
     *
     * @param stdOutput The output connected to the process standard stream.
     * @param errOutput The output connected to the process error stream.
     * @param baseDir The directory in which to run ADB.
     * @param args The arguments.
     * @return The exit value.
     * @throws IOException on I/O error.
     */
    public synchronized int run(OutputStream stdOutput,OutputStream errOutput,
        File baseDir,String... args) throws IOException {
    // create the process
    	ProcessRunnerProcess process = createProcess(
			stdOutput,errOutput,baseDir,args);
        
    // run the process
        try {
            process.start();
            process.waitFor();
        } finally {
            process.cleanUp();
        }
        
        return process.getExitValue();
    }
}
