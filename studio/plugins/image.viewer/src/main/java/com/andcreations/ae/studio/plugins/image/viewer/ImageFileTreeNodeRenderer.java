package com.andcreations.ae.studio.plugins.image.viewer;

import com.andcreations.ae.studio.plugins.file.explorer.tree.DefaultFileTreeNodeRenderer;
import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeNode;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.io.FileNode;

/**
 * @author Mikolaj Gucki
 */
public class ImageFileTreeNodeRenderer extends DefaultFileTreeNodeRenderer {
    /** */
    @Override
    public FileTreeNode createFileTreeNode(FileNode fileNode) {
        if (ImageViewer.get().isImageFile(fileNode.getFile()) == false) {
            return null;
        }
        
        FileTreeNode node = super.createFileTreeNode(fileNode);
        node.setIconName(DefaultIcons.IMAGE);
        
        return node;
    }    
}