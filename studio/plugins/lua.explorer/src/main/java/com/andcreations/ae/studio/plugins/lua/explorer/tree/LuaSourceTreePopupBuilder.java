package com.andcreations.ae.studio.plugins.lua.explorer.tree;

import javax.swing.JPopupMenu;

import com.andcreations.ae.studio.plugins.lua.explorer.tree.actions.PopupAction;
import com.andcreations.ae.studio.plugins.lua.explorer.tree.actions.PopupActionRegistry;
import com.andcreations.io.FileNode;

/**
 * @author Mikolaj Gucki
 */
class LuaSourceTreePopupBuilder {
    /** */
    private JPopupMenu popup = new JPopupMenu();
    
    /** */
    LuaSourceTreePopupBuilder() {
    }
    
    /** */
    JPopupMenu build(LuaSourceTreeNode node) {
        popup.removeAll();
        
        FileNode fileNode = null;
        if (node != null) {
            fileNode = node.getFileNode();
        }
        
        for (PopupAction action:PopupActionRegistry.actions()) {
            if (action == null) {
                popup.addSeparator();
                continue;
            }
            
            if (action.canPerform(fileNode) == true) {
                popup.add(action.createMenuItem(fileNode));
            }
        }
        
        return popup;
    }
}