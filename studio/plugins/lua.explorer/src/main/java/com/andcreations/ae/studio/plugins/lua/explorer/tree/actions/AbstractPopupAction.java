package com.andcreations.ae.studio.plugins.lua.explorer.tree.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import com.andcreations.io.FileNode;

/**
 * @author Mikolaj Gucki
 */
abstract class AbstractPopupAction implements PopupAction {
    /** */
    private String text;
    
    /** */
    private ImageIcon icon;
    
    /** */
    protected AbstractPopupAction() {
    }
    
    /** */
    protected void setText(String text) {
        this.text = text;        
    }
    
    /** */
    protected void setIcon(ImageIcon icon) {
        this.icon = icon;
    }
    
    /** */
    @Override
    public JMenuItem createMenuItem(final FileNode node) {
        JMenuItem item = new JMenuItem(text,icon);
        item.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                perform(node);
            }
        });
        
        return item;
    }
    
    /** */
    @Override
    public abstract boolean canPerform(FileNode node);
    
    /** */
    abstract void perform(FileNode node);
}