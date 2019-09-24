package com.andcreations.ae.studio.plugins.lua.explorer;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.RequiredPlugins;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;
import com.andcreations.ae.studio.plugins.lua.explorer.search.LuaSearch;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.ui.main",
    "com.andcreations.ae.studio.plugins.text.editor",
    "com.andcreations.ae.studio.plugins.lua",
    "com.andcreations.ae.studio.plugins.lua.lib",
    "com.andcreations.ae.studio.plugins.lua.parser",
    "com.andcreations.ae.studio.plugins.file",
    "com.andcreations.ae.studio.plugins.file.explorer",
    "com.andcreations.ae.studio.plugins.project",
    "com.andcreations.ae.studio.plugins.project.files",
    "com.andcreations.ae.studio.plugins.todo",
    "com.andcreations.ae.studio.plugins.search"
})
@RequiredPlugins(id={
    "com.andcreations.ae.studio.plugins.outline"
})
public class LuaExplorerPlugin extends Plugin<PluginState> {
    /** */
    @Override
    public void start() throws PluginException {
        LuaExplorerInit luaExplorerInit = new LuaExplorerInit();
        luaExplorerInit.init();
        
        createOpenLuaFile();
        createGoToLuaItem();
        createLuaOutline();
        createLuaToDoSource();
        initLuaSearch();
    }
    
    /** */
    private void createOpenLuaFile() {
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                OpenLuaFile.get().init();
            }
        });
    }
    
    /** */
    private void createGoToLuaItem() {
        GoToLuaItem.get().init();
    }
    
    /** */
    private void createLuaOutline() {
        LuaOutline.get().init();
    }
    
    /** */
    private void createLuaToDoSource() {
        LuaToDoSource.create();
    }
    
    /** */
    private void initLuaSearch() {
        LuaSearch.get().init();
    }
}