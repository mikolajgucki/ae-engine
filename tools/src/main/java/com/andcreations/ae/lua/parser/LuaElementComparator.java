package com.andcreations.ae.lua.parser;

import java.util.Comparator;

/**
 * @author Mikolaj Gucki
 */
class LuaElementComparator implements Comparator<LuaElement> {
    /** */
    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }
    
    /** */
    @Override
    public int compare(LuaElement a,LuaElement b) {
        return a.getBeginLine() - b.getBeginLine();
    }
}