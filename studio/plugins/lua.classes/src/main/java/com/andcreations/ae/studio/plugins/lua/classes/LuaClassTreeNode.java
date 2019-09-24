package com.andcreations.ae.studio.plugins.lua.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class LuaClassTreeNode {
    /** */
    private LuaClass luaClass;
    
    /** */
    private List<LuaClassTreeNode> childNodes = new ArrayList<>();
    
    /** */
    LuaClassTreeNode() {
    }
    
    /** */
    LuaClassTreeNode(LuaClass luaClass) {
        this.luaClass = luaClass;
    }
    
    /** */
    public LuaClass getLuaClass() {
        return luaClass;
    }
    
    /** */
    void addChildNode(LuaClassTreeNode childNode) {
        childNodes.add(childNode);
    }
    
    /** */
    public List<LuaClassTreeNode> getChildNodes() {
        return Collections.unmodifiableList(childNodes);
    }
    
    /** */
    private String toString(int indent) {
        StringBuilder builder = new StringBuilder();
        for (LuaClassTreeNode childNode:childNodes) {
            for (int index = 0; index < indent; index++) {
                builder.append("  ");
            }
            builder.append(childNode.getLuaClass().getLuaModule());
            builder.append("\n");
            builder.append(childNode.toString(indent + 1));
        }
        
        return builder.toString();
    }
    
    /** */
    @Override
    public String toString() {
        return toString(0);
    }
    
    /** */
    void sortTree(Comparator<LuaClassTreeNode> comparator) {
        Collections.sort(childNodes,comparator);
        for (LuaClassTreeNode childNode:childNodes) {
            childNode.sortTree(comparator);
        }
    }
}