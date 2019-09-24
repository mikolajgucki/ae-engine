package com.andcreations.ae.studio.plugins.file.explorer.tree;

/**
 * @author Mikolaj Gucki
 */
public interface FileTreeNodeListener {
    /**
     * Called when a file tree node has been selected.
     *
     * @param node The node.
     * @return <code>true</code> if consumes the event, <code>false</code>
     *   otherwise.
     */ 
    boolean fileTreeNodeSelected(FileTreeNode node);
    
    /**
     * Called when a file tree node has been double-clicked.
     *
     * @param node The node.
     * @return <code>true</code> if consumes the event, <code>false</code>
     *   otherwise.
     */ 
    boolean fileTreeNodeDoubleClicked(FileTreeNode node);
}