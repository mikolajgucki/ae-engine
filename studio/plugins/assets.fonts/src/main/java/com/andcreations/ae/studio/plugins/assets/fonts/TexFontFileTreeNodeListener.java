package com.andcreations.ae.studio.plugins.assets.fonts;

import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeNode;
import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeNodeAdapter;

/**
 * @author Mikolaj Gucki
 */
public class TexFontFileTreeNodeListener extends FileTreeNodeAdapter {
    /** */
    @Override
    public boolean fileTreeNodeDoubleClicked(FileTreeNode node) {
        if (TexFont.get().isTexFontFile(node.getFile()) == false) {        
            return false;
        }
        
    // edit
        TexFont.get().edit(node.getFile());        
        return true;
    }
}
