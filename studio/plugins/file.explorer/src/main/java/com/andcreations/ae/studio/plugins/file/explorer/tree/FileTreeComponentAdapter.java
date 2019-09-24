package com.andcreations.ae.studio.plugins.file.explorer.tree;

import java.util.List;

import javax.swing.JPopupMenu;

/**
 * @author Mikolaj Gucki
 */
public class FileTreeComponentAdapter implements FileTreeComponentListener {
    /** */
    @Override
    public void fileTreeNodeSelectionChanged(FileTreeComponent component,
        List<FileTreeNode> nodes) {
    //
    }
    
    /** */
    @Override
    public void appendFileTreeNodeMenuItems(FileTreeComponent component,
        List<FileTreeNode> nodes,JPopupMenu menu) {
    //
    }
}