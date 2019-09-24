package com.andcreations.ae.sdk;

import java.io.File;

import com.andcreations.ae.project.AEProjectProperties;

/**
 * Fetches the assets files from a project.
 *
 * @author Mikolaj Gucki
 */
public class FetchAssetsAntTask extends FetchProjectFilesAntTask {
    /**
     * Constructs a {@link FetchAssetsAntTask}.
     */
    public FetchAssetsAntTask() {
        super(AEProjectProperties.ASSET_DIRS);
        addIgnoreProperty(AEProjectProperties.LUA_SRC_DIRS);
        addIgnoreProperty(AEProjectProperties.TEXTURE_DIRS);
        addIgnoreProperty(AEProjectProperties.SOUND_DIRS);
        addIgnoreProperty(AEProjectProperties.MUSIC_DIRS);
    }
    
    /** */
    @Override
    protected void fetchFile(File srcFile,File dstFile) {
        copyFile(srcFile,dstFile);
    }
}