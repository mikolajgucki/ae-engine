package com.andcreations.ae.lua.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.andcreations.io.DirScanner;

/**
 * @author Mikolaj Gucki
 */
public class LuaTestUtil {
    /** */
    public static boolean isLuaTestFile(File file) {
        String name = file.getName();
        return name.endsWith("test.lua") || name.endsWith("Test.lua");
    }
    
    /**
     * Lists all Lua test files in a directory.
     *
     * @param dir The directory.
     * @return The Lua test files.
     */
    public static List<File> listLuaTestFiles(File dir) {
        DirScanner scanner = new DirScanner(dir,false);
        String[] paths = scanner.build();
        
    // sort by name
        Arrays.sort(paths);
        
        List<File> files = new ArrayList<>();
    // for each path
        for (String path:paths) {
            File file = scanner.getFile(path);
            if (isLuaTestFile(file) == true) {
                files.add(file);
            }
        }
        
        return files;
    }
}