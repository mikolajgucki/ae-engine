package com.andcreations.ae.studio.plugins.lua.classes;

import java.io.File;

import com.andcreations.ae.lua.hierarchy.LuaFileClassInfo;

/**
 * @author Mikolaj Gucki
 */
public class LuaClass {
    /** */
    private File file;
    
    /** */
    private String luaModule;
    
    /** */
    private LuaFileClassInfo luaClassInfo;
    
    /** */
    LuaClass(File file,String luaModule,LuaFileClassInfo luaClassInfo) {
        this.file = file;
        this.luaModule = luaModule;
        this.luaClassInfo = luaClassInfo;
    }
    
    /** */
    public File getFile() {
        return file;
    }
    
    /** */
    public String getPrefix() {
        if (luaModule == null) {
            return null;
        }
        
        int indexOf = luaModule.lastIndexOf('.');
        if (indexOf < 0) {
            return null;
        }
        
        return luaModule.substring(0,indexOf);        
    }
    
    /** */
    public String getName() {
        if (luaModule == null) {
            return null;
        }
        
        int indexOf = luaModule.lastIndexOf('.');
        if (indexOf < 0) {
            return luaModule;
        }
        
        return luaModule.substring(indexOf + 1,luaModule.length());
    }
    
    /** */
    public String getLuaModule() {
        return luaModule;
    }
    
    /** */
    public String getSuperclassName() {
        return luaClassInfo.getSuperclassName();
    }
    
    /** */
    public LuaFileClassInfo getLuaClassInfo() {
        return luaClassInfo;
    }
}