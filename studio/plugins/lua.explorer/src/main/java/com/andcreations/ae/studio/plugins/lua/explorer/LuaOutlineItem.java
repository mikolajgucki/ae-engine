package com.andcreations.ae.studio.plugins.lua.explorer;

import javax.swing.ImageIcon;

import com.andcreations.ae.lua.parser.LuaElement;
import com.andcreations.ae.studio.plugins.outline.OutlineItem;

/**
 * @author Mikolaj Gucki
 */
class LuaOutlineItem extends OutlineItem implements Comparable<LuaOutlineItem> {
    /** */
    private LuaElement luaElement;
    
    /** */
    LuaOutlineItem(ImageIcon icon,String value,String htmlValue,
        LuaElement luaElement) {
    //
        super(icon,value,htmlValue);
        this.luaElement = luaElement;
    }
    
    /** */
    @Override
    public int compareTo(LuaOutlineItem that) {
        return this.luaElement.getBeginLine() - that.luaElement.getBeginLine();
    }
}