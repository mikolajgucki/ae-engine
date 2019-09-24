package com.andcreations.ae.studio.plugins.lua.explorer.tree.actions;

import java.io.File;

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
class DeleteLuaFileAction extends AbstractPopupAction {
    /** */
    private BundleResources res =
        new BundleResources(DeleteLuaFileAction.class);    
        
    /** */
    private SelectFileDialog dialog;
        
    /** */
    DeleteLuaFileAction() {
        create();
    }
     
    /** */
    private void create() {        
        setText(res.getStr("item.text"));
        setIcon(Icons.getIcon(DefaultIcons.DELETE));
        
    // dialog
        dialog = new SelectFileDialog(
            res.getStr("multi.file.dialog.title"),
            res.getStr("multi.file.dialog.label"),
            res.getStr("multi.file.dialog.ok"),
            res.getStr("multi.file.dialog.cancel"));
    }
    
    /** */
    @Override
    public boolean canPerform(FileNode node) {
        if (node == null) {
            return false;
        }            
        return node.isFile();
    }
    
    /** */
    private void deleteFile(File file) {
        Files.get().deleteFile(file);
    }
    
    /** */
    private void deleteSingleFile(FileNode node) {
        File file = node.getFile();
        String path = ProjectLuaFiles.get().getPath(file);
        
    // dialog
        String title = res.getStr("single.file.dialog.title");
        String message = res.getStr("single.file.dialog.message",path);
        if (CommonDialogs.yesNo(title,message) == true) {
            deleteFile(file);
        }
    }
    
    /** */
    private void deleteMultiFile(FileNode node) {
        File file = dialog.showDialog(node.getFiles());
        if (file != null) {
            deleteFile(file);
        }
    }
    
    /** */
    @Override
    void perform(FileNode node) {       
    // single file
        if (node.getFileCount() == 1) {
            deleteSingleFile(node);
            return;
        }
        else {        
        // multiple files
            deleteMultiFile(node);
        }
    }
}