package com.andcreations.ae.studio.plugins.lua.autocompletion.luadoc;

import com.andcreations.ae.lua.doc.LuaDocVar;
import com.andcreations.ae.studio.plugins.lua.LuaIcons;
import com.andcreations.ae.studio.plugins.text.editor.autocomplete.VarAutocompl;
import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class LuaDocVarConverter {
    /** */
    private static final BundleResources res =
        new BundleResources(LuaDocVarConverter.class);
        
    /** */
    private static String dark;
    
    /** */
    static {
        init();
    }    
    
    /** */
    private static void init() {
        dark = UIColors.toHex(UIColors.dark());
    }        
    
    /** */
    static VarAutocompl convert(LuaDocVar luaDocVar) {
        String name = null;
        String prefix = "";
        
        if (luaDocVar.isLocal() == false) {
            String globalName = luaDocVar.getGlobalName();
            name = LuaDocCommon.getName(globalName);
            prefix = LuaDocCommon.getPrefix(globalName);
        }
        else {
            name = luaDocVar.getName();
        }
        
    // create
        VarAutocompl varAutocompl = new VarAutocompl(name,prefix);
        
    // values
        varAutocompl.setDefinedIn(luaDocVar.getDefinedIn());
        varAutocompl.setReplacementText(
            res.getStr("replacement.text",prefix,name));
        varAutocompl.setDisplayText(
            res.getStr("display.text",prefix,name,dark));
        varAutocompl.setDisplayHTML(
            res.getStr("display.html",prefix,name,dark));
        varAutocompl.setDefinitionString(
            res.getStr("definition.string",prefix,name,dark));
        varAutocompl.setIcon(Icons.getIcon(LuaIcons.LUA_ASSIGNMENT));   
        
    // description
        if (luaDocVar.getFull() != null) {
            varAutocompl.setDescription(luaDocVar.getFull());
        }
        else {
            varAutocompl.setDescription(luaDocVar.getBrief());
        }        
        
        return varAutocompl;
    }
}