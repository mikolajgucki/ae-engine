package com.andcreations.ae.studio.plugins.assets.fonts;

import java.io.File;
import java.io.IOException;

import org.apache.velocity.VelocityContext;

import com.andcreations.ae.studio.plugins.assets.fonts.resources.R;
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
public class CreateTexFontWizard extends AbstractWizard {
    /** */
    private final BundleResources res =
        new BundleResources(CreateTexFontWizard.class); 
     
    /** */
    private CreateTexFontDialog dialog;        
        
    /** */
    CreateTexFontWizard() {
        create();
    }
    
    /** */
    private void create() {
        setName(res.getStr("name"));
        setIcon(Icons.getIcon(FontsIcons.FONT,DefaultIcons.DECO_ADD));
        setDesc(res.getStr("desc"));  
        
    // dialog
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                dialog = new CreateTexFontDialog();
            }
        });         
    }
    
    /** */
    private void createTexFontFile(File file) {
        String src = Template.evaluate(
            R.class,"texfont.vm",new VelocityContext());
        if (src == null) {
            return;
        }
        
        try {
            Files.get().createFile(file,src);
        } catch (IOException exception) {
            CommonDialogs.error(res.getStr("exception.dialog.title"),exception);
            return;
        }
        TexFont.get().edit(file);
    }
    
    /** */
    @Override
    public void runWizard() {
        if (dialog.showCreateTexFontDialog() == true) {
            createTexFontFile(dialog.getFile());
        }        
    }
}