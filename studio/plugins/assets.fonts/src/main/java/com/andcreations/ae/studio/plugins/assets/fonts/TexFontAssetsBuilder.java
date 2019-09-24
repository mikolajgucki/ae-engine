package com.andcreations.ae.studio.plugins.assets.fonts;

import com.andcreations.ae.assets.AssetsCfg;
import com.andcreations.ae.assets.LuaModulesCodeGenListener;
import com.andcreations.ae.assets.TexFontBuilder;
import com.andcreations.ae.assets.TexFontLuaModulesCodeGen;
import com.andcreations.ae.studio.plugins.assets.AbstractAssetsBuilder;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class TexFontAssetsBuilder extends AbstractAssetsBuilder {
    /** */
    public static final String ID = "fonts";
    
    /** */
    private static final BundleResources res =
        new BundleResources(TexFontAssetsBuilder.class);
        
    /** */
    private TexFontBuilder texFontBuilder;
    
    /** */
    private AssetsCfg cfg;
    
    /** */
    private LuaModulesCodeGenListener luaModulesCodeGenListener;     
        
    /** */
    TexFontAssetsBuilder(TexFontBuilder texFontBuilder,
        AssetsCfg cfg,LuaModulesCodeGenListener luaModulesCodeGenListener) {
    //
        super(ID,res.getStr("name"),Icons.getIcon(FontsIcons.FONT),
            res.getStr("desc"));
        this.texFontBuilder = texFontBuilder;
        this.cfg = cfg;
        this.luaModulesCodeGenListener = luaModulesCodeGenListener;        
    }
    
    
    /** */
    @Override
    public boolean hasWarnings() {
        return TexFont.get().hasWarnings();
    }
    
    /** */
    @Override
    public boolean hasErrors() {
        return TexFont.get().hasErrors();
    }    
    
    /** */
    @Override
    public void build() {
    // fonts
        texFontBuilder.build();
        
    // modules
        TexFontLuaModulesCodeGen codeGen = new TexFontLuaModulesCodeGen(
            cfg,texFontBuilder.getTexFontFiles(),luaModulesCodeGenListener);
        codeGen.generate();    
    }
}