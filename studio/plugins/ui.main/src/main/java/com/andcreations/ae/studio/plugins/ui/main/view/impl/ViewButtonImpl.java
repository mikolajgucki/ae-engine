package com.andcreations.ae.studio.plugins.ui.main.view.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import bibliothek.gui.dock.action.DockAction;
import bibliothek.gui.dock.action.actions.SimpleButtonAction;

import com.andcreations.ae.studio.plugins.ui.main.view.ViewButton;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButtonListener;

/**
 * @author Mikolaj Gucki
 */
class ViewButtonImpl implements ViewButton,ViewAction {
    /** */
    private ImageIcon icon;
    
    /** */
    private ImageIcon disabledIcon;
    
    /** */
    private String text;
    
    /** */
    private String tooltip;
    
    /** */
    private KeyStroke stroke;
    
    /** */
    private List<ViewButtonListener> listeners = new ArrayList<>();
    
    /** */
    private boolean enabled = true;
    
    /** */
    private SimpleButtonAction action;
    
    /** */
    @Override
    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }
    
    /** */
    @Override
    public void setDisabledIcon(ImageIcon disabledIcon) {
        this.disabledIcon = disabledIcon;
    }
    
    /** */
    @Override
    public void setText(String text) {
        this.text = text;
    }
    
    /** */
    @Override
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }
    
    /** */
    @Override
    public void setAccelerator(KeyStroke stroke) {
        this.stroke = stroke;
    }
    
    /** */
    @Override
    public void addViewButtonListener(ViewButtonListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (action != null) {
            action.setEnabled(enabled);
        }
    }
    
    /** */
    private void notifyActionPerformed() {
        synchronized (listeners) {
            for (ViewButtonListener listener:listeners) {
                listener.actionPerformed(this);
            }
        }
    }
    
    /** */
    @Override
    public DockAction getDockAction() {
        action = new SimpleButtonAction();
        action.setIcon(icon);
        action.setDisabledIcon(disabledIcon);
        action.setText(text);
        action.setTooltip(tooltip);
        action.setAccelerator(stroke);
        action.setEnabled(enabled);
        action.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                if (enabled) {
                    notifyActionPerformed();
                }
            }
        });
        
        return action;
    }
}