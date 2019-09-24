package com.andcreations.ae.studio.plugins.ui.common.settings;

import javax.swing.JComponent;

/**
 * @author Mikolaj Gucki
 */
public interface SettingsPage {
    /** */
    String getTitle();
    
    /** */
    String getIconName();
    
    /** */
    JComponent getComponent();
    
    /** */
    void setSettingsContext(SettingsContext context);
    
    /** */
    void update();
    
    /** */    
    void apply();
}