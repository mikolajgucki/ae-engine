package com.andcreations.ae.studio.plugins.assets.textures;

import java.io.File;
import java.io.IOException;

import org.apache.velocity.VelocityContext;

import com.andcreations.ae.studio.plugins.assets.textures.resources.R;
import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.common.template.Template;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.ae.studio.plugins.wizards.AbstractWizard;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class CreateTexPackWizard extends AbstractWizard {
    /** */
    private final BundleResources res =
        new BundleResources(CreateTexPackWizard.class);
        
    /** */
    private CreateTexPackDialog dialog;
        
    /** */
    CreateTexPackWizard() {
        create();
    }
    
    /** */    
    private void create() {
        setName(res.getStr("name"));
        setIcon(Icons.getIcon(TexturesIcons.TEXTURE,DefaultIcons.DECO_ADD));
        setDesc(res.getStr("desc"));   
        
    // dialog
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                dialog = new CreateTexPackDialog();
            }
        });        
    }
    
    /** */
    private void createTexPackFile(File file) {
        String src = Template.evaluate(
            R.class,"texpack.vm",new VelocityContext());
        if (src == null) {
            return;
        }
        
        try {
            Files.get().createFile(file,src);
        } catch (IOException exception) {
            CommonDialogs.error(res.getStr("exception.dialog.title"),exception);
            return;
        }
        TexPack.get().edit(file);
    }
    
    /** */
    @Override
    public void runWizard() {
        if (dialog.showCreateTexPackDialog() == true) {
            createTexPackFile(dialog.getFile());
        }
    }
}