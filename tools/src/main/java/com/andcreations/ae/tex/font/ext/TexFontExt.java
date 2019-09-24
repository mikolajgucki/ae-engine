package com.andcreations.ae.tex.font.ext;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.image.Image;
import com.andcreations.ae.tex.font.TexFontListener;
import com.andcreations.ae.tex.font.gen.TexFontGenChar;
import com.andcreations.ae.tex.font.gen.TexFontGenResult;
import com.andcreations.io.UpToDate;
import com.andcreations.io.json.JSON;

/**
 * Extracts images from a font image.
 *
 * @author Mikolaj Gucki
 */
public class TexFontExt {
    /** */
    private TexFontExtCfg cfg;
    
    /** */
    private TexFontListener listener;
    
    /** */
    public TexFontExt(TexFontExtCfg cfg) {
        this.cfg = cfg;
    }
    
    /** */
    public void setTexFontListener(TexFontListener listener) {
        this.listener = listener;
    }    
    
    /** */
    private TexFontGenResult loadData() throws IOException  {
        return JSON.read(cfg.getDataFile(),TexFontGenResult.class);
    }
    
    /** */
    private Image loadImage() throws IOException {
        return Image.load(cfg.getImageFile());
    }
    
    /** */
    private File getDstFile(TexFontGenChar ch) {
        return new File(cfg.getDstDir(),ch.getSubtexture() + ".png");
    }
    
    /** */
    public boolean isUpToDate() throws IOException {
        TexFontGenResult data = loadData();
        
        List<File> dstFiles = new ArrayList<>();
    // for each character
        for (TexFontGenChar ch:data.getChars()) {
            dstFiles.add(getDstFile(ch));
        }
        
        File[] srcFiles = new File[]{cfg.getImageFile(),cfg.getDataFile()};
        return UpToDate.is(srcFiles,dstFiles);
    }
    
    /** */
    private void mkdirs(File newDir) {
        newDir.mkdirs();
        if (listener != null) {
            listener.texFontDirCreated(cfg.getImageFile(),newDir);
        }
    }    
        
    /** */
    private void fileCreated(File newFile) {
        if (listener != null) {
            listener.texFontFileCreated(cfg.getImageFile(),newFile);
        }
    }    
    
    /** */
    public void run() throws IOException {
        TexFontGenResult data = loadData();
        Image image = loadImage();
        
    // for each character
        for (TexFontGenChar ch:data.getChars()) {
            Image subimage = image.subimage(ch.getX(),ch.getY(),
                ch.getWidth(),ch.getHeight());
            
        // destination file
            File dstFile = getDstFile(ch);
            mkdirs(dstFile.getParentFile());
            
        // write image
            subimage.savePNG(dstFile);   
            fileCreated(dstFile);
        }
    }
}