package com.andcreations.ae.sdk;

import java.io.File;

import com.andcreations.ae.project.AEProjectProperties;

/**
 * Fetches all the music files from an AndEngine project.
 *
 * @author Mikolaj Gucki
 */
public class FetchMusicAntTask extends FetchProjectFilesAntTask {
    /**
     * Constructs a {@link FetchMusicAntTask}.
     */
    public FetchMusicAntTask() {
        super(AEProjectProperties.MUSIC_DIRS);
    }
    
    /** */
    @Override
    protected void fetchFile(File srcFile,File dstFile) {
        copyFile(srcFile,dstFile);
    }
}