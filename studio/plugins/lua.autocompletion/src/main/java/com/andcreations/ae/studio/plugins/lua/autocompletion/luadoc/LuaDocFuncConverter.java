package com.andcreations.ae.studio.plugins.lua.autocompletion.luadoc;

import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.lua.doc.LuaDocFunc;
import com.andcreations.ae.lua.doc.LuaDocFuncParam;
import com.andcreations.ae.lua.doc.LuaDocFuncVariant;
import com.andcreations.ae.studio.plugins.lua.LuaIcons;
import com.andcreations.ae.studio.plugins.text.editor.autocomplete.FuncAutocompl;
import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class LuaDocFuncConverter {
    /** */
    private static final BundleResources res =
        new BundleResources(LuaDocFuncConverter.class); 
        
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
    private static String getArgs(List<LuaDocFuncParam> params) {
        String args = "";
        
        if (params != null) {
            for (LuaDocFuncParam param:params) {
                if (args.length() > 0) {
                    args += ",";
                }
                args += param.getName();
            }
        }
        
        return args;
    }    
    
    /** */
    static List<FuncAutocompl> convert(LuaDocFunc luaDocFunc) {
        List<FuncAutocompl> funcAutocomplList = new ArrayList<>();        
                
        String name = null;
        String prefix = "";
        
        if (luaDocFunc.isLocal() == false) {
            String globalName = luaDocFunc.getGlobalName();
            name = LuaDocCommon.getName(globalName);
            prefix = LuaDocCommon.getPrefix(globalName);
        }
        else {
            name = luaDocFunc.getName();
        }
        
    // for each variant
        for (LuaDocFuncVariant variant:luaDocFunc.getVariants()) {
            FuncAutocompl funcAutocompl = new FuncAutocompl(name,prefix);
            funcAutocomplList.add(funcAutocompl);
            
        // parameters string
            String args = getArgs(variant.getParams());
            
        // values
            funcAutocompl.setIcon(Icons.getIcon(LuaIcons.LUA_FUNC));
            funcAutocompl.setDefinedIn(luaDocFunc.getDefinedIn());
            funcAutocompl.setDisplayText(res.getStr("display.text",
                prefix,name,args));
            funcAutocompl.setDisplayHTML(res.getStr("display.html",
                prefix,name,args,dark));
            funcAutocompl.setDefinitionString(res.getStr("definition.string",
                prefix,name,args));

        // description
            if (variant.getFull() != null) {
                funcAutocompl.setDescription(variant.getFull());
            }
            else {
                funcAutocompl.setDescription(variant.getBrief());
            }
            
        // replacement text
            String replacementText = null;
            if (luaDocFunc.isMethod() == false) {
                replacementText = luaDocFunc.getGlobalName();
            }
            else {
                replacementText = luaDocFunc.getLocalName();
            }            
            if (replacementText == null) {
                replacementText = name;
            }
            funcAutocompl.setReplacementText(replacementText);
            
        // parameters
            for (LuaDocFuncParam param:variant.getParams()) {
                funcAutocompl.addParam(param.getName(),param.getDesc());
            }
        }
        
        return funcAutocomplList;
    }
}