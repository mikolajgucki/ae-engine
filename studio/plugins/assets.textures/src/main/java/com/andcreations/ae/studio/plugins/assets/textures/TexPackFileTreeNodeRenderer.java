package com.andcreations.ae.studio.plugins.assets.textures;

import com.andcreations.ae.studio.plugins.file.explorer.tree.DefaultFileTreeNodeRenderer;
import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeNode;
import com.andcreations.io.FileNode;

/**
 * @author Mikolaj Gucki
 */
public class TexPackFileTreeNodeRenderer extends DefaultFileTreeNodeRenderer {
    /** */
    @Override
    public FileTreeNode createFileTreeNode(final FileNode fileNode) {
        if (TexPack.get().isTexPackFile(fileNode.getFile()) == false) {        
            return null;
        }
        
    // create node
        FileTreeNode node = super.createFileTreeNode(fileNode);
        node.setIconName(TexturesIcons.TEXTURE);
        
        return node;
    }
}