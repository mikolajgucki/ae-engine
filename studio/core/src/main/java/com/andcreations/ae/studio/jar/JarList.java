package com.andcreations.ae.studio.jar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Finds and holds all the jar files in specified directories.
 *
 * @author Mikolaj Gucki
 */
public class JarList {
    /**
     * The jar files found in the directories.
     */
    private List<File> jarFiles;
    
    /**
     * Constructs a JarList object building the jar file list.
     *
     * @param directories The array of directory names with module files.
     */
    public JarList(String[] directories) {
        buildJarNameList(directories);
    }
    
    /**
     * Builds the list of jar files in a specified directory.
     *
     * @param directory The directory in which to look for jar files.
     */
    private void buildJarList(String directory) {
        File file = new File(directory);
        String list[] = file.list();
    
    // if no files given
        if (list == null) {
            return;
        }
      
        for (int index = 0; index < list.length; index++) {
            String name = list[index];
      
            if (name.endsWith(".jar")) {
                jarFiles.add(new File(file,name));
            }
        }
    }
    
    /**
     * Builds the list of jar files in specified directories.
     *
     * @param directories The directories in which to look for jar files.
     */
    private void buildJarNameList(String[] directories) {
        jarFiles = new ArrayList<>();
    
        for (int index = 0; index < directories.length; index++) {
            buildJarList(directories[index]);
        }
    }
    
    /**
     * Gets the JAR files.
     *
     * @return The JAR files.
     */
    public List<File> getJarFiles() {
        return jarFiles;
    }
}
