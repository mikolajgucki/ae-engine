package com.andcreations.ae.assets;

import java.io.File;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class AssetsBuilder {
    /** */
    private AssetsCfg assetsCfg;
    
    /** */
    private AssetsBuilderCfg builderCfg;
    
    /** */
    public AssetsBuilder(AssetsCfg assetsCfg,AssetsBuilderCfg builderCfg) {
        this.assetsCfg = assetsCfg;
        this.builderCfg = builderCfg;
    }
    
    /** */
    private TexFontBuilder createTexFontBuilder() {
        List<File> texFontFiles = AssetsUtil.findFiles(
            assetsCfg.getFontResourcesDir(),
            TexFontBuilder.TEX_FONT_FILE_SUFFIX);
        TexFontBuilder texFontBuilder = new TexFontBuilder(
            assetsCfg,texFontFiles,builderCfg.getTexFontBuilderListener());
        return texFontBuilder;
    }
    
    /** */
    private TexLuaModulesCodeGen createTexLuaModulesCodeGen(
        List<File> texPackFiles) {
    //
        TexLuaModulesCodeGen codeGen = new TexLuaModulesCodeGen(assetsCfg,
            texPackFiles,builderCfg.getLuaModulesCodeGenListener());
        return codeGen;
    }
    
    /** */
    private TexPackBuilder createTexPackBuilder() {
        List<File> texPackFiles = AssetsUtil.findFiles(
            assetsCfg.getTextureResourcesDir(),
            TexPackBuilder.TEX_PACK_FILE_SUFFIX);
        TexPackBuilder texPackBuilder = new TexPackBuilder(
            assetsCfg,texPackFiles,builderCfg.getTexPackBuilderListener());
        return texPackBuilder;
    }
    
    /** */
    private TexFontLuaModulesCodeGen createTexFontLuaModulesCodeGen(
        List<File> texFontFiles) {
    //
        TexFontLuaModulesCodeGen codeGen = new TexFontLuaModulesCodeGen(
            assetsCfg,texFontFiles,builderCfg.getLuaModulesCodeGenListener());
        return codeGen;        
    }
    
    /** */
    public void build() {
        TexFontBuilder texFontBuilder = createTexFontBuilder();
        texFontBuilder.build();
        
        TexFontLuaModulesCodeGen texFontLuaModulesCodeGen =
            createTexFontLuaModulesCodeGen(texFontBuilder.getTexFontFiles());
        texFontLuaModulesCodeGen.generate();
        
        TexPackBuilder texPackBuilder = createTexPackBuilder();
        texPackBuilder.build();
        
        TexLuaModulesCodeGen texLuaModulesCodeGen = 
            createTexLuaModulesCodeGen(texPackBuilder.getTexPackFiles());
        texLuaModulesCodeGen.generate();
        
        
    }
}