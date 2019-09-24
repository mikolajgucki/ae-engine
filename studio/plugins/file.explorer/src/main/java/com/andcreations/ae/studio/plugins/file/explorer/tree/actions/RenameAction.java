package com.andcreations.ae.studio.plugins.file.explorer.tree.actions;

import java.io.File;
import java.util.List;

import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeNode;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class RenameAction extends AbstractPopupAction {
    /** */
    private BundleResources res = new BundleResources(RenameAction.class);
    
    /** */
    private RenameDialog dialog;
    
    /** */
    RenameAction() {
        create();
    }
    
    /** */
    private void create() {
        setText(res.getStr("action.name"));
        setIcon(Icons.getIcon(DefaultIcons.FIELD));        
        
    // dialog
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override            
            public void run() {
                dialog = new RenameDialog();
            }
        });
    }
    
    /** */
    @Override
    public boolean canPerform(FileTreeNode root,List<FileTreeNode> nodes) {
        if (nodes.size() != 1) {
            return false;
        }
        
    // cannot rename root
        return root != nodes.get(0);
    }

    /** */
    private void rename(File src,String newName) {
        if (newName.equals(src.getName()) == true) {
            return;
        }
        
        File dst = new File(src.getParentFile(),newName);
        if (dst.exists() == true) {
            // TODO Show dialog asking if to delete the destination file.
        }
        
        if (Files.get().rename(src,dst) == false) {
            CommonDialogs.error(res.getStr("error.dialog.title"),
                res.getStr("error.dialog.msg",src.getAbsolutePath(),
                dst.getAbsolutePath()));            
        }
    }
    
    /** */
    @Override
    public void perform(FileTreeNode root,List<FileTreeNode> nodes) {
        FileTreeNode node = nodes.get(0);
        File src = node.getFileNode().getFile();
        
        if (dialog.showRenameDialog(src) == true) {
            rename(src,dialog.getNewName());
        }
    }    
}