package com.andcreations.ae.studio.plugins.lua.lib.runner;

import java.io.File;

import com.andcreations.ae.dist.AEDistFiles;
import com.andcreations.ae.studio.plugins.ae.dist.AEDist;
import com.andcreations.lua.runner.LuaRunner;

/**
 * @author Mikolaj Gucki
 */
public class LuaRunnerFactory {
    /** */
    private static LuaRunnerFactory instance;
    
    /** */
    public LuaRunner createLuaRunner() {
        File aeDistDir = AEDist.get().getAEDistDir();
        return new LuaRunner(AEDistFiles.getLuaExec(aeDistDir));
    }
    
    /** */
    public static LuaRunnerFactory get() {
        if (instance == null) {
            instance = new LuaRunnerFactory();
        }
        
        return instance; 
    }
}