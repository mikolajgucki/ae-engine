package com.andcreations.ae.sdk;

import java.io.File;

import com.andcreations.ae.project.AEProjectProperties;

/**
 * Fetches all the Lua files from an AndEngine project.
 *
 * @author Mikolaj Gucki
 */
public class FetchLuaAntTask extends FetchProjectFilesAntTask {
    /**
     * Constructs a {@link FetchLuaAntTask}.
     */
    public FetchLuaAntTask() {
        super(AEProjectProperties.LUA_SRC_DIRS);
    }
    
    /** */
    @Override
    protected void fetchFile(File srcFile,File dstFile) {
        copyFile(srcFile,dstFile);
    }
}