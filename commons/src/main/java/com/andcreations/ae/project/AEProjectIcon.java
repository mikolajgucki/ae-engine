package com.andcreations.ae.project;

import java.io.File;
import java.io.IOException;

import com.andcreations.ae.image.Image;

/**
 * @author Mikolaj Gucki
 */
public class AEProjectIcon {
    /** The path to the directory with icons. */
    public static final String ICONS_PATH = "resources/icon";
    
    /** The path to the icon file. */
    public static final String ICON_PATH = ICONS_PATH + "/icon.png";
    
    /** The path to the Android icon file. */
    public static final String ANDROID_ICON_PATH =
        ICONS_PATH + "/icon_android.png";
    
    /** The path to the iOS icon file. */
    public static final String IOS_ICON_PATH = ICONS_PATH + "/icon_ios.png";
    
    /** */
    public static File getIconFile(File projectDir) {
        File file = new File(projectDir,ICON_PATH);
        if (file.exists() == true && file.isFile() == true) {
            return file;
        }
        return null;
    }
    
    /** */
    public static File getAndroidIconFile(File projectDir) {
        File file = new File(projectDir,ANDROID_ICON_PATH);
        if (file.exists() == true && file.isFile() == true) {
            return file;
        }
        
        return getIconFile(projectDir);
    }
    
    /** */
    public static File getiOSIconFile(File projectDir) {
        File file = new File(projectDir,IOS_ICON_PATH);
        if (file.exists() == true && file.isFile() == true) {
            return file;
        }
        
        return getIconFile(projectDir);
    }    
    
    /** */
    public static Image loadIcon(File projectDir) throws IOException {
        return Image.load(getIconFile(projectDir));
    }
    
    /** */
    public static Image loadAndroidIcon(File projectDir) throws IOException {
        return Image.load(getAndroidIconFile(projectDir));
    }
    
    /** */
    public static Image loadiOSIcon(File projectDir) throws IOException {
        return Image.load(getiOSIconFile(projectDir));
    }        
}