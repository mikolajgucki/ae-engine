package com.andcreations.ae.studio.plugins.ui.common.blinker;

import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.studio.log.Log;

/**
 * @author Mikolaj Gucki
 */
public class Blinker {
    /** */
    private static Blinker instance;
    
    /** */
    private BlinkerRunnable blinkerRunnable;
    
    /** */
    private Thread blinkerThread;
    
    /** */
    private List<BlinkerTask> tasks = new ArrayList<>();
    
    /** */
    public void start() {
        if (blinkerRunnable != null) {
            throw new IllegalStateException("Blinker already started");
        }
        
    // runnable
        blinkerRunnable = new BlinkerRunnable();
        
    // thread
        blinkerThread = new Thread(blinkerRunnable,"Blinker");
        blinkerThread.start();
    }
    
    /** */
    public void stop() {
        if (blinkerRunnable == null) {
            return;
        }
        
        blinkerRunnable.postStop();
        try {
            blinkerThread.join();
        } catch (InterruptedException exception) {
            Log.error("Interrupted",exception);
        }
    }
    
    /** */
    synchronized void on() {
        for (BlinkerTask task:tasks) {
            task.on();
        }
    }
    
    /** */
    synchronized void off() {
        for (BlinkerTask task:tasks) {
            task.off();
        }
    }

    /** */
    public synchronized void addBlinkerTask(BlinkerTask task) {
        tasks.add(task);
    }

    /** */
    public synchronized void removeBlinkerTask(BlinkerTask task) {
        tasks.remove(task);
    }
    
    /** */
    public static Blinker get() {
        if (instance == null) {
            instance = new Blinker();
        }
        
        return instance;
    }        
}