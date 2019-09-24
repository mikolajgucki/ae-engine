package com.andcreations.ae.lua.doc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
class LuaDocCommentBlock {
    /** */
    private int beginLine;
    
    /** */
    private List<LuaDocTag> tags;
    
    /** */
    LuaDocCommentBlock(int beginLine,List<LuaDocTag> tags) {
        this.beginLine = beginLine;
        this.tags = tags;
    }
    
    /** */
    LuaDocCommentBlock(int beginLine) {
        this(beginLine,new ArrayList<LuaDocTag>());
    }
    
    /** */
    void addTag(LuaDocTag tag) {
        tags.add(tag);
    }
    
    /** */
    int getBeginLine() {
        return beginLine;
    }
    
    /** */
    List<LuaDocTag> getTags() {
        return Collections.unmodifiableList(tags);
    }
    
    /** */
    LuaDocTag getTag(String name) {
        LuaDocTag found = null;
        for (LuaDocTag tag:tags) {
            if (tag.getName().equals(name) == true) {
                if (found != null) {
                    throw new IllegalStateException(
                        "Attempt to get duplicated tag " + name);
                }
                found = tag;
            }
        }
        return found;
    }
    
    /** */
    boolean hasTag(String name) {
        for (LuaDocTag tag:tags) {
            if (tag.getName().equals(name) == true) {
                return true;
            }
        }
        return false;
    }
    
    /** */
    int getTagIndex(String name) {
        for (int index = 0; index < tags.size(); index++) {
            if (tags.get(index).matchName(name) == true) {
                return index;
            }
        }
        
        return -1;
    }
    
    /** */
    boolean isEmpty() {
        return tags.isEmpty();
    }
    
    /** */
    String getTagValue(String name) {
        LuaDocTag tag = getTag(name);
        if (tag == null) {
            return null;
        }
        return tag.getValue();
    }
}