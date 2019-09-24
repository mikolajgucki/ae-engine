package com.andcreations.ae.studio.plugins.file.explorer.tree.actions;

import java.util.List;

import javax.swing.JMenuItem;

import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeNode;

/**
 * @author Mikolaj Gucki
 */
public interface PopupAction {
    /** */
    boolean canPerform(FileTreeNode root,List<FileTreeNode> nodes);
    
    /** */
    JMenuItem createMenuItem(FileTreeNode root,List<FileTreeNode> nodes);
}