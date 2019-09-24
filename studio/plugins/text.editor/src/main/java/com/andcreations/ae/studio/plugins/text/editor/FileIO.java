package com.andcreations.ae.studio.plugins.text.editor;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * Responsible for reading/writing files.
 *
 * @author Mikolaj Gucki
 */
class FileIO {
    /** */
    private static final String ENCODING = "UTF-8";
    
    /** */
    static String read(File file) throws IOException {
        return FileUtils.readFileToString(file,ENCODING);
    }
    
    /** */
    static void write(File file,String data) throws IOException {
        FileUtils.writeStringToFile(file,data,ENCODING);
    }
}