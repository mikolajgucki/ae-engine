package com.andcreations.ae.studio.plugins.ui.common.window;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;

/**
 * @author Mikolaj Gucki
 */
public class JComponentRequestFocusWindowAdapter extends WindowAdapter {
    /** */
    private JComponent component;
    
    /** */
    public JComponentRequestFocusWindowAdapter(JComponent component) {
        this.component = component;
    }
    
    /** */
    @Override
    public void windowOpened(WindowEvent event) {
        component.requestFocus();
    }
}