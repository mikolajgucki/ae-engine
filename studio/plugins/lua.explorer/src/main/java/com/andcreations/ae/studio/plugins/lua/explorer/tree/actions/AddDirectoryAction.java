package com.andcreations.ae.studio.plugins.lua.explorer.tree.actions;

import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.io.FileNode;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class AddDirectoryAction extends AbstractPopupAction {
    /** */
    private BundleResources res =
        new BundleResources(AddDirectoryAction.class);    
        
    /** */
    private AddDirectoryDialog dialog;
        
    /** */
    AddDirectoryAction() {
        create();
    }
     
    /** */
    private void create() {        
        setText(res.getStr("item.text"));
        setIcon(Icons.getIcon(DefaultIcons.DIRECTORY,DefaultIcons.DECO_ADD));
        
    // dialog
        dialog = new AddDirectoryDialog();
    }
    
    /** */
    @Override
    public boolean canPerform(FileNode node) {
        if (node == null) {
            return true;
        }
        
        if (node.isDirectory() == true) {
            return true;
        }
        
        FileNode parent = node.getParent();
        if (parent != null && parent.isDirectory() == true) {
            return true;
        }
        
        return false;
    }
    
    /** */
    @Override
    void perform(FileNode node) {
        if (node == null) {
            node = ProjectLuaFiles.get().getLuaSourceTree().getRoot();
        }        
        
        if (node.isDirectory() == true) {
            dialog.showDialog(node);
            return;
        }
        
        FileNode parent = node.getParent();
        if (parent != null && parent.isDirectory() == true) {
            dialog.showDialog(parent);
        }        
    }
}