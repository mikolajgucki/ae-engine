package com.andcreations.ae.studio.plugins.lua.explorer;

import java.io.File;

import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeNode;
import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeNodeAdapter;
import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;

/**
 * @author Mikolaj Gucki
 */
public class LuaFileTreeNodeListener extends FileTreeNodeAdapter {
    /** */
    @Override
    public boolean fileTreeNodeDoubleClicked(FileTreeNode node) {
        File file = node.getFile();
        if (file.isFile() == false ||
            ProjectLuaFiles.get().isLuaFile(file) == false) {
        //
            return false;
        }
        
    // edit
        LuaFile.edit(file);
        return true;
    }
}
