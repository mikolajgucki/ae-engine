package com.andcreations.util;

/**
 * @author Mikolaj Gucki
 */
public abstract class Timer implements Runnable {
    /** */
    private int period;
    
    /** */
    private boolean stopFlag;
    
    /** */
    protected Timer(int period) {
        this.period = period;
    }
    
    /** */
    protected boolean isStopped() {
        return stopFlag;
    }
    
    /** */
    protected abstract void tick();
    
    /** */
    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                try {
                    wait(period);
                } catch (InterruptedException exception) {                    
                }                
                if (stopFlag == true) {
                    notify();
                    break;
                }
            }
            tick();
        }
    }
    
    /** */
    public void stopAsync() {
        synchronized (this) {
            stopFlag = true;
            notify();
        }
    }
    
    /** */
    public void stop() {
        synchronized (this) {
            stopFlag = true;
            try {
                wait();
            } catch (InterruptedException exception) {
            }
        }
    }
}