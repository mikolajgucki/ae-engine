package com.andcreations.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Contains file-related utility methods.
 *
 * @author Mikolaj Gucki
 */
public class FileUtil {
    /** */
    public static File canonical(File file) {
    	if (file == null) {
    		return null;
    	}
        try {
            file = file.getCanonicalFile();
        } catch (IOException exception) {
        }        
        return file;
    }    
    
    /** */
    public static String relativize(File parent,File file) {
        Path filePath = Paths.get(file.getAbsolutePath()).normalize();
        Path parentPath = Paths.get(parent.getAbsolutePath()).normalize();
        
        try {
            Path relativePath = parentPath.relativize(filePath);
            String path = relativePath.toString();
            return path.replace('\\','/');
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }
    
    /** */
    public static boolean isAncestor(File ancestor,File child) {
        Path ancestorPath =
            Paths.get(ancestor.getAbsolutePath()).toAbsolutePath();
        Path childPath = Paths.get(child.getAbsolutePath()).toAbsolutePath();
        
        return childPath.startsWith(ancestorPath);
    }
    
    /** */
    public static boolean hasAncestor(File file,String ancestorName) {
        file = file.getParentFile();
        while (true) {            
            if (file == null) {
                break;
            }
            if (file.getName().equals(ancestorName) == true) {
                return true;
            }
            file = file.getParentFile();
        }
        
        return false;
    }
}