package com.andcreations.ae.studio.plugins.simulator;

/**
 * @author Mikolaj Gucki
 */
interface SimulatorComponentListener {
    /** */
    void startSimulator();
    
    /** */
    void stopSimulator();
    
    /** */
    void pauseResumeSimulator();
    
    /** */
    void debugSimulator();
        
    /** */
    void volumeChanged(double volume);
    
    /** */
    void resolutionChanging(int width,int height);
    
    /** */
    void resolutionChanged(int width,int height);
}