package com.andcreations.ae.desktop;

import java.util.Collections;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class LuaTraceback {
    /** */
    private List<LuaTracebackItem> items;
    
    /** */
    LuaTraceback(List<LuaTracebackItem> items) {
        this.items = Collections.unmodifiableList(items);
    }
    
    /** */
    public List<LuaTracebackItem> getItems() {
        return items;
    }
}