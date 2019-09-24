package com.andcreations.ae.assets;

/**
 * @author Mikolaj Gucki
 */
public class AssetsBuilderCfg {
    /** */
    private TexFontBuilderListener texFontBuilderListener;
    
    /** */
    private TexPackBuilderListener texPackBuilderListener;
    
    /** */
    private LuaModulesCodeGenListener luaModulesCodeGenListener;
    
    /** */
    public AssetsBuilderCfg() {
    }
    
    /** */
    public AssetsBuilderCfg(TexFontBuilderListener texFontBuilderListener,
        TexPackBuilderListener texPackBuilderListener,
        LuaModulesCodeGenListener luaModulesCodeGenListener) {
    //
        this.texFontBuilderListener = texFontBuilderListener;
        this.texPackBuilderListener = texPackBuilderListener;
        this.luaModulesCodeGenListener = luaModulesCodeGenListener;
    }
    
    /** */
    public TexFontBuilderListener getTexFontBuilderListener() {
        return texFontBuilderListener;
    }
    
    /** */
    public void setTexFontBuilderListener(
        TexFontBuilderListener texFontBuilderListener) {
    //
        this.texFontBuilderListener = texFontBuilderListener;
    }
    
    /** */
    public TexPackBuilderListener getTexPackBuilderListener() {
        return texPackBuilderListener;
    }
    
    /** */
    public void setTexPackBuilderListener(
        TexPackBuilderListener texPackBuilderListener) {
    //
        this.texPackBuilderListener = texPackBuilderListener;
    }
    
    /** */
    public LuaModulesCodeGenListener getLuaModulesCodeGenListener() {
        return luaModulesCodeGenListener;
    }
    
    /** */
    public void setLuaModulesCodeGenListener(
        LuaModulesCodeGenListener luaModulesCodeGenListener) {
    //
        this.luaModulesCodeGenListener = luaModulesCodeGenListener;
    }
}