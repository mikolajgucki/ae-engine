package com.andcreations.ae.studio.plugins.file.explorer.tree;

import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.io.FileNode;

/**
 * @author Mikolaj Gucki
 */
public class DefaultFileTreeNodeRenderer implements FileTreeNodeRenderer {
    /** */
    @Override
    public FileTreeNode createFileTreeNode(FileNode fileNode) {
    // icon
        String iconName = null;
        if (fileNode.isDirectory() == true) {
            iconName = DefaultIcons.PROJECT_DIRECTORY;
        }
        else {
            iconName = DefaultIcons.PROJECT_FILE;
        }
        
        String name = fileNode.getFile().getName();
        return new FileTreeNode(iconName,name,name,fileNode);
    }
}