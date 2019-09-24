package com.andcreations.ae.studio.plugins.file.explorer.tree.actions;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeNode;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class DeleteAction extends AbstractPopupAction {
    /** */
    private BundleResources res =
        new BundleResources(DeleteAction.class);     
    
    /** */
    DeleteAction() {
        create();
    }
    
    /** */
    private void create() {
        setText(res.getStr("item.text"));
        setIcon(Icons.getIcon(DefaultIcons.DELETE));        
    }
    
    /** */
    @Override
    public boolean canPerform(FileTreeNode root,List<FileTreeNode> nodes) {
        if (nodes == null || nodes.isEmpty() == true) {
            return false;
        }
        
    // cannot delete root
        for (FileTreeNode node:nodes){
            if (node == root) {
                return false;
            }
        }
        
        return true;
    }
    
    /** */
    private void delete(FileTreeNode node) {
        File file = node.getFile();
        try {
            if (file.isDirectory() == true) {
                Files.get().deleteDir(file);
            }            
            else if (file.isFile() == true) {
                Files.get().deleteFile(file);
            }
        } catch (IOException exception) {
            CommonDialogs.error(res.getStr("exception.dialog.title"),exception);
            return;             
        }        
    }
    
    /** */
    private void delete(List<FileTreeNode> nodes) {
        for (FileTreeNode node:nodes) {
            delete(node);
        }
    }
    
    /** */
    public void perform(FileTreeNode root,List<FileTreeNode> nodes) {
    // dialog
        String title = res.getStr("dialog.title");
        String message = res.getStr("dialog.message");
        if (CommonDialogs.yesNo(title,message) == true) {
            delete(nodes);
        }    
    }
}