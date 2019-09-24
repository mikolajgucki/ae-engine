package com.andcreations.ae.studio.plugins.ant;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mikolaj Gucki
 */
public class AntAppState {
    /** */
    private List<AntAppStateListener> listeners = new ArrayList<>();
    
    /** */
    private String antHome;
    
    /** */
    void addAntAppStateListener(AntAppStateListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    public String getAntHome() {
        return antHome;
    }
    
    /** */
    public void setAntHome(String antHome) {
        String oldAntHome = this.antHome;
        String newAntHome = antHome;
        this.antHome = antHome;
        
        if (StringUtils.equals(oldAntHome,newAntHome) == false) {
        // notify listners
            synchronized (listeners) {
                for (AntAppStateListener listener:listeners) {
                    listener.antHomeChanged(oldAntHome,newAntHome);
                }
            }        
        }
    }
}