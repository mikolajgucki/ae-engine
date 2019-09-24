package com.andcreations.ae.studio.plugins.lua.explorer.tree.actions;

import javax.swing.JMenuItem;

import com.andcreations.io.FileNode;

/**
 * @author Mikolaj Gucki
 */
public interface PopupAction {
    /** */
    boolean canPerform(FileNode node);
    
    /** */
    JMenuItem createMenuItem(FileNode node);
}