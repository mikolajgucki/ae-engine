package com.andcreations.ae.studio.plugins.ui.main.view.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import bibliothek.gui.Dockable;
import bibliothek.gui.dock.action.DockAction;
import bibliothek.gui.dock.action.SelectableDockAction;
import bibliothek.gui.dock.action.actions.SimpleSelectableAction;
import bibliothek.gui.dock.event.SelectableDockActionListener;

import com.andcreations.ae.studio.plugins.ui.main.view.ViewCheckButton;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCheckButtonListener;

/**
 * @author Mikolaj Gucki
 */
class ViewCheckButtonImpl implements ViewCheckButton,ViewAction {
    /** */
    private ImageIcon icon;
    
    /** */
    private String text;    
    
    /** */
    private String tooltip;
    
    /** */
    private KeyStroke stroke;
    
    /** */
    private List<ViewCheckButtonListener> listeners = new ArrayList<>();
    
    /** */
    private SimpleSelectableAction.Check check;
    
    /** */
    ViewCheckButtonImpl() {
        create();
    }
    
    /** */
    private void create() {
        check = new SimpleSelectableAction.Check();
        check.addSelectableListener(new SelectableDockActionListener() {
            /** */
            @Override
            public void selectedChanged(SelectableDockAction action,
                Set<Dockable> dockables) {
            //
                notifyActionPerformed();
            }
        });
    }
    
    /**
     * Sets the button icon.
     */
    @Override
    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }
    
    /** */
    @Override
    public void setText(String text) {
        this.text = text;
    }    
    
    /**
     * Sets the tooltip.
     *
     * @param tooltip The tooltip.
     */
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
    public boolean isChecked() {
        return check.isSelected();
    }
    
    /** */
    @Override
    public void setChecked(boolean checked) {
        check.setSelected(checked);
    }
    
    /** */
    @Override
    public void addViewCheckButtonListener(ViewCheckButtonListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    private void notifyActionPerformed() {
        synchronized (listeners) {
            for (ViewCheckButtonListener listener:listeners) {
                listener.actionPerformed(this);
            }
        }
    }
    
    /** 
     * Gets the dock action associated with this view action.
     *
     * @return The dock action.
     */
    @Override
    public DockAction getDockAction() {
        check.setIcon(icon);
        check.setText(text);
        check.setTooltip(tooltip);
        check.setAccelerator(stroke);
        
        return check;
    }
}