package com.andcreations.ae.studio.plugins.project.explorer;

import java.io.File;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.appicon.AppIcon;
import com.andcreations.ae.studio.plugins.file.explorer.tree.DefaultFileTreeNodeRenderer;
import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeNode;
import com.andcreations.io.FileNode;

/**
 * @author Mikolaj Gucki
 */
class ProjectExplorerFileTreeNodeRenderer extends DefaultFileTreeNodeRenderer {
    /** */
    private File projectDir;

    /** */
    ProjectExplorerFileTreeNodeRenderer(File projectDir) {
        this.projectDir = projectDir;
    }
    
    /** */
    @Override
    public FileTreeNode createFileTreeNode(FileNode fileNode) {
    // project directory?
        File file = fileNode.getFile();
        if (file.equals(projectDir) == false) {
            return null;
        }
        
    // load icon
        ImageIcon appIcon = AppIcon.get().loadIcon();
        if (appIcon == null) {
            return null;
        }
        
    // create not with the icon
        FileTreeNode node = super.createFileTreeNode(fileNode);
        node.setIcon(appIcon);
            
        return node;
    }    
}