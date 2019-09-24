package com.andcreations.ae.sdk.update;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * @author Mikolaj Gucki
 */
public class FileCopyStrategy implements FileUpdateStrategy {
    /** */
    public static final FileCopyStrategy INSTANCE = new FileCopyStrategy();
    
    /** */
    private FileCopyStrategy() {
    }
    
    /** */
    public void updateFile(File srcFile,File dstFile) throws IOException {
        FileUtils.copyFile(srcFile,dstFile);
    }
}