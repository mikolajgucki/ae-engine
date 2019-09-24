package com.andcreations.io;

import java.util.Comparator;

/**
 * Sorts file nodes by nodes (directories before files).
 *
 * @author Mikolaj Gucki
 */
public class FileNodeComparator implements Comparator<FileNode> {
    /** */
    @Override
    public int compare(FileNode a,FileNode b) {
    // directories before files
        if (a.isDirectory() == true && b.isDirectory() == false) {
            return -1;
        }
        if (a.isDirectory() == false && b.isDirectory() == true) {
            return 1;
        }
        
        String nameA = a.getName();
        String nameB = b.getName();
    // compare by name
        if (nameA == null && nameB == null) {
            return 0;
        }
        if (nameA != null) {
            return nameA.compareToIgnoreCase(nameB);
        }
        return nameB.compareToIgnoreCase(nameA);
    }
    
    /** */
    @Override            
    public boolean equals(Object object) {
        return this == object;
    }    
}