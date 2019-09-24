package com.andcreations.ae.studio.plugins.search;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.RequiredPlugins;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;

/**
 * The plugins which allows to search text files.
 * 
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.project",
    "com.andcreations.ae.studio.plugins.ui.main"
})
@RequiredPlugins(id={
    "com.andcreations.ae.studio.plugins.search.common"
})
public class SearchPlugin extends Plugin<PluginState> {
    /** */
    private SearchDialog searchDialog;
    
    /** */
    @Override
    public void start() throws PluginException {
        createSearchDialog();
        Search.get().initUI(searchDialog);
    }
    
    /** */
    private void createSearchDialog() {
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {        
                searchDialog = new SearchDialog();
                searchDialog.addMenuItem();
            }
        });
    }
}