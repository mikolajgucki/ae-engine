package com.andcreations.ae.assets;

import java.io.File;

/**
 * @author Mikolaj Gucki
 */
public class AssetsCfg {
    /** */
    private File fontResourcesDir;
    
    /** */
    private File fontBuildDir;
    
    /** */
    private File textureResourcesDir;
    
    /** */
    private File textureBuildDir;
    
    /** */
    private File luaBuildDir;
    
    /** */
    public void setFontResourcesDir(File fontResourcesDir) {
        this.fontResourcesDir = fontResourcesDir;
    }
    
    /** */
    public File getFontResourcesDir() {
        return fontResourcesDir;
    }
    
    /** */
    public void setFontBuildDir(File fontBuildDir) {
        this.fontBuildDir = fontBuildDir;
    }
    
    /** */
    public File getFontBuildDir() {
        return fontBuildDir;
    }
    
    /** */
    public void setTextureResourcesDir(File textureResourcesDir) {
        this.textureResourcesDir = textureResourcesDir;
    }
    
    /** */
    public File getTextureResourcesDir() {
        return textureResourcesDir;
    }
    
    /** */
    public void setTextureBuildDir(File textureBuildDir) {
        this.textureBuildDir = textureBuildDir;
    }
    
    /** */
    public File getTextureBuildDir() {
        return textureBuildDir;
    }
    
    /** */
    public void setLuaBuildDir(File luaBuildDir) {
        this.luaBuildDir = luaBuildDir;
    }
        
    /** */
    public File getLuaBuildDir() {
        return luaBuildDir;
    }
    
    /** */
    public void mkdirs() {
        fontBuildDir.mkdirs();
        textureBuildDir.mkdirs();
        luaBuildDir.mkdirs();
    }
}