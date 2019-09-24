package com.andcreations.ae.studio.plugins.file.explorer.tree.actions;

import java.io.File;
import java.io.IOException;

import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class AddFileAction extends AddItemAction {
    /** */
    private BundleResources res = new BundleResources(AddFileAction.class);
    
    /** */
    AddFileAction() {
        create();
    }
    
    /** */
    private void create() {
        create(res.getStr("action.name"),
            Icons.getIcon(DefaultIcons.FILE,DefaultIcons.DECO_ADD),
            res.getStr("dialog.title"));                   
    }
    
    /** */
    @Override
    protected void addItem(File file) {
        try {
            Files.get().createFile(file);
        } catch (IOException exception) {
            CommonDialogs.error(res.getStr("exception.dialog.title"),
                exception);
            return;
        }        
    }
}