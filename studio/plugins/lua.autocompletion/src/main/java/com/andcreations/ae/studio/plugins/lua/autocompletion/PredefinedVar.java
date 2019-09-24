package com.andcreations.ae.studio.plugins.lua.autocompletion;

import com.andcreations.ae.studio.plugins.lua.LuaIcons;
import com.andcreations.ae.studio.plugins.text.editor.autocomplete.VarAutocompl;
import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class PredefinedVar {
    /** */
    private static final BundleResources res =
        new BundleResources(PredefinedVar.class); 
    
    /** */
    private static String dark;
    
    /** */
    private String name;
    
    /** */
    private String prefix;
    
    /** */
    private String desc;
    
    /** */
    private String definedIn;
    
    /** */
    static {
        init();
    }
    
    /** */
    private static void init() {
        dark = UIColors.toHex(UIColors.dark());
    }    
    
    /** */
    String getName() {
        return name;
    }
    
    /** */
    void setName(String name) {
        this.name = name;
    }

    /** */
    String getPrefix() {
        return prefix;
    }
    
    /** */
    void setPrefix(String prefix) {
        this.prefix = prefix;
    }    
    
    /** */
    String getDesc() {
        return desc;
    }
    
    /** */
    void setDesc(String desc) {
        this.desc = desc;
    }
    
    /** */
    String getDefinedIn() {
        return definedIn;
    }
    
    /** */
    void setDefinedIn(String definedIn) {
        this.definedIn = definedIn;
    }  
    
    /** */
    VarAutocompl convert() {
    // prefix
        String prefix = getPrefix();
        if (prefix == null) {
            prefix = "";
        }        
        
        VarAutocompl varAutocompl = new VarAutocompl(getName(),prefix);
        varAutocompl.setDescription(getDesc());
        varAutocompl.setDefinedIn(getDefinedIn());
        varAutocompl.setReplacementText(res.getStr("replacement.text",
            prefix,getName()));
        varAutocompl.setDisplayText(res.getStr("display.text",
            prefix,getName(),dark));
        varAutocompl.setDisplayHTML(res.getStr("display.html",
            prefix,getName(),dark));
        varAutocompl.setDefinitionString(res.getStr("definition.string",
            prefix,getName(),dark));
        varAutocompl.setIcon(Icons.getIcon(LuaIcons.LUA_ASSIGNMENT));        
        
        return varAutocompl;
    }
}