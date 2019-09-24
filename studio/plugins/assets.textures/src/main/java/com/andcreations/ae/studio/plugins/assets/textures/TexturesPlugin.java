package com.andcreations.ae.studio.plugins.assets.textures;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.Required;
import com.andcreations.ae.studio.plugin.RequiredPlugins;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;
import com.andcreations.ae.studio.plugin.StartBeforeAndRequire;

/**
 * @author Mikolaj Gucki
 */
@StartBeforeAndRequire(id={        
    "com.andcreations.ae.studio.plugins.project.explorer"
})
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.ui.main",
    "com.andcreations.ae.studio.plugins.project.files",        
    "com.andcreations.ae.studio.plugins.project",
    "com.andcreations.ae.studio.plugins.file",
    "com.andcreations.ae.studio.plugins.file.problems",
    "com.andcreations.ae.studio.plugins.text.editor",
    "com.andcreations.ae.studio.plugins.console",
    "com.andcreations.ae.studio.plugins.resources",
    "com.andcreations.ae.studio.plugins.assets",
    "com.andcreations.ae.studio.plugins.assets.fonts",
    "com.andcreations.ae.studio.plugins.wizards"
})
@RequiredPlugins(id={
    "com.andcreations.ae.studio.plugins.file.explorer",
    "com.andcreations.ae.studio.plugins.problems",
    "com.andcreations.ae.studio.plugins.outline",
    "com.andcreations.ae.studio.plugins.builders",
    "com.andcreations.ae.studio.plugins.assets"
})
public class TexturesPlugin extends Plugin<PluginState> {
    /** */
    @Required(id="com.andcreations.ae.studio.plugins.project")
    private Object projectPluginObject;    
    
    /** */
    @Override
    public void start() throws PluginException {
        TexturesIcons.init();
        TexPack.get().init(projectPluginObject);
        
        createTexPackOutline();
    }
    
    /** */
    private void createTexPackOutline() {
        new TexPackOutline();
    }
}