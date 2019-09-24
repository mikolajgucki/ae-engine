package com.andcreations.lua.runner;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;


/**
 * Runs a Lua script by running the Lua executable.
 *
 * @author Mikolaj Gucki
 */
public class LuaRunner {
    /** */
    private File luaExec;
    
    /** */
    public LuaRunner(File luaExec) {
        this.luaExec = luaExec;
    }
    
    /** */
    private ProcessBuilder buildProcess(File luaFile) {
        List<String> args = new ArrayList<>();
        args.add(luaExec.getAbsolutePath());
        args.add(luaFile.getAbsolutePath());
        
        ProcessBuilder builder = new ProcessBuilder(args);
        return builder;
    }   
    
    /** */
    public LuaRunnerProcess getProcess(File luaFile,OutputStream output,
        OutputStream errorOutput) {
    //
        return new LuaRunnerProcess(buildProcess(luaFile),output,errorOutput);
    }
    
    /** */
    public void run(File luaFile,OutputStream output,
        OutputStream errorOutput) throws LuaErrorException,IOException {
    //
        LuaRunnerProcess luaRunnerProcess = getProcess(
            luaFile,output,errorOutput);
        try {
            luaRunnerProcess.start();
            luaRunnerProcess.waitFor();
        } finally {
            luaRunnerProcess.cleanUp();
        }
    }
    
    /** */
    public String run(File luaFile) throws LuaErrorException,IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayOutputStream errorOutput = new ByteArrayOutputStream();
        
        try {
            run(luaFile,output,errorOutput);
        } catch (LuaErrorException exception) {
            throw new LuaErrorException(
                errorOutput.toString("UTF-8"),exception);
        }
        
        return output.toString("UTF-8");
    }
}