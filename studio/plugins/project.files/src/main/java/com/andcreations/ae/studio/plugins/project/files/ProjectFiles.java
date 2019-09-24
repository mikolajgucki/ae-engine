package com.andcreations.ae.studio.plugins.project.files;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.andcreations.io.FileUtil;

/**
 * Provides the project-related file methods.
 *
 * @author Mikolaj Gucki
 */
public class ProjectFiles {
    /** The storage directory. */
    public static final String STORAGE_DIR = ".ae.studio";    
    
    /** */
    private static ProjectFiles instance;    
    
    /** */
    private File projectDir;
    
    /** */
    private Comparator<File> projectFileComprator = new Comparator<File>() {
        /** */
        @Override
        public int compare(File a,File b) {
            boolean isProjectFileA = isProjectFile(a);
            boolean isProjectFileB = isProjectFile(b);
            
            if (isProjectFileA == false && isProjectFileB == true) {
                return 1;
            }
            if (isProjectFileA == true && isProjectFileB == false) {
                return -1;
            }
            
            return a.getAbsolutePath().compareTo(b.getAbsolutePath());
        }
    };
    
    /** */
    public boolean isProjectFile(File file) {
        return FileUtil.isAncestor(projectDir,file);        
    }
    
    /** */
    public boolean allProjectFiles(Iterable<File> files) {
        for (File file:files) {
            if (isProjectFile(file) == false) {
                return false;
            }
        }
        
        return true;
    }
    
    /** */
    public String getRelativePath(File file) {
        if (isProjectFile(file) == false) {
            return file.getAbsolutePath();
        }
        
        String path = FileUtil.relativize(projectDir,file);
        if (path == null) {
            return file.getAbsolutePath();
        }
        
        return path;
    }
    
    /** */
    public File getFileFromRelativePath(String relativePath) {
        return new File(projectDir,relativePath);
    }
    
    /** */
    public void init(File projectDir) {
        this.projectDir = projectDir;
    }
    
    /** */
    public File getProjectDir() {
        return projectDir;
    }
    
    /**
     * Gets the comparator which sorts files by name (project files before
     * the others).
     *
     * @return The comparator.
     */
    public Comparator<File> getComparator() {
        return projectFileComprator;
    }
    
    /**
     * Sorts files by name (project files before the others).
     *
     * @param files The files to sort.
     */
    public void sort(List<File> files) {
        Collections.sort(files,getComparator());
    }
    
    /** */
    public static ProjectFiles get() {
        if (instance == null) {
            instance = new ProjectFiles();
        }
        
        return instance;
    }    
}