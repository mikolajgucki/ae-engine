package com.andcreations.ae.studio.plugins.ui.main.view;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * @author Mikolaj Gucki
 */
public class ViewBorder {
    /**
     * Sets the border of a top panel inside a view.
     *
     * @param panel The panel.
     */
    public static void set(JPanel panel) {
        panel.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
    }
}