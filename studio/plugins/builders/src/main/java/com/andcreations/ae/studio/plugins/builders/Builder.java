package com.andcreations.ae.studio.plugins.builders;

import javax.swing.ImageIcon;

/**
 * @author Mikolaj Gucki
 */
public interface Builder {
    /** */
    String getId();
    
    /** */
    String getName();
    
    /** */
    ImageIcon getIcon();
    
    /** */
    String getDesc();
    
    /** */
    boolean hasWarnings();
    
    /** */
    boolean hasErrors();    
    
    /** */
    void build();    
    
    /** */
    void terminate();
}