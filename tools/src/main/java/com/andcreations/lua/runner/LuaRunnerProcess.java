package com.andcreations.lua.runner;

import java.io.OutputStream;

import com.andcreations.lang.OutputStreamProcessRunner;

/**
 * @author Mikolaj Gucki
 */
public class LuaRunnerProcess extends OutputStreamProcessRunner {
    /** */
    LuaRunnerProcess(ProcessBuilder processBuilder,OutputStream stdOutput,
        OutputStream errOutput) {
    //
        super(processBuilder,stdOutput,errOutput);
    }
    
    /**
     * Checks the exit value.
     *
     * @throws LuaErrorException if Lua finished with an error exit value.
     */
    public void checkExitValue() throws LuaErrorException {
        if (getExitValue() != 0) {
            throw new LuaErrorException(Integer.toString(getExitValue()));
        }        
    }    
}