package com.andcreations.ae.studio.plugins.wizards;

import javax.swing.ImageIcon;

/**
 * @author Mikolaj Gucki
 */
public interface Wizard {
    /** */
    String getName();
    
    /** */
    ImageIcon getIcon();
    
    /** */
    String getDesc();

    /** */
    void runWizard();    
}