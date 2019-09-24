package com.andcreations.ae.studio.plugins.file.explorer.tree;

import java.util.List;

import javax.swing.JPopupMenu;

import com.andcreations.ae.studio.plugins.file.explorer.tree.actions.PopupAction;
import com.andcreations.ae.studio.plugins.file.explorer.tree.actions.PopupActionRegistry;

/**
 * @author Mikolaj Gucki
 */
public class DefaultPopupMenuBuilder {
    /** */
    void appendMenuItems(FileTreeNode root,List<FileTreeNode> nodes,
        JPopupMenu popup) {
    //
        for (PopupAction action:PopupActionRegistry.get().getActions()) {
            if (action.canPerform(root,nodes) == true) {
                popup.add(action.createMenuItem(root,nodes));
            }
        }        
    }
}