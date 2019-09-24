package com.andcreations.ae.studio.plugins.lua.parser;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Mikolaj Gucki
 */
public class LuaParserRunnable implements Runnable {
    /** */
    private class Entry {
        /** */
        private File file;
        
        /** */
        private String src;
        
        /** */
        private Entry(File file,String src) {
            this.file = file;
            this.src = src;
        }
        
        /** */
        private Entry(File file) {
            this(file,null);
        }
    }
    
    /** Indicates if to stop the thread. */
    private boolean stopFlag;    
    
    /** The queue of the files to parse. */
    private Queue<Entry> queue = new LinkedList<>();   
    
    /** */
    LuaParserRunnable() {
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
                
                parse(queue.poll());
            }            
        }
    }
    
    /** */
    private void parse(final Entry entry) {
        Runnable runnable = new Runnable() {
            /** */
            @Override
            public void run() {
                LuaParser.get().cache(entry.file,entry.src);
            }
        };
        
        Thread thread = new Thread(runnable,"LuaFileParser");
        thread.start();
    }
    
    /** */
    synchronized void stop() {
        stopFlag = true;
        notify();
    }      
    
    /** */
    synchronized void add(File file) {
        queue.add(new Entry(file));
        notify();
    }        
    /** */
    synchronized void add(File file,String src) {
        queue.add(new Entry(file,src));
        notify();
    }    
}