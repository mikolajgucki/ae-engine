package com.andcreations.ae.studio.plugins.lua.explorer;

import java.awt.BorderLayout;
import java.io.File;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.andcreations.ae.studio.plugins.file.EditedFile;
import com.andcreations.ae.studio.plugins.file.FileAdapter;
import com.andcreations.ae.studio.plugins.file.FileIssue;
import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.lua.explorer.tree.LuaSourceTree;
import com.andcreations.ae.studio.plugins.lua.explorer.tree.LuaSourceTreeListener;
import com.andcreations.io.FileNode;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class LuaExplorerComponent extends JPanel {
    /** */
    private LuaSourceTree tree;
    
    /** */
    LuaExplorerComponent() {
        create();
    }
    
    /** */
    private void create() {
        setLayout(new BorderLayout());
        
    // tree
        tree = new LuaSourceTree();
        JScrollPane treePane = new JScrollPane(tree);
        add(treePane,BorderLayout.CENTER);
        
    // tree listener
        tree.addLuaSourceTreeListener(new LuaSourceTreeListener() {
            /** */
            @Override
            public void fileNodeDoubleClicked(FileNode fileNode) {
                editFile(fileNode);                
            }
        });
        
    // file listener
        Files.get().addFileListener(new FileAdapter() {
            /** */
            @Override    
            public void fileIssuesChanged(File file,List<FileIssue> issues) {
                tree.repaint();
            }                
        });
    }
    
    /** */
    private void editFile(FileNode fileNode) {
        if (fileNode.isDirectory() == true) {
            return;
        }
        if (fileNode.getFileCount() != 1) {
            return;
        }
        
        LuaFile.edit(fileNode.getFile());
    }
    
    /** */
    void viewFocusGained() {
        tree.viewFocusGained();
    }
    
    /** */
    void syncWithEditor() {
        File file = EditedFile.get().getLastEditedFile();
        if (file == null) {
            return;
        }
        tree.goTo(file);
    }
}