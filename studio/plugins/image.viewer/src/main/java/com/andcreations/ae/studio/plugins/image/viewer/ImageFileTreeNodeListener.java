package com.andcreations.ae.studio.plugins.image.viewer;

import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeNode;
import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeNodeAdapter;

/**
 * @author Mikolaj Gucki
 */
public class ImageFileTreeNodeListener extends FileTreeNodeAdapter {
    /** */
    @Override
    public boolean fileTreeNodeSelected(FileTreeNode node) {
        if (ImageViewer.get().isImageFile(node.getFile()) == false) {
            return false;
        }
        
        ImageViewer.get().showImage(node.getFile());
        return true;        
    }
    
    /** */
    @Override
    public boolean fileTreeNodeDoubleClicked(FileTreeNode node) {
        if (ImageViewer.get().isImageFile(node.getFile()) == false) {
            return false;
        }        
        
        ImageViewer.get().showViewAndImage(node.getFile());
        return true;
    }    
}