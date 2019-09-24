package com.andcreations.ae.sdk;

import java.io.File;

import com.andcreations.ae.project.AEProjectProperties;

/**
 * Fetches all the sound files from an AndEngine project.
 *
 * @author Mikolaj Gucki
 */
public class FetchSoundsAntTask extends FetchProjectFilesAntTask {
    /**
     * Constructs a {@link FetchSoundsAntTask}.
     */
    public FetchSoundsAntTask() {
        super(AEProjectProperties.SOUND_DIRS);
    }
    
    /** */
    @Override
    protected void fetchFile(File srcFile,File dstFile) {
        copyFile(srcFile,dstFile);
    }    
}