package com.andcreations.ae.assets;

import java.io.File;

/**
 * @author Mikolaj Gucki
 */
public class DefaultAssetsCfg {
    /** */
    public static final String FONT_RESOURCES_PATH = "resources/fonts";
    
    /** */
    public static final String FONT_BUILD_PATH = "build/fonts";
    
    /** */
    public static final String TEXTURE_RESOURCES_PATH = "resources/textures";
    
    /** */
    public static final String TEXTURE_BUILD_PATH = "build/assets/textures";
    
    /** */
    public static final String LUA_BUILD_PATH = "build/assets/lua";
    
    /** */
    public static AssetsCfg create(File root) {
        AssetsCfg cfg = new AssetsCfg();
        
    // fonts
        cfg.setFontResourcesDir(new File(root,FONT_RESOURCES_PATH));
        cfg.setFontBuildDir(new File(root,FONT_BUILD_PATH));
        
    // textures
        cfg.setTextureResourcesDir(new File(root,TEXTURE_RESOURCES_PATH));
        cfg.setTextureBuildDir(new File(root,TEXTURE_BUILD_PATH));    
    
    // Lua
        cfg.setLuaBuildDir(new File(root,LUA_BUILD_PATH));
        
        return cfg;
    }
}