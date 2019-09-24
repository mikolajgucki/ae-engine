package com.andcreations.ae.project;

import java.io.File;

import com.andcreations.io.FileUtil;

/**
 * Provides IO methods related to a project.
 *
 * @author Mikolaj Gucki
 */
public class AEProjectIO {
    /**
     * Gets a directory from a path relative to the project directory.
     *
     * @param projectDir The project directory.
     * @param path The path.
     * @return The directory.
     */
    public static File getDirFromPath(File projectDir,String path) {
        File dir = new File(path);
        if (dir.isAbsolute() == true) {
            dir = FileUtil.canonical(dir);
        } else {
            dir = FileUtil.canonical(new File(projectDir,path));
        }        
        
        return dir;
    }
    
    /**
     * Gets directories from paths relative to the project directory.
     *
     * @param projectDir The project directory.
     * @param paths The paths.
     * @return The directories.
     */
    public static File[] getDirsFromPaths(File projectDir,String[] paths) {
        File[] dirs = new File[paths.length];
        
        for (int index = 0; index < paths.length; index++) {
            dirs[index] = getDirFromPath(projectDir,paths[index]);
            /*
            File dir = new File(paths[index]);
            if (dir.isAbsolute() == true) {
                dirs[index] = FileUtil.canonical(dir);
            } else {
                dirs[index] = FileUtil.canonical(
                    new File(projectDir,paths[index]));
            }*/
        }
        
        return dirs;
    }    
}
