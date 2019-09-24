package com.andcreations.ae.studio.plugins.assets.textures;

import com.andcreations.ae.assets.AssetsCfg;
import com.andcreations.ae.assets.LuaModulesCodeGenListener;
import com.andcreations.ae.assets.TexLuaModulesCodeGen;
import com.andcreations.ae.assets.TexPackBuilder;
import com.andcreations.ae.studio.plugins.assets.AbstractAssetsBuilder;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class TexPackAssetsBuilder extends AbstractAssetsBuilder {
    /** */
    public static final String ID = "textures";
    
    /** */
    private static final BundleResources res =
        new BundleResources(TexPackAssetsBuilder.class);     
        
    /** */
    private TexPackBuilder texPackBuilder;
    
    /** */
    private AssetsCfg cfg;
    
    /** */
    private LuaModulesCodeGenListener luaModulesCodeGenListener;    
        
    /** */
    TexPackAssetsBuilder(TexPackBuilder texPackBuilder,
        AssetsCfg cfg,LuaModulesCodeGenListener luaModulesCodeGenListener) {
    //
        super(ID,res.getStr("name"),Icons.getIcon(TexturesIcons.TEXTURE),
            res.getStr("desc"));
        this.texPackBuilder = texPackBuilder;
        this.cfg = cfg;
        this.luaModulesCodeGenListener = luaModulesCodeGenListener;
    }
    
    /** */
    @Override
    public boolean hasWarnings() {
        return TexPack.get().hasWarnings();
    }
    
    /** */
    @Override
    public boolean hasErrors() {
        return TexPack.get().hasErrors();
    }
    
    /** */
    @Override
    public void build() {
    // textures
        texPackBuilder.build();
        
    // modules
        TexLuaModulesCodeGen codeGen = new TexLuaModulesCodeGen(
            cfg,texPackBuilder.getTexPackFiles(),luaModulesCodeGenListener);
        codeGen.generate();
    }
}