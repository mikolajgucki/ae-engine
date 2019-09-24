package com.andcreations.io;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

/**
 * @author Mikolaj Gucki
 */
public class UpToDate {
    /** */
    public static boolean is(File[] srcFiles,File[] dstFiles) {
        return is(Arrays.asList(srcFiles),Arrays.asList(dstFiles));
    }
    
    /** */
    public static boolean is(Iterable<File> srcFiles,File[] dstFiles) {
        return is(srcFiles,Arrays.asList(dstFiles));
    }
    
    /** */
    public static boolean is(File[] srcFiles,Iterable<File> dstFiles) {
        return is(Arrays.asList(srcFiles),dstFiles);
    }
    
    /** */
    public static boolean is(Iterable<File> srcFiles,Iterable<File> dstFiles) {
        for (File srcFile:srcFiles) {
            if (srcFile.exists() == false) {
                continue;
            }
            for (File dstFile:dstFiles) {
                if (dstFile.exists() == false) {
                    return false;
                }
                if (FileUtils.isFileNewer(srcFile,dstFile)) {
                    return false;
                }
            }
        }
        
        return true;
    }
}