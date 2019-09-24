package com.andcreations.ae.studio.plugins.file.explorer.tree.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeNode;

/**
 * @author Mikolaj Gucki
 */
public abstract class AbstractPopupAction implements PopupAction {
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
    public JMenuItem createMenuItem(final FileTreeNode root,
        final List<FileTreeNode> nodes) {
    //
        JMenuItem item = new JMenuItem(text,icon);
        item.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                perform(root,nodes);
            }
        });
        
        return item;
    }
    
    /** */
    @Override
    public abstract boolean canPerform(FileTreeNode root,
        List<FileTreeNode> nodes);
    
    /** */
    public abstract void perform(FileTreeNode root,List<FileTreeNode> nodes);
}