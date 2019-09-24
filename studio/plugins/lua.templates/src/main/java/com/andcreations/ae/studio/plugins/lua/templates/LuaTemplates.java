package com.andcreations.ae.studio.plugins.lua.templates;

import java.io.IOException;

import com.andcreations.ae.studio.plugins.lua.templates.resources.R;
import com.andcreations.resources.ResourceLoader;

/**
 * @author Mikolaj Gucki
 */
class LuaTemplates {
    /** */
    private static LuaTemplates instance;
    
    /** */
    void init() {
    // insert function
        InsertFuncTemplate insertFunc = new InsertFuncTemplate();
        insertFunc.init();        
        
    // create Lua class
        CreateLuaClassTemplate createLuaClass = new CreateLuaClassTemplate();
        createLuaClass.init();       
    }
    
    /** */
    public String loadTemplate(String name) throws IOException {
        return ResourceLoader.loadAsString(R.class,name);
    }
    
    /** */
    static LuaTemplates get() {
        if (instance == null) {
            instance = new LuaTemplates();
        }
        
        return instance;
    }
}