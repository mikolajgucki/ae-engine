package com.andcreations.ae.studio.plugins.assets.fonts;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.Required;
import com.andcreations.ae.studio.plugin.RequiredPlugins;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={        
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.ui.main",
    "com.andcreations.ae.studio.plugins.project.files",
    "com.andcreations.ae.studio.plugins.project",
    "com.andcreations.ae.studio.plugins.file",
    "com.andcreations.ae.studio.plugins.file.problems",
    "com.andcreations.ae.studio.plugins.console",
    "com.andcreations.ae.studio.plugins.builders",
    "com.andcreations.ae.studio.plugins.resources",    
    "com.andcreations.ae.studio.plugins.assets",
})
@RequiredPlugins(id={
    "com.andcreations.ae.studio.plugins.problems",
    "com.andcreations.ae.studio.plugins.file.explorer",
    "com.andcreations.ae.studio.plugins.text.editor",
    "com.andcreations.ae.studio.plugins.wizards",
})
public class FontsPlugin extends Plugin<PluginState> {
    /** */
    @Required(id="com.andcreations.ae.studio.plugins.project")
    private Object projectPluginObject;    
    
    /** */
    @Override
    public void start() throws PluginException {
        FontsIcons.init();
        TexFont.get().init(projectPluginObject);
    }
}