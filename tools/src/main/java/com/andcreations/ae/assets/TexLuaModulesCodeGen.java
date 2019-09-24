package com.andcreations.ae.assets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class TexLuaModulesCodeGen {
    /** */
    private AssetsCfg cfg;
    
    /** */
    private List<File> texPackFiles;
    
    /** */
    private LuaModulesCodeGenListener listener;
    
    /** */
    public TexLuaModulesCodeGen(AssetsCfg cfg,List<File> texPackFiles,
        LuaModulesCodeGenListener listener) {
    //
        this.cfg = cfg;
        this.texPackFiles = texPackFiles;
        this.listener = listener;
    }
    
    /** */
    public void generate() {
    // modules
        List<String> modules = new ArrayList<>();
        for (File texPackFile:texPackFiles) {
            modules.add("assets.textures." + TexPackBuilder.getId(texPackFile));
        }
        
        File dstFile = new File(cfg.getLuaBuildDir(),
            "assets/textures/modules.lua");
    // generate
        LuaModulesCodeGen gen = new LuaModulesCodeGen();
        gen.write("textures",modules,"Provides modules with textures.",
            listener,dstFile);
    }
}