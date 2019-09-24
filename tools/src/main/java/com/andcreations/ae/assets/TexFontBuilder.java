package com.andcreations.ae.assets;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.andcreations.ae.issue.Issue;
import com.andcreations.ae.tex.font.TexFont;
import com.andcreations.ae.tex.font.TexFontCfg;
import com.andcreations.ae.tex.font.TexFontDataReader;
import com.andcreations.ae.tex.font.TexFontListener;
import com.andcreations.ae.tex.font.data.TexFontData;
import com.andcreations.ae.tex.font.ext.TexFontExt;
import com.andcreations.ae.tex.font.ext.TexFontExtCfg;

/**
 * @author Mikolaj Gucki
 */
public class TexFontBuilder {
    /** */
    public static final String TEX_FONT_FILE_SUFFIX = ".texfont.json";
    
    /** */
    public static final String RAW_FILE_NAME = "%s.raw.png";
    
    /** */
    public static final String MASK_FILE_NAME = "%s.mask.png";
    
    /** */
    public static final String LUA_FILE_NAME = "%s.lua";

    /** */
    public static final String DATA_FILE_NAME = "%s.texfont.data.json";

    /** */
    public static final String IMAGE_FILE_NAME = "%s.png";    
    
    /** */
    public static final String LUA_FONTS_PATH = "assets/fonts";
    
    /** */
    public static final String EXT_DST_PATH = "subtextures";
    
    /** */
    private AssetsCfg assetsCfg;
    
    /** */
    private TexFontBuilderListener listener;
    
    /** */
    private TexFontListener texFontListener;
    
    /** */
    private Map<File,TexFont> texFonts = new HashMap<>();
    
    /** */
    private Map<File,TexFontExt> texFontExts = new HashMap<>();
    
    /** */
    private Map<File,Long> lastModifiedMap = new HashMap<>();    
    
    /** */
    public TexFontBuilder(AssetsCfg assetsCfg,List<File> texFontFiles,
        TexFontBuilderListener listener,TexFontListener texFontListener) {
    //
        this.assetsCfg = assetsCfg;
        this.listener = listener;
        this.texFontListener = texFontListener;
        create(texFontFiles);
    }
    
    /** */
    public TexFontBuilder(AssetsCfg assetsCfg,List<File> texFontFiles,
        TexFontBuilderListener listener) {
    //
        this(assetsCfg,texFontFiles,listener,null);
    }    
    
    /** */
    private void create(List<File> texFontFiles) {
        for (File file:texFontFiles) {
            load(file);
        }
    }
    
    /** */
    private File getBuildDir(TexFontCfg cfg) {
        return new File(assetsCfg.getFontBuildDir(),cfg.getId());
    }
    
    /** */
    private File getLuaDir() {
        return new File(assetsCfg.getLuaBuildDir(),LUA_FONTS_PATH);
    }

    /** */
    private void load(File file) {
        lastModifiedMap.put(file,file.lastModified());
        texFonts.put(file,null);        
        
    // notify
        listener.loadingTexFont(file);        
        
    // identifier
        String id = getId(file);
        
    // load
        TexFontDataReader dataReader = new TexFontDataReader();
        TexFontData data;
        try {
            data = dataReader.read(file);
        } catch (IOException exception) {
            listener.failedToLoadTexFont(file,exception);
            return;
        }
        TexFontCfg texFontCfg = new TexFontCfg(id,data);
        
    // create
        TexFont texFont = new TexFont(file,texFontCfg);
        texFont.setTexFontListener(texFontListener);
        List<Issue> issues = texFont.getIssues();
        if (issues != null && issues.isEmpty() == false) {
            listener.failedToLoadTexFont(file,issues);
            return;
        }
        texFonts.put(file,texFont);
        
        File buildDir = getBuildDir(texFontCfg);        
    // raw file
        String rawFileName = String.format(RAW_FILE_NAME,texFontCfg.getId());
        texFontCfg.setRawFile(new File(buildDir,rawFileName));

    // mask file
        String maskFileName = String.format(MASK_FILE_NAME,texFontCfg.getId());
        texFontCfg.setMaskFile(new File(buildDir,maskFileName));
            
    // Lua file
        String luaFileName = String.format(LUA_FILE_NAME,texFontCfg.getId());
        texFontCfg.setLuaFile(new File(getLuaDir(),luaFileName));
        
    // data file
        String dataFileName = String.format(DATA_FILE_NAME,texFontCfg.getId());
        File dataFile = new File(buildDir,dataFileName);
        texFontCfg.setDataFile(dataFile);
        
    // image file
        String imageFileName = String.format(IMAGE_FILE_NAME,
            texFontCfg.getId());
        File imageFile = new File(file.getParentFile(),imageFileName);
        texFontCfg.setImageFile(imageFile);
        
    // extractor configuration
        File extDstDir = new File(buildDir,EXT_DST_PATH);
        TexFontExtCfg texFontExtCfg = new TexFontExtCfg(
            imageFile,dataFile,extDstDir);
        
    // exractor
        TexFontExt texFontExt = new TexFontExt(texFontExtCfg);
        texFontExt.setTexFontListener(texFontListener);
        texFontExts.put(file,texFontExt);
        
    // notify
        listener.loadedTexFont(file);
    }
    
    /** */
    private boolean isUpToDate(File file) {
        return file.lastModified() <= lastModifiedMap.get(file);
    }    
    
    /** */
    public void buildTexFontFile(File file) {
    // notify
        listener.buildingTexFont(file);   
        
        boolean upToDate = true;
    // check if up-to-date
        if (isUpToDate(file) == false) {
            upToDate = false;
        }            
        load(file);
        
        TexFont texFont = texFonts.get(file);
    // could not create due to invalid data in file            
        if (texFont == null) {
            return;
        }
        if (texFont.isUpToDate() == false) {
            upToDate = false;
        }
            
    // build if not up to date
        if (upToDate == false) {                
            try {
                texFont.run();
                listener.didBuildTexFont(file);
            } catch (Exception exception) {
                listener.failedToBuildTexFont(file,exception);
                return;
            }
        }
        else {
            listener.texFontUpToDate(file);
        }
        
    // extract
        if (texFontExts.get(file) != null) {
            TexFontExt texFontExt = texFontExts.get(file);            
            try {
                if (texFontExt.isUpToDate() == false) {
                    texFontExt.run();
                    listener.didExtTexFont(file);
                }
                else {
                    listener.texFontExtUpToDate(file);
                }
            } catch (Exception exception) {
                listener.failedToExtTexFont(file,exception);
                return;
            }            
        }
    }
    
    /** */
    public void build() {
    // build fonts
        for (Map.Entry<File,TexFont> entry:texFonts.entrySet()) {
            File file = entry.getKey();
            buildTexFontFile(file);
        }
    }    
    
    /** */
    public static boolean isTexFontFile(File file) {
        return file.getName().endsWith(TEX_FONT_FILE_SUFFIX);
    }
    
    /** */
    public void addTexFontFile(File file) {
        load(file);
    }
    
    /** */
    public void removeTexFontFile(File file) {
        texFonts.remove(file);
        texFontExts.remove(file);
        lastModifiedMap.remove(file);
    }    
    
    /** */
    public List<File> getTexFontFiles() {
        List<File> files = new ArrayList<>();
        for (File file:texFonts.keySet()) {
            files.add(file);
        }
        
        return files;
    }
    
    /** */
    static String getId(File file) {
        String name = file.getName();
        int indexOf = name.indexOf('.');
        return name.substring(0,indexOf);
    }    
}