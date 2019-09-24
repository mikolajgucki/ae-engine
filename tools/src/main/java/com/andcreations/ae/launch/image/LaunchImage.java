package com.andcreations.ae.launch.image;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.image.Image;

/**
 * @author Mikolaj Gucki
 */
public class LaunchImage {
    /** */
    private LaunchImageEntryList entries;
    
    /** */
    private LaunchImageLayout layout;
    
    /** */
    private List<LaunchImageListener> listeners = new ArrayList<>();
    
    /** */
    public LaunchImage(LaunchImageEntryList entries,LaunchImageLayout layout) {
        this.entries = entries;
        this.layout = layout;
    }
    
    /** */
    public void addLaunchImageListener(LaunchImageListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    public void removeLaunchImageListener(LaunchImageListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }
    
    /** */
    private Image generate(File dstDir,LaunchImageEntry entry)
        throws IOException {
    //
        return layout.layout(entry);
    }
    
    /** */
    public void generate(File dstDir) throws IOException {
        for (LaunchImageEntry entry:entries.getEntries()) {
            File dstFile = new File(dstDir,entry.getFilename());
            
        // notify
            synchronized (listeners) {
                for (LaunchImageListener listener:listeners) {
                    listener.generatingImage(dstFile,entry);
                }
            }
    
        // generate
            Image dstImage = generate(dstDir,entry);
            
        // save
            dstFile.getParentFile().mkdirs();
            dstImage.savePNG(dstFile);
        }
    }
    
    /** */
    private static LaunchImage load(String name,LaunchImageLayout layout)
        throws IOException {
    //
        LaunchImageEntryList list = LaunchImageEntryList.load(name);
        return new LaunchImage(list,layout);
    }
    
    /** */
    public static LaunchImage ios(LaunchImageLayout layout) throws IOException {
        return load("ios.json",layout);
    }    
}