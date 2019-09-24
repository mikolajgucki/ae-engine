package com.andcreations.ae.studio.plugins.lua.explorer.tree.actions;

import java.io.File;
import java.io.IOException;

import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.io.FileNode;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class DeleteDirectoryAction extends AbstractPopupAction {
    /** */
    private BundleResources res =
        new BundleResources(DeleteDirectoryAction.class);    
        
    /** */
    DeleteDirectoryAction() {
        create();
    }
     
    /** */
    private void create() {        
        setText(res.getStr("item.text"));
        setIcon(Icons.getIcon(DefaultIcons.DELETE));
    }
    
    /** */
    @Override
    public boolean canPerform(FileNode node) {
        if (node == null) {
            return false;
        }            
        return node.isDirectory();
    }
    
    /** */
    private void deleteDir(File dir) {
        try {
            Files.get().deleteDir(dir);
        } catch (IOException exception) {
            CommonDialogs.error(res.getStr("exception.dialog.title"),exception);
            return;               
        }        
    }
    
    /** */
    @Override
    void perform(FileNode node) {       
        File dir = node.getFile();
        String path = ProjectLuaFiles.get().getPath(dir);
        if (path == null) {
            path = node.getName();
        }
        
    // dialog
        String title = res.getStr("dialog.title");
        String message = res.getStr("dialog.message",path);
        if (CommonDialogs.yesNo(title,message) == true) {
            deleteDir(dir);
        }
    }
}