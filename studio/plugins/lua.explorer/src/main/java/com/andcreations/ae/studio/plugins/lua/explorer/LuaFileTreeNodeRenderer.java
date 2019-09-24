package com.andcreations.ae.studio.plugins.lua.explorer;

import java.io.File;

import com.andcreations.ae.studio.plugins.file.explorer.tree.DefaultFileTreeNodeRenderer;
import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeNode;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.io.FileNode;

/**
 * @author Mikolaj Gucki
 */
public class LuaFileTreeNodeRenderer extends DefaultFileTreeNodeRenderer {
    /** */
    @Override
    public FileTreeNode createFileTreeNode(final FileNode fileNode) {
        File file = fileNode.getFile();
        if (file.isFile() == false ||
            ProjectLuaFiles.get().isLuaFile(file) == false) {
        //
            return null;
        }
        
    // create node
        FileTreeNode node = super.createFileTreeNode(fileNode);
        node.setIconName(ProjectLuaFiles.get().getLuaFileIconName(file));
        
        return node;
    }
}