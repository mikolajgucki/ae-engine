package com.andcreations.ae.assets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class TexFontLuaModulesCodeGen {
    /** */
    private AssetsCfg cfg;
    
    /** */
    private List<File> texFontFiles;
    
    /** */
    private LuaModulesCodeGenListener listener;
    
    /** */
    public TexFontLuaModulesCodeGen(AssetsCfg cfg,List<File> texFontFiles,
        LuaModulesCodeGenListener listener) {
    //
        this.cfg = cfg;
        this.texFontFiles = texFontFiles;
        this.listener = listener;
    }    
    
    /** */
    public void generate() {
    // modules
        List<String> modules = new ArrayList<>();
        for (File texFontFile:texFontFiles) {
            modules.add("assets.fonts." + TexFontBuilder.getId(texFontFile));
        }
        
        File dstFile = new File(cfg.getLuaBuildDir(),
            "assets/fonts/modules.lua");
    // generate
        LuaModulesCodeGen gen = new LuaModulesCodeGen();
        gen.write("fonts",modules,"Provides modules with fonts.",
            listener,dstFile);
    }    
}