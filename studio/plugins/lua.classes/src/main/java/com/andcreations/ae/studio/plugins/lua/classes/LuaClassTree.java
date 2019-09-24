package com.andcreations.ae.studio.plugins.lua.classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class LuaClassTree {
    /** */
    private LuaClassTreeNode root;
    
    /** */
    private LuaClassTree(LuaClassTreeNode root) {
        this.root = root;        
    }
    
    /** */
    public LuaClassTreeNode getRoot() {
        return root;
    }
    
    /** */
    private static LuaClassTreeNode findNodeByModule(
        List<LuaClassTreeNode> nodes,String luaModule) {
    //
        for (LuaClassTreeNode node:nodes) {
            if (node.getLuaClass().getLuaModule().equals(luaModule)) {
                return node;
            }
        }

        return null;            
    }
    
    /** */
    @Override
    public String toString() {
        return root.toString();
    }
    
    /** */
    static LuaClassTree build(Collection<LuaClass> luaClasses) {
        LuaClassTreeNode root = new LuaClassTreeNode();
        List<LuaClassTreeNode> nodes = new ArrayList<>();
        
        luaClasses = new HashSet<>(luaClasses);
        while (luaClasses.isEmpty() == false) {
            LuaClass luaClassToAdd = null;
            for (LuaClass luaClass:luaClasses) {
                String superclass = luaClass.getSuperclassName();
            // if it's a top class or its superclass already added to nodes
                if (superclass == null ||
                    findNodeByModule(nodes,superclass) != null) {
                //
                    luaClassToAdd = luaClass;
                    break;
                }
            }
            luaClasses.remove(luaClassToAdd);
            
            LuaClassTreeNode node = new LuaClassTreeNode(luaClassToAdd);
            nodes.add(node);
            
        // build hierarchy
            if (luaClassToAdd.getSuperclassName() != null) {
                LuaClassTreeNode parentNode = findNodeByModule(
                    nodes,luaClassToAdd.getSuperclassName());
                parentNode.addChildNode(node);
            }
            else {
                root.addChildNode(node);
            }
        }
        
        return new LuaClassTree(root);
    }
    
    /** */
    public void sort(Comparator<LuaClassTreeNode> comparator) {
        root.sortTree(comparator);
    }
    
    /** */
    public void sortByName() {
        sort(new Comparator<LuaClassTreeNode>() {
            /** */
            @Override
            public boolean equals(Object obj) {
                return this == obj;
            }
            
            /** */
            @Override
            public int compare(LuaClassTreeNode a,LuaClassTreeNode b) {
                return a.getLuaClass().getName().compareTo(
                    b.getLuaClass().getName());
            }
        });
    }
}