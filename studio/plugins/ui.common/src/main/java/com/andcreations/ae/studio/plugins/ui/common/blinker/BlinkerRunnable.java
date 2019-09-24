package com.andcreations.ae.studio.plugins.ui.common.blinker;

import com.andcreations.ae.studio.log.Log;

/**
 * @author Mikolaj Gucki
 */
public class BlinkerRunnable implements Runnable {
    /** */
    private static final int ON_DURATION = 500;
    
    /** */
    private static final int OFF_DURATION = 500;
    
    /** */
    private boolean stopFlag;
    
    /** */
    @Override
    public void run() {
        while (stopFlag == false) {
            try {
            // on
                Blinker.get().on();
                Thread.sleep(ON_DURATION);
                if (stopFlag == true) {
                    break;
                }
                
            // off
                Blinker.get().off();
                Thread.sleep(OFF_DURATION);
            } catch (InterruptedException exception) {
                Log.error("Interrupted",exception);
            }
        }
    }
    
    /** */
    void postStop() {
        stopFlag = true;
    }
}