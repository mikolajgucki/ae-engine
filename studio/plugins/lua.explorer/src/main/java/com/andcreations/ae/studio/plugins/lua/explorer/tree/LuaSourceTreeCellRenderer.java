package com.andcreations.ae.studio.plugins.lua.explorer.tree;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.lua.LuaIcons;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.io.FileNode;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class LuaSourceTreeCellRenderer extends DefaultTreeCellRenderer {
    /** */
    private BundleResources res = new BundleResources(
        LuaSourceTreeCellRenderer.class);
    
    /** */
    private boolean hasErrors(FileNode node) {
        if (ProjectLuaFiles.getError(node) != null) {
            return true;
        }
        if (Files.get().hasErrors(node.getFile()) == true) {
            return true;
        }
        return false;
    }
    
    /** */
    private boolean hasWarnings(FileNode node) {
        if (Files.get().hasWarnings(node.getFile()) == true) {
            return true;
        }
        return false;
    }
    
    /** */
    private boolean childrenHasErrors(FileNode node) {
        synchronized (node) {
            if (node.getChildNodes() != null) {
                for (FileNode childNode:node.getChildNodes()) {
                    if (hasErrors(childNode) == true ||
                        childrenHasErrors(childNode) == true) {
                    //
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    /** */
    private boolean childrenHasWarnings(FileNode node) {
        synchronized (node) {
            if (node.getChildNodes() != null) {
                for (FileNode childNode:node.getChildNodes()) {
                    if (hasWarnings(childNode) == true ||
                        childrenHasWarnings(childNode) == true) {
                    //
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    /** */
    public Component getTreeCellRendererComponent(JTree tree,Object value,
        boolean selected,boolean expanded,boolean leaf,int row,
        boolean hasFocus) {
    // root
        if (value instanceof LuaSourceTreeRootNode) {
            return new JLabel(res.getStr("root.node.text"));
        }
    
        LuaSourceTreeNode node = (LuaSourceTreeNode)value;
        FileNode fileNode = node.getFileNode();
        
    // label
        JLabel label = (JLabel)super.getTreeCellRendererComponent(
            tree,value,selected,expanded,leaf,row,hasFocus);
        label.setText(fileNode.getName());
        label.setBorder(BorderFactory.createEmptyBorder(0,0,1,0));
        
    // tooltip
        String error = ProjectLuaFiles.getError(fileNode);
        if (error != null) {
            label.setToolTipText(error);
        }
        else {
            String tooltip = "<html>";
            for (File file:fileNode.getFiles()) {
                tooltip += file.getAbsolutePath() + "<br/>";
            }
            tooltip += "</html>";
            label.setToolTipText(tooltip);
        }

    // issues
        boolean hasErrors = hasErrors(fileNode) ||
            childrenHasErrors(fileNode);
        boolean hasWarnings = hasWarnings(fileNode) ||
            childrenHasWarnings(fileNode);
        
    // file
        File file = fileNode.getFile();
        
    // test
        boolean isTestFile = false;
        if (file != null) {
            isTestFile = ProjectLuaFiles.get().isLuaTestFile(file) ||
                ProjectLuaFiles.get().isLuaTestDir(file);
        }
        
    // icon
        List<String> icons = new ArrayList<>();
        if (fileNode.hasFiles() && fileNode.hasDirs()) {
        // mixed files and directories (it's an error)
            icons.add(DefaultIcons.DIRECTORY);
        }
        if (fileNode.isDirectory() == true) {
            icons.add(ProjectLuaFiles.get().getLuaDirIconName(
                fileNode.getFiles()));
        }            
        if (fileNode.isFile() == true) {
            if (file == null) {
            // multiple files (it's an error)
                icons.add(LuaIcons.LUA_FILE);
            }
            else {
                icons.add(ProjectLuaFiles.get().getLuaFileIconName(file));
            }
        }
        if (hasErrors == true) {
            icons.add(DefaultIcons.DECO_ERROR);
        }
        else if (hasWarnings == true) {
            icons.add(DefaultIcons.DECO_WARNING);
        }
        if (isTestFile == true) {
            icons.add(DefaultIcons.DECO_TEST);
        }
        label.setIcon(Icons.getIcon(icons));
    
        return label;
    }   
}