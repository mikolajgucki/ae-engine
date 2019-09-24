package com.andcreations.ae.studio.plugins.assets.fonts;

import java.io.File;

import com.andcreations.ae.assets.TexFontBuilder;
import com.andcreations.ae.studio.plugins.file.validation.RelativeFilePathVerifier;
import com.andcreations.ae.studio.plugins.project.files.ProjectFiles;
import com.andcreations.ae.studio.plugins.ui.common.dialog.AbstractCreateFileDialog;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class CreateTexFontDialog extends AbstractCreateFileDialog {
    /** */
    private BundleResources res =
        new BundleResources(CreateTexFontDialog.class);
        
    /** */
    private RelativeFilePathVerifier verifier;        
        
    /** */
    CreateTexFontDialog() {
        super(MainFrame.get());
        create();
    }
    
    /** */
    private void create() {
        makeEscapable();
        setTitle(res.getStr("title"));
        
        create(res.getStr("path"),res.getStr("name"));
        verifier = new RelativeFilePathVerifier(getNameField(),
            res.getStr("invalid.name"));
    }
    
    /** */
    File getFile() {
        String path = String.format("%s%s",getFileName(),
            TexFontBuilder.TEX_FONT_FILE_SUFFIX);
        return new File(Fonts.get().getFontsDir(),path);
    }
    
    /** */
    private void updatePath() {
        File file = getFile();
        setPath(ProjectFiles.get().getRelativePath(file));
    }    
    
    /** */
    private void verify() {
        boolean ok = true;
    // name
        if (verifier.verify() == false) {
            setError(verifier.getErrorToolTip());
            ok = false;
        }
        
    // check file exists
        if (ok == true) {
            File file = getFile();
            if (file.exists() == true) {
                setError(res.getStr("file.exists"));
                ok = false;
            }
        }
        
    // if no errors
        if (ok == true) {
            setNoError();
        }
    }    

    /** */
    @Override
    protected void nameChanged(String name) {
        updatePath();
        verify();
    }
    
    /** */
    boolean showCreateTexFontDialog() {
        return showCreateFileDialog();
    }    
}