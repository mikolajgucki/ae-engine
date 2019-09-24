package com.andcreations.ae.sdk.update;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class FileSetList implements Iterable<FileSet> {
    /** */
    private List<FileSet> fileSets = new ArrayList<>();
    
    /** */
    public void add(FileSet fileSet) {
        this.fileSets.add(fileSet);
    }
    
    /** */
    public void add(List<FileSet> fileSets) {
        this.fileSets.addAll(fileSets);
    }
    
    /** */
    public void add(FileSetList fileSetList) {
        for (FileSet fileSet:fileSetList) {
            add(fileSet);
        }
    }
    
    /** */
    @Override
    public Iterator<FileSet> iterator() {
        return fileSets.iterator();
    }
    
    /** */
    public boolean containsPath(String path) {
        for (FileSet fileSet:fileSets) {
            if (fileSet.containsPath(path) == true) {
                return true;
            }
        }
        
        return false;
    }
    
    /** */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (FileSet fileSet:fileSets) {
            str.append(fileSet.toString());
        }
        return str.toString();
    }
}