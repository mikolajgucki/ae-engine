package com.andcreations.ae.studio.plugins.lua.compiler;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.RequiredPlugins;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;
import com.andcreations.ae.studio.plugins.lua.lib.NativeLua;
import com.andcreations.ae.studio.plugins.problems.ProblemSeverity;
import com.andcreations.ae.studio.plugins.problems.Problems;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.lua.lib",
    "com.andcreations.ae.studio.plugins.project",
    "com.andcreations.ae.studio.plugins.file.problems",
    "com.andcreations.ae.studio.plugins.text.editor"
})
@RequiredPlugins(id={
    "com.andcreations.ae.studio.plugins.file",
    "com.andcreations.ae.studio.plugins.problems",
    "com.andcreations.ae.studio.plugins.lua"
})
public class LuaCompilerPlugin extends Plugin<PluginState> {
    /** */
    private static final String PROBLEM_SOURCE_ID =
        LuaCompilerPlugin.class.getName();
        
    /** */
    private static final BundleResources res =
        new BundleResources(LuaCompilerPlugin.class);        
        
    /** */
    private LuaCompiler compiler;
    
    /** */
    @Override
    public void start() throws PluginException {
        startCompiler();
    }
    
    /** */
    @Override
    public void stop() throws PluginException {
        stopCompiler();
    }
    
    /** */
    private void startCompiler() throws PluginException {
        if (NativeLua.isLoaded() == false) {
            String msg = res.getStr("no.lua.lib");
            Log.warning(msg);
            Problems.get().add(PROBLEM_SOURCE_ID,ProblemSeverity.WARNING,msg);
            return;
        }        
        
        compiler = new LuaCompiler();
        compiler.start();
    }
    
    /** */
    private void stopCompiler() {
        if (compiler != null) {
            compiler.stop();
        }
    }
}