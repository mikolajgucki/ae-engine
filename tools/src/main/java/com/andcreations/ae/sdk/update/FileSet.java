package com.andcreations.ae.sdk.update;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import com.andcreations.io.DirScanner;

/**
 * @author Mikolaj Gucki
 */
public class FileSet {
    /** */
    private File srcDir;
    
    /** */
    private String[] paths;
    
    /** */
    private String prefix;
    
    /** */
    FileSet(File srcDir,String[] paths) {
        this.srcDir = srcDir;
        this.paths = paths;
    }

    /** */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    /** */
    public String[] getPaths() {
        return paths;
    }
    
    /** */
    public boolean containsPath(String path) {
        String nnPrefix = prefix != null ? prefix : "";
        
        for (String fileSetPath:paths) {
            //System.out.println("fileSetPath=" + (nnPrefix + fileSetPath) + " " + path);
            if (path.equals(nnPrefix + fileSetPath) == true) {
                return true;
            }
        }
        
        return false;
    }
    
    /** */
    public File getSrcFile(String path) {
        return new File(srcDir,path);
    }
    
    /** */
    public File getDstFile(File dir,String path) {
        if (prefix == null) {
            return new File(dir,path);
        }
        return new File(dir,prefix + path);
    }
    
    /** */
    static FileSet build(File dir,FileFilter fileFilter) {
        DirScanner scanner = new DirScanner(dir);        
        return new FileSet(dir,scanner.build(fileFilter));
    }
    
    /** */
    static FileSet build(File dir) {
        return build(dir,null);
    }
    
    /** */
    static void setPrefix(List<FileSet> fileSets,String prefix) {
        for (FileSet fileSet:fileSets) {
            fileSet.setPrefix(prefix);
        }
    }
    
    /** */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(srcDir.getAbsolutePath() + "\n");
        
        for (String path:paths) {
            str.append(" " + path + "\n");
        }        
        
        return str.toString();
    }
}