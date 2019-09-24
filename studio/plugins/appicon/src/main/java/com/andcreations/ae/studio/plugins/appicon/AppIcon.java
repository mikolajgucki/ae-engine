package com.andcreations.ae.studio.plugins.appicon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import com.andcreations.ae.image.Image;
import com.andcreations.ae.project.AEProjectIcon;
import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.file.FileAdapter;
import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.project.files.ProjectFiles;

/**
 * @author Mikolaj Gucki
 */
public class AppIcon {
    /** */
    private static AppIcon instance;
    
    /** */
    private List<AppIconListener> listeners = new ArrayList<>();
    
    /** */
    void init() {
        Files.get().addFileListener(new FileAdapter() {
            /** */
            @Override
            public void fileChanged(File file) {
                notifyAppIconChanged();
            }
            
            /** */
            @Override
            public void fileCreated(File file) {
                notifyAppIconChanged();
            }            
            
            /** */
            @Override
            public void dirCreated(File dir) {
                notifyAppIconChanged();
            }            
            
            /** */
            @Override
            public void dirDeleted(File dir) {
                notifyAppIconChanged();
            } 
            
            /** */
            @Override
            public void fileRenamed(File src,File dst) {
                notifyAppIconChanged();
            }            
        });
    }
    
    /** */
    public void addAppIconListener(AppIconListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    private void notifyAppIconChanged() {
        synchronized (listeners) {
            for (AppIconListener listener:listeners) {
                listener.appIconChanged();
            }
        }
    }
    
    /** */
    public ImageIcon loadIcon() {
        Image image = null;
        try {
            image = AEProjectIcon.loadIcon(ProjectFiles.get().getProjectDir());
        } catch (IOException exception) {
            Log.error(exception.getMessage());
        }
        if (image == null) {
            return null;
        }
        
        return new ImageIcon(image.scale(16,16).getBufferedImage());
    }
    
    /** */
    public static AppIcon get() {
        if (instance == null) {
            instance = new AppIcon();
        }
        
        return instance;
    }
}