package com.andcreations.ae.studio.plugins.lua.explorer.tree.actions;

import java.io.File;
import java.io.IOException;

import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class AddDirectoryDialog extends AddItemDialog {
    /** */
    private BundleResources res = new BundleResources(AddDirectoryDialog.class); 
    
    /** */
    AddDirectoryDialog() {
        setTitle(res.getStr("title"));
        setItemLabel(res.getStr("path"));
    }
    
    /** */
    protected String getPath() {
        return String.format("%s/%s",itemPrefix,nameField.getText());
    }
    
    /** */
    protected File getFile() {
        File dir = (File)directoryCombo.getSelectedItem();
        return new File(dir,nameField.getText());
    }    
    
    /** */
    @Override
    protected boolean verify() {
        boolean ok = super.verify();
        
    // verify module does not exist
        if (ok == true) {
            String path = getPath();
            if (ProjectLuaFiles.get().getNodeByPath(path,false) != null) {
                setError(res.getStr("directory.exists"));
                ok = false;                
            }
        }
        
        return ok;        
    }    
    
    /** */
    @Override
    protected String getItemValue() {
        return getPath();
    }
    
    /** */
    @Override
    protected void addItem() {
        if (verify() == false) {
            return;
        }        
        setVisible(false);
        
        File file = getFile();
        try {
            Files.get().createDir(file);
        } catch (IOException exception) {
            CommonDialogs.error(res.getStr("exception.dialog.title"),exception);
            return;
        }
    }
}