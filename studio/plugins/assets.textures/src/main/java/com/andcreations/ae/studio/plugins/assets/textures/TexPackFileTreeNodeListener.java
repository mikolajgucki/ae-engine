package com.andcreations.ae.studio.plugins.assets.textures;

import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeNode;
import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeNodeAdapter;

/**
 * @author Mikolaj Gucki
 */
public class TexPackFileTreeNodeListener extends FileTreeNodeAdapter {
    /** */
    @Override
    public boolean fileTreeNodeDoubleClicked(FileTreeNode node) {
        if (TexPack.get().isTexPackFile(node.getFile()) == false) {        
            return false;
        }
        
    // edit
        TexPack.get().edit(node.getFile());        
        return true;
    }
}
