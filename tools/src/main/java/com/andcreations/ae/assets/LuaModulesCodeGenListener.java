package com.andcreations.ae.assets;

import java.io.File;
import java.io.IOException;

/**
 * @author Mikolaj Gucki
 */
public interface LuaModulesCodeGenListener {
    /** */
    void luaModuleCodeGenDirCreated(File dir);
    
    /** */
    void luaModuleCodeGenFileCreated(File file);
    
    /** */
    void failedToGenLuaModulesCode(File file,IOException exception);
    
    /** */
    void generatedLuaModuleCode(File file);
}