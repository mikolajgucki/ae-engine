package com.andcreations.ae.studio.plugins.file.explorer.tree;

/**
 * @author Mikolaj Gucki
 */
public class FileTreeNodeAdapter implements FileTreeNodeListener {
    /** */
    @Override
    public boolean fileTreeNodeSelected(FileTreeNode node) {
        return false;
    }
    
    /** */
    @Override
    public boolean fileTreeNodeDoubleClicked(FileTreeNode node) {
        return false;
    }
}