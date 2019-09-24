package com.andcreations.ae.studio.plugins.lua.explorer.tree.actions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class PopupActionRegistry {
    /** */
    private static PopupActionRegistry instance = new PopupActionRegistry();
    
    /** */
    private List<PopupAction> actions = new ArrayList<>();
    
    /** */
    private PopupActionRegistry() {
        create();
    }
    
    /** */
    private void create() {
        actions.add(new AddLuaFileAction());
        actions.add(new AddDirectoryAction());
        actions.add(new DeleteLuaFileAction());
        actions.add(new DeleteDirectoryAction());
    }

    
    /** */
    public static List<PopupAction> actions() {
        return instance.actions;
    }
}
