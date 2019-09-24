package com.andcreations.ae.studio.plugins.file.problems;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.file",
    "com.andcreations.ae.studio.plugins.problems"
})
public class FileProblemsPlugin extends Plugin<PluginState> {
}