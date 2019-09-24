package com.andcreations.ae.studio.plugins.file;

import java.io.File;

/**
 * @author Mikolaj Gucki
 */
public interface EditedFileListener {
    /** */
    void editedFileChanged(File file);
}