package com.andcreations.ae.dist;

import java.io.File;

import com.andcreations.lua.LuaUtil;

/**
 * @author Mikolaj Gucki
 */
public class AEDistFiles {
    /** The path to binaries. */
    public static final String BIN_PATH = "bin";
    
    /** The path to common Lua sources. */
    public static final String LUA_PATH = "lua";
    
    /** The path to plugins in an AE distribution. */
    public static final String PLUGINS_PATH = "plugins";
    
    /** The path to Lua sources in a plugin. */
    public static final String PLUGIN_LUA_SRC_PATH = "common/src/lua";
    
    /** The path to the libraries. */
    public static final String LIBS_PATH = "libs";
    
    /** The path to the AE Studio libraries. */
    public static final String LIBS_STUDIO_PATH = "libs/studio";
    
    /** The path to the test files. */
    public static final String TEST_PATH = "test";
    
    /** The path to the test Lua sources. */
    public static final String TEST_LUA_PATH = TEST_PATH + "/lua";
    
    /** */
    public static File getBinDir(File aeDistDir) {
        return new File(aeDistDir,BIN_PATH);
    }
    
    /** */
    public static File getLuaExec(File aeDistDir) {
        return new File(getBinDir(aeDistDir),LuaUtil.getLuaExecName());
    }
    
    /** */
    public static File getPluginsDir(File aeDistDir) {
        return new File(aeDistDir,PLUGINS_PATH);
    }
 
    /** */
    public static File getPluginDir(File aeDistDir,String pluginId) {
        return new File(getPluginsDir(aeDistDir),pluginId);
    }
    
    /** */
    public static File getPluginLuaSrcDir(File pluginDir) {
        return new File(pluginDir,PLUGIN_LUA_SRC_PATH);
    }
    
    /** */
    public static File getPluginLuaSrcDir(File aeDistDir,String pluginId) {
        return getPluginLuaSrcDir(getPluginDir(aeDistDir,pluginId));
    }
    
    /** */
    public static File getTestDir(File aeDistDir) {
        return new File(aeDistDir,TEST_PATH);
    }
    
    /** */
    public static File getLuaTestSrcDir(File aeDistDir) {
        return new File(aeDistDir,TEST_LUA_PATH);
    }
}
