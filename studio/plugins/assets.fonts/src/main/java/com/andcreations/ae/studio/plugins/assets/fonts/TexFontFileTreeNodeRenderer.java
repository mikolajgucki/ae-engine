package com.andcreations.ae.studio.plugins.assets.fonts;

import com.andcreations.ae.studio.plugins.file.explorer.tree.DefaultFileTreeNodeRenderer;
import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeNode;
import com.andcreations.io.FileNode;

/**
 * @author Mikolaj Gucki
 */
public class TexFontFileTreeNodeRenderer extends DefaultFileTreeNodeRenderer {
    /** */
    @Override
    public FileTreeNode createFileTreeNode(final FileNode fileNode) {
        if (TexFont.get().isTexFontFile(fileNode.getFile()) == false) {        
            return null;
        }        
        
    // create node
        FileTreeNode node = super.createFileTreeNode(fileNode);
        node.setIconName(FontsIcons.FONT);
        
        return node;
    }
}