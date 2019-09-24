package com.andcreations.ae.studio.plugins.lua.wizards;

import java.io.File;
import java.io.IOException;

import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.lua.LuaIcons;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.ae.studio.plugins.wizards.AbstractWizard;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class CreateLuaFileWizard extends AbstractWizard {
    /** */
    private final BundleResources res =
        new BundleResources(CreateLuaFileWizard.class);
        
    /** */
    private CreateLuaFileDialog dialog;
        
    /** */
    CreateLuaFileWizard() {
        create();
    }
    
    /** */
    private void create() {
        setName(res.getStr("name"));
        setIcon(Icons.getIcon(LuaIcons.LUA_FILE,DefaultIcons.DECO_ADD));
        setDesc(res.getStr("desc"));
        
    // dialog
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                dialog = new CreateLuaFileDialog();
            }
        });
    }
    
    /** */
    private void createFile(File file) {        
        try {
        // directory
            File dir = file.getParentFile();
            if (dir.exists() == false) {
                Files.get().createDir(dir);
            }
            
        // file
            Files.get().createFile(file);
        } catch (IOException exception) {
            CommonDialogs.error(res.getStr("exception.dialog.title"),exception);
            return;
        }
        LuaFile.edit(file);
    }
    
    /** */
    @Override
    public void runWizard() {
        if (dialog.showCreateLuaFileDialog() == true) {
            File file = dialog.getFile();
            createFile(file);
        }
    }
}