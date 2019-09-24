package com.andcreations.ae.studio.plugins.ui.main.view;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

/**
 * Represents a simple button.
 *
 * @author Mikolaj Gucki
 */
public interface ViewButton {
    /**
     * Sets the icon.
     *
     * @param icon The icon.
     */
    void setIcon(ImageIcon icon);
    
    /**
     * Sets the disabled icon.
     *
     * @param disabledIcon The icon.
     */
    void setDisabledIcon(ImageIcon disabledIcon);
    
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
     * Adds a button listener.
     *
     * @param listener The listener.
     */
    void addViewButtonListener(ViewButtonListener listener);
    
    /**
     * Sets the button enabled/disabled.
     *
     * @param enabled The enabled state.
     */
    void setEnabled(boolean enabled);
}