package com.andcreations.ae.studio.plugins.simulator;

/**
 * @author Mikolaj Gucki
 */
interface SimulatorMenuListener {
    /** */
    void startSimulator();
    
    /** */
    void stopSimulator();
    
    /** */
    void pauseResumeSimulator();    
    
    /** */
    void debugSimulator();    
}