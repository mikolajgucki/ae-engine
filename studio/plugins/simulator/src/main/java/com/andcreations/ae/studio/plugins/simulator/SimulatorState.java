package com.andcreations.ae.studio.plugins.simulator;

import com.andcreations.ae.studio.plugin.PluginState;

/**
 * @author Mikolaj Gucki
 */
public class SimulatorState extends PluginState {
    /** */
    private static final int VOLUME_FACTOR = 1000;
    
    /** */
    private int volume;
    
    /** */
    private int width;
    
    /** */
    private int height;
    
    /** */
    private boolean clearLog;
    
    /** */
    private boolean showLog;
    
    /** */
    public void setVolume(double volume) {
        this.volume = (int)(volume * VOLUME_FACTOR);
    }
    
    /** */
    public double getVolume() {
        return volume / (double)VOLUME_FACTOR;
    }
    
    /** */
    public void setWidth(int width) {
        this.width = width;
    }
    
    /** */
    public int getWidth() {
        return width;
    }
    
    /** */
    public void setHeight(int height) {
        this.height = height;
    }
    
    /** */
    public int getHeight() {
        return height;
    }
    
    /** */
    public void setClearLog(boolean clearLog) {
        this.clearLog = clearLog;
    }
    
    /** */
    public boolean getClearLog() {
        return clearLog;
    }
    
    /** */
    public void setShowLog(boolean showLog) {
        this.showLog = showLog;
    }
    
    /** */
    public boolean getShowLog() {
        return showLog;
    }
    
    /** */
    void initWithDefaults() {
        volume = VOLUME_FACTOR;
        width = 480;
        height = 800;
        clearLog = true;
        showLog = true;
    }
}