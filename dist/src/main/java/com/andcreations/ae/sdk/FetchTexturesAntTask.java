package com.andcreations.ae.sdk;

import java.io.File;

import com.andcreations.ae.project.AEProjectProperties;

/**
 * Fetches all the textures from an AndEngine project.
 *
 * @author Mikolaj Gucki
 */
public class FetchTexturesAntTask extends FetchProjectFilesAntTask {
    /**
     * Constructs a {@link FetchTexturesAntTask}.
     */
    public FetchTexturesAntTask() {
        super(AEProjectProperties.TEXTURE_DIRS);
    }
        
    /** */
    @Override
    protected void fetchFile(File srcFile,File dstFile) {
        copyFile(srcFile,dstFile);
    }    
}