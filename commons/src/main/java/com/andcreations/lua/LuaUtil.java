package com.andcreations.lua;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.system.OS;

/**
 * Provides Lua-related utility methods.
 *
 * @author Mikolaj Gucki
 */
public class LuaUtil {
    /** */
    public static final String LUA_FILE_SUFFIX = "lua";
    
    /** */
    public static final String LUA_FILE_DOT_SUFFIX = "." + LUA_FILE_SUFFIX;
    
    /**
     * Gets the path which can be appended to <code>package.path</code>.
     *
     * @param dir The directory.
     * @return The package path corresponding to the directory.
     */
    public static String getPackagePath(File dir) {
        return dir.getAbsolutePath().replace('\\','/');
    }
    
    /**
     * Gets the paths which can be appended to <code>package.path</code>.
     *
     * @param dirs The directories.
     * @return The package paths corresponding to the directories.
     */
    public static List<String> getPackagePaths(List<File> dirs) {
        List<String> pkgPaths = new ArrayList<>();
        for (File dir:dirs) {
            pkgPaths.add(getPackagePath(dir));
        }
        
        return pkgPaths;
    }
    
    /**
     * Tests if a filename denotes a Lua file.
     *
     * @param filename The filename.
     * @return <code>true</code> if the filename denotes a Lua file,
     *   <code>false</code> otherwise.
     */
    public static boolean isLuaFile(String filename) {
        return filename.endsWith(LUA_FILE_DOT_SUFFIX);
    }
    
    /**
     * Tests if a file denotes a Lua file.
     *
     * @param file The file.
     * @return <code>true</code> if the file denotes a Lua file,
     *   <code>false</code> otherwise.
     */    
    public static boolean isLuaFile(File file) {
        if (file == null) {
            return false;
        }
        
        return isLuaFile(file.getName());
    }
    
    /**
     * Gets the Lua executable name.
     *
     * @return The Lua executable name.
     */
    public static String getLuaExecName() {
        if (OS.getOS() == OS.WINDOWS) {
            return "lua.exe";
        }            
        if (OS.getOS() == OS.OSX ||
            OS.getOS() == OS.LINUX) {
        //
            return "lua";
        }            
        
        throw new RuntimeException("Unsupported OS");
    }
}