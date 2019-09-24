package com.andcreations.ae.sdk.update;

import java.io.File;
import java.io.IOException;

import com.andcreations.ae.io.HashUtils;

/**
 * @author Mikolaj Gucki
 */
public class FileHashStrategy implements FileUpdateStrategy {
    /** */
    public static final FileHashStrategy INSTANCE = new FileHashStrategy();
    
    /** */
    private FileHashStrategy() {
    }
    
    /** */
    public void updateFile(File srcFile,File dstFile) throws IOException {
        HashUtils.hash(srcFile,dstFile);
    }
}