package com.andcreations.ae.studio.plugins.ui.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class IconButton extends JLabel {
    /** */
    private ImageIcon icon;
    
    /** */
    private ImageIcon highlightIcon;
    
    /** */
    private List<ActionListener> actionListeners = new ArrayList<>();
    
    /** */
    public IconButton(ImageIcon icon,ImageIcon highlightIcon) {
        this.icon = icon;
        this.highlightIcon = highlightIcon;
        create();
    }
    
    /** */
    public IconButton(ImageIcon icon) {
        this(icon,null);
    }
    
    /** */
    private void create() {
        setIcon(icon);
        
    // mouse listener
        addMouseListener(new MouseAdapter() {
            /** */
            @Override
            public void mouseClicked(MouseEvent event) {
                if (isEnabled() == true) {
                    notifyClicked();
                }
            }
            
            /** */
            @Override
            public void mouseEntered(MouseEvent event) {
                if (highlightIcon != null && isEnabled() == true) {
                    setIcon(highlightIcon);
                }
            }
            
            /** */
            @Override
            public void mouseExited(MouseEvent event) {
                if (highlightIcon != null) {
                    setIcon(icon);
                }
            }
        });        
    }
    
    /** */
    public void addActionListener(ActionListener actionListener) {
        actionListeners.add(actionListener);
    }
    
    /** */
    private void notifyClicked() {
        ActionEvent event = new ActionEvent(
            this,ActionEvent.ACTION_PERFORMED,"");
        for (ActionListener listener:actionListeners) {
            listener.actionPerformed(event);
        }
    }
}