package com.andcreations.ae.appicon;

import java.io.File;
import java.io.IOException;

import com.andcreations.ae.image.Image;

/**
 * @author Mikolaj Gucki
 */
public class AppIcon {
    /** */
    private AppIconEntryList list;
    
    /** */
    public AppIcon(AppIconEntryList list) {
        this.list = list;
    }
    
    /** */
    private void generate(Image srcImage,File dstDir,AppIconEntry entry)
        throws IOException {
    // sclae
        Image icon = srcImage.scale(entry.getWidth(),entry.getHeight());
        
    // make directories
        File dstFile = new File(dstDir,entry.getFilename() + ".png");
        dstFile.getParentFile().mkdirs();
        
    // save
        icon.savePNG(dstFile);
    }
    
    /** */
    public void generate(String srcFile,String dstDir) throws IOException {
        Image srcImage = Image.load(new File(srcFile));
        
        for (AppIconEntry entry:list.getEntries()) {
            generate(srcImage,new File(dstDir,list.getDir()),entry);
        }
    }
    
    /** */
    public Image createByFilename(Image srcImage,String filename) {
        filename = filename.replace('\\','/');
        for (AppIconEntry entry:list.getEntries()) {
            if (entry.getFilename().equals(filename) == true) {
                return srcImage.scale(entry.getWidth(),entry.getHeight());
            }
        }
        
        return null;
    }
    
    /** */
    private static AppIcon load(String name) throws IOException {
        AppIconEntryList list = AppIconEntryList.load(name);
        return new AppIcon(list);
    }
    
    /** */
    public static AppIcon ios() throws IOException {
        return load("ios.json");
    }
    
    /** */
    public static AppIcon android() throws IOException {
        return load("android.json");
    }
}