package com.andcreations.ae.studio.plugins.lua.compiler;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.file.cache.TextFileCache;
import com.andcreations.ae.studio.plugins.lua.lib.NativeLua;

/**
 * @author Mikolaj Gucki
 */
class LuaCompilerRunnable implements Runnable {
    /** Indicates if to stop the thread. */
    private boolean stopFlag;
    
    /** The queue of the files to compile. */
    private Queue<File> queue = new LinkedList<>();
    
    /** */
    private LuaCompilerProblems luaCompilerProblems;
    
    /** */
    LuaCompilerRunnable() {
        create();
    }
    
    /** */
    private void create() {
        luaCompilerProblems = new LuaCompilerProblems();
    }
    
    /** */
    private void compile(File file) {
        Log.trace(String.format("Compiling %s",file.getAbsolutePath()));
        clearErrors(file);
        
    // load
        String src;
        try {
            src = TextFileCache.get().read(file);
        } catch (IOException exception) {
            luaCompilerProblems.handleError(file,exception.toString());
            return;
        }
        
        String error = NativeLua.compile(src);
        if (error != null) {
            luaCompilerProblems.handleLuaCompilationError(file,error);
        }
    }
    
    /** */
    @Override
    public void run() {
        while (stopFlag == false) {
            synchronized (this) {
            // wait for the next file if none at the moment
                if (queue.isEmpty() == true) {
                    try {
                        wait();
                    } catch (InterruptedException exception) {
                    }                    
                }
                
                if (stopFlag == true) {
                    break;
                }
                
                compile(queue.poll());
            }
        }
    }
    
    /** */
    synchronized void stop() {
        stopFlag = true;
        notify();
    }        
    
    /** */
    synchronized void add(File file) {
        queue.add(file);
        notify();
    }
    
    /** */
    synchronized void clearErrors(File file) {
        luaCompilerProblems.clearErrors(file);
    } 
    
    /** */
    Set<File> getProblemFiles() {
        return luaCompilerProblems.getFiles();
    }
}