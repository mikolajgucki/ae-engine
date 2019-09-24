package com.andcreations.ant;

import java.io.OutputStream;

import com.andcreations.lang.OutputStreamProcessRunner;

/**
 * @author Mikolaj Gucki
 */
public class AntRunnerProcess extends OutputStreamProcessRunner {
    /** */     
    AntRunnerProcess(ProcessBuilder processBuilder,OutputStream stdOutput,
        OutputStream errOutput) {
    //
        super(processBuilder,stdOutput,errOutput);
    }
    
    /**
     * Checks the exit value.
     *
     * @throws AntBuildFailedException if Ant finished with an error exit value.
     */
    public void checkExitValue() throws AntBuildFailedException {
        if (getExitValue() != 0) {
            throw new AntBuildFailedException(getExitValue());
        }        
    }
}