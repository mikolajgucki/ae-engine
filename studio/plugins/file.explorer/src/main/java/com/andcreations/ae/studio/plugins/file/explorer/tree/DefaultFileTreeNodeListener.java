package com.andcreations.ae.studio.plugins.file.explorer.tree;

import java.io.File;

import com.andcreations.ae.studio.plugins.text.editor.DefaultTextEditor;

/**
 * @author Mikolaj Gucki
 */
public class DefaultFileTreeNodeListener extends FileTreeNodeAdapter {
    /** */
    @Override
    public boolean fileTreeNodeDoubleClicked(FileTreeNode node) {
        File file = node.getFile();
        if (file.isFile() == true) {
            DefaultTextEditor.get().edit(file);
        }
        
        return true;
    }    
}