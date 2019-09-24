package com.andcreations.ae.studio.plugins.file.explorer.tree;

import java.util.List;

import javax.swing.JPopupMenu;

/**
 * @author Mikolaj Gucki
 */
public interface FileTreeComponentListener {
    /**
     * Called when a file tree node selection has changed.
     *
     * @param component The component.
     * @param nodes The selected nodes.
     */
    void fileTreeNodeSelectionChanged(FileTreeComponent component,
        List<FileTreeNode> nodes);
    
    /**
     * Appends popup menu items.
     *
     * @param component The component.
     * @param nodes The selected nodes.
     * @param menu The menu.
     */
    void appendFileTreeNodeMenuItems(FileTreeComponent component,
        List<FileTreeNode> nodes,JPopupMenu menu);
}