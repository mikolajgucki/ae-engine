package com.andcreations.ae.studio.plugins.lua.autocompletion.luadoc;

/** */
class LuaDocCommon {
    /** */
    static String getPrefix(String globalName) {
        if (globalName != null) {
            int indexOf = globalName.lastIndexOf(":");
            if (indexOf != -1) {
                return globalName.substring(0,indexOf + 1);
            }
            indexOf = globalName.lastIndexOf(".");
            if (indexOf != -1) {
                return globalName.substring(0,indexOf + 1);
            }
        }
        
        return "";
    }
    
    /** */
    static String getName(String globalName) {
        if (globalName != null) {
            int indexOf = globalName.lastIndexOf(":");
            if (indexOf != -1) {
                return globalName.substring(indexOf + 1,globalName.length());
            }
            indexOf = globalName.lastIndexOf(".");
            if (indexOf != -1) {
                return globalName.substring(indexOf + 1,globalName.length());
            }
        }
        
        return globalName;
    }    
}