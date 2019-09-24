package com.andcreations.ae.studio.plugins.lua.explorer.search;

import com.andcreations.ae.studio.plugins.search.Search;

/**
 * @author Mikolaj Gucki
 */
public class LuaSearch {
    /** */
    private static LuaSearch instance;
    
    /** */
    public void init() {
        Search.get().addSearchSource(new LuaSearchSource());
    }
    
    /** */
    public static LuaSearch get() {
        if (instance == null) {
            instance = new LuaSearch();
        }
        
        return instance;
    }
}