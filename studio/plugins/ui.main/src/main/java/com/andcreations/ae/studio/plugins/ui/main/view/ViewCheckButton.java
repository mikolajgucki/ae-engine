package com.andcreations.ae.studio.plugins.ui.main.view;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

/**
 * Represents a check button.
 *
 * @author Mikolaj Gucki
 */
public interface ViewCheckButton {
    /**
     * Sets the button icon.
     */
    void setIcon(ImageIcon icon);
    
    /**
     * Sets the text.
     *
     * @param text The text.
     */
    void setText(String text);    
    
    /**
     * Sets the tooltip.
     *
     * @param tooltip The tooltip.
     */
    void setTooltip(String tooltip); 
    
    /**
     * Sets the accelerator.
     *
     * @param stroke The stroke.
     */
    void setAccelerator(KeyStroke stroke);    
    
    /**
     * Tests if the button is checked.
     *
     * @return <code>true</code> if checked, <code>false</code> otherwise.
     */
    boolean isChecked();
    
    /**
     * Sets the button (un)checked.
     *
     * @param checked <code>true</code> if checked,
     *   <code>false</code> otherwise.
     */
    void setChecked(boolean checked);
    
    /**
     * Adds a button listener.
     *
     * @param listener The listener.
     */
    void addViewCheckButtonListener(ViewCheckButtonListener listener);
}