package com.andcreations.ae.studio.plugins.todo;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;

/**
 * The plugins which displays TODO list.
 * 
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.ui.main",
    "com.andcreations.ae.studio.plugins.file",
    "com.andcreations.ae.studio.plugins.problems",
    "com.andcreations.ae.studio.plugins.text.editor"
})
public class ToDoPlugin extends Plugin<PluginState> {
    /** */
    @Override
    public void start() throws PluginException {
        ToDoIcons.init();
        ToDo.get().init();
    }
}