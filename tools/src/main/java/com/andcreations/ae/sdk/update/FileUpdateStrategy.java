package com.andcreations.ae.sdk.update;

import java.io.File;
import java.io.IOException;

/**
 * @author Mikolaj Gucki
 */
interface FileUpdateStrategy {
    /** */
    void updateFile(File srcFile,File dstFile) throws IOException;
}