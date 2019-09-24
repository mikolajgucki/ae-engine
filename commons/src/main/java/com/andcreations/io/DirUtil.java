package com.andcreations.io;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Contains directory-related utility methods.
 *
 * @author Mikolaj Gucki
 */
public class DirUtil {
    /**
     * Compares strings by their lengths.
     */
    private static class StrLenComparator implements Comparator<String> {
        /** */
        @Override
        public int compare(String a, String b) {
            return b.length() - a.length();
        }        
    }
    
    /**
     * Deletes a either empty or non-empty directory.
     * 
     * @param dir The directory.
     * @return <tt>true</tt> if the directory was successfully deleted,
     *   <tt>false</tt> otherwise.
     */
    public static boolean delete(File dir) {
        DirScanner dirScanner = new DirScanner(dir);
        
        String[] paths = dirScanner.build();
    // sort the paths so that the deletion will start from the deepest ones
        Arrays.sort(paths,new StrLenComparator());
        
        for (String path:paths) {
            File file = new File(dir.getAbsolutePath() + File.separator + path);
            if (file.delete() == false) {
                return false;
            }
        }
        
        return dir.delete();
    }
}
