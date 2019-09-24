package com.andcreations.ae.studio.plugins.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class EditedFile {
    /** */
    private static EditedFile instance;
    
    /** */
    private File lastEditedFile;
    
    /** */
    private List<EditedFileListener> listeners = new ArrayList<>();
    
    /** */
    public void setLastEditedFile(File lastEditedFile) {
        this.lastEditedFile = lastEditedFile;
        synchronized (listeners) {
            for (EditedFileListener listener:listeners) {
                listener.editedFileChanged(lastEditedFile);
            }
        }
    }
    
    /** */
    public File getLastEditedFile() {
        return lastEditedFile;  
    }
    
    /** */
    public void addEditedFileListener(EditedFileListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    public static EditedFile get() {
        if (instance == null) {
            instance = new EditedFile();
        }
        
        return instance;
    }
}