package com.andcreations.ae.assets;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.andcreations.ae.issue.Issue;
import com.andcreations.ae.tex.pack.TexPacker;
import com.andcreations.ae.tex.pack.TexPackerCfg;
import com.andcreations.ae.tex.pack.TexPackerDataReader;
import com.andcreations.ae.tex.pack.TexPackerListener;
import com.andcreations.ae.tex.pack.data.TexPack;

/**
 * @author Mikolaj Gucki
 */
public class TexPackBuilder {
    /** */
    public static final String TEX_PACK_FILE_SUFFIX = ".texpack.json";
    
    /** */
    private static final String IMAGE_FILE_NAME = "%s.png";
    
    /** */
    public static final String LUA_FILE_NAME = "%s.lua";    
    
    /** */
    public static final String LUA_TEXTURES_PATH = "assets/textures";
    
    /** */
    private AssetsCfg assetsCfg;
    
    /** */
    private TexPackBuilderListener listener;
    
    /** */
    private TexPackerListener texPackerListener;
    
    /** */
    private Map<File,TexPacker> texPackers = new HashMap<>();
    
    /** */
    private Map<File,Long> lastModifiedMap = new HashMap<>();
    
    /** */
    public TexPackBuilder(AssetsCfg assetsCfg,List<File> texPackFiles,
        TexPackBuilderListener listener,TexPackerListener texPackerListener) {
    //
        this.assetsCfg = assetsCfg;
        this.listener = listener;
        this.texPackerListener = texPackerListener;
        create(texPackFiles);
    }    
    
    
    /** */
    public TexPackBuilder(AssetsCfg assetsCfg,List<File> texPackFiles,
        TexPackBuilderListener listener) {
    //
        this(assetsCfg,texPackFiles,listener,null);
    }      
    
    /** */
    private void create(List<File> texPackFiles) {
        for (File file:texPackFiles) {
            load(file);
        }        
    }
    
    /** */
    private File getLuaDir() {
        return new File(assetsCfg.getLuaBuildDir(),LUA_TEXTURES_PATH);
    }    
    
    /** */
    private void load(File file) {
        lastModifiedMap.put(file,file.lastModified());
        texPackers.put(file,null);        
            
    // notify
        listener.loadingTexPack(file);
        
    // read data
        TexPackerDataReader reader = new TexPackerDataReader();        
        TexPack data = null;
        try {
            data = reader.read(file);
        } catch (IOException exception) {
            listener.failedToLoadTexPack(file,exception);
            return;
        }
        
    // identifier
        String id = getId(file);
        
    // configuration
        TexPackerCfg cfg = new TexPackerCfg(id,data);
        
    // image file
        String imageFileName = String.format(IMAGE_FILE_NAME,cfg.getId());
        cfg.setImageFile(
            new File(assetsCfg.getTextureBuildDir(),imageFileName));
        
    // Lua
        String luaFileName = String.format(LUA_FILE_NAME,cfg.getId());
        cfg.setLuaFile(new File(getLuaDir(),luaFileName));        
        
    // create   
        TexPacker packer = new TexPacker(file,cfg);
        packer.setTexPackerListener(texPackerListener);
        List<Issue> issues = packer.getIssues();
        if (issues != null && issues.isEmpty() == false) {
            listener.failedToLoadTexPack(file,issues);
            return;
        }        
        texPackers.put(file,packer);
        
    // notify
        listener.loadedTexPack(file);
    }
    
    /** */
    private boolean isUpToDate(File file) {
        return file.lastModified() <= lastModifiedMap.get(file);
    }
    
    /** */
    public void buildTexPackFile(File file) {
    // notify
        listener.buildingTexPack(file);
        
        boolean upToDate = true;
    // check if up-to-date
        if (isUpToDate(file) == false) {
            upToDate = false;
        }
        load(file);
        
        TexPacker texPacker = texPackers.get(file);
    // could not create due to invalid data in file
        if (texPacker == null) {
            return;
        }
        if (texPacker.isUpToDate() == false) {
            upToDate = false;
        }
        
    // build if not up to date
        if (upToDate == false) {
            try {
                texPacker.run();
                listener.didBuildTexPack(file);
            } catch (Exception exception) {
                listener.failedToBuildTexPack(file,exception);
            }
        }
        else {
            listener.texPackUpToDate(file);
        }
    }
    
    /** */
    public void build() {
        for (Map.Entry<File,TexPacker> entry:texPackers.entrySet()) {
            File file = entry.getKey();
            buildTexPackFile(file);
        }
    }    
    
    /** */
    public static boolean isTexPackFile(File file) {
        return file.getName().endsWith(TEX_PACK_FILE_SUFFIX);
    }
    
    /** */
    public void addTexPackFile(File file) {
        load(file);
    }
    
    /** */
    public void removeTexPackFile(File file) {
        texPackers.remove(file);
        lastModifiedMap.remove(file);
    }
    
    /** */
    public List<File> getTexPackFiles() {
        List<File> files = new ArrayList<>();
        for (File file:texPackers.keySet()) {
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