package com.andcreations.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.resources.FileResource;

/**
 * @author Mikolaj Gucki
 */
public class FileSetHelper {
    /**
     * Gets the files for a file set.
     * 
     * @param fileSet The file set.
     * @return The files contained in the file set.
     */
    public static File[] getFiles(FileSet fileSet) {
        List<File> files = new ArrayList<File>();
        
        Iterator<?> iterator = fileSet.iterator();
        while (iterator.hasNext() == true) {
            FileResource res = (FileResource)iterator.next();
            files.add(res.getFile());
        }
        
        return files.toArray(new File[]{});
    }
}
