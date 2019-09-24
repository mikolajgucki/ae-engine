package com.andcreations.ae.studio.plugins.lua.explorer.tree.actions;

import java.io.File;
import java.io.IOException;

import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class AddLuaFileDialog extends AddItemDialog {
    /** */
    private BundleResources res = new BundleResources(AddLuaFileDialog.class); 
    
    /** */
    AddLuaFileDialog() {
        setTitle(res.getStr("title"));
        setItemLabel(res.getStr("path"));
    }
    
    /** */
    protected String getPath() {
        return String.format("%s/%s.lua",itemPrefix,nameField.getText());
    }
    
    /** */
    protected File getFile() {
        File dir = (File)directoryCombo.getSelectedItem();
        return new File(dir,String.format("%s%s",nameField.getText(),
            LuaFile.DOT_SUFFIX));
    }    
    
    /** */
    @Override
    protected boolean verify() {
        boolean ok = super.verify();
        
    // verify module does not exist
        if (ok == true) {
            String path = getPath();            
            if (ProjectLuaFiles.get().getNodeByPath(path,false) != null) {
                setError(res.getStr("module.exists"));
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
            Files.get().createFile(file);
        } catch (IOException exception) {
            CommonDialogs.error(res.getStr("exception.dialog.title"),exception);
            return;
        }
        LuaFile.edit(file);
    }
}