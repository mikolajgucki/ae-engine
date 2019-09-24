package com.andcreations.ae.studio.plugins.simulator;

import com.andcreations.ae.desktop.LuaValue;
import com.andcreations.ae.desktop.LuaValueUtil;
import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class SimulatorLuaValue {
    /** */
    private static SimulatorLuaValue instance;
    
    /** */
    private BundleResources res = new BundleResources(SimulatorLuaValue.class);

    /** */ 
    private String dark;        
     
    /** */
    private SimulatorLuaValue() {
        create();
    }
    
    /** */
    private void create() {
        dark = UIColors.toHex(UIColors.dark());
    }
    
    /** */
    private String toStr(LuaValue luaValue,boolean withScope,
        String resKeySuffix,SimulatorGlobals globals) {
    // name
        String name = luaValue.getName();
    
    // global
        LuaValue global = null;
        if (globals != null) {
            global = globals.findByPointer(luaValue.getPointer());
            if (global != null) {
                name = res.getStr("global.name." + resKeySuffix,
                    name,global.getName(),dark);
            }
        }
    
    // type
        String luaValueType = res.getStr(String.format("value.type.%s",
            luaValue.getType()));
        
    // scope
        String luaValueScope = "";
        if (withScope == true) {
            luaValueScope = res.getStr(String.format("value.scope.%s",
                luaValue.getScope()));
        }
        
    // value
        String value = luaValue.getValue();
        if (luaValue.getType() == LuaValue.Type.NUMBER) {
            value = LuaValueUtil.formatNumber(value);
        }

    // resource key
        String resKey = "value.";
        if (luaValue.getType() == LuaValue.Type.TABLE) {
            resKey = "table.value.";
        }
        else if (luaValue.getType() == LuaValue.Type.FUNCTION) {
            resKey = "function.value.";
        }
        else if (luaValue.getType() == LuaValue.Type.USER_DATA) {
            resKey = "userdata.value.";
        }        
        resKey += resKeySuffix;
        
    // get the string
        String str = res.getStr(resKey,name,value,luaValueType,luaValueScope,
            luaValue.getPointer(),dark);
        return str;
    }
    
    /** */
    String toText(LuaValue luaValue,boolean withScope,
        SimulatorGlobals globals) {
    //
        return toStr(luaValue,withScope,"text",globals);
    }
    
    /** */
    String toText(LuaValue luaValue,boolean withScope) {
        return toText(luaValue,withScope,null);
    }
    
    /** */
    String toHTML(LuaValue luaValue,boolean withScope,
        SimulatorGlobals globals) {
    //
        return toStr(luaValue,withScope,"html",globals);
    }
    
    /** */
    String toHTML(LuaValue luaValue,boolean withScope) {
        return toHTML(luaValue,withScope,null);
    }
    
    /** */
    static SimulatorLuaValue get() {
        if (instance == null) {
            instance = new SimulatorLuaValue();
        }
        
        return instance;
    }
}