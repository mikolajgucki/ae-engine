package com.andcreations.io;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Builds the list of all the files and directories contained in a directory
 * and its subdirectories and in the subdirectories of the subdirectories
 * and so on.
 * 
 * @author Mikolaj Gucki
 */
public class DirScanner {
    /**
     * A stack item.
     */
    private class Item {
        /** The file stepped in. */
        private File file;
        
        /** The subfiles. */
        private File[] files;
        
        /** The index of the next (sub)file. */
        private int index;
        
        /**
         * Constructs a {@link FullFileList} object.
         * 
         * @param file The file stepped in.
         * @param fileFilter The file filter.
         */
        private Item(File file,FileFilter fileFilter) {
            this.file = file;
            this.files = file.listFiles(fileFilter);
        }
        
        /**
         * Gets the file.
         * 
         * @return The file.
         */
        private File getFile() {
            return file;
        }
        
        /**
         * Gets the next subfile.
         * 
         * @return The next subfile.
         */
        private File getNextSubfile() {
            if (files == null || index == files.length) {
                return null;                
            }
                        
            return files[index++];
        }
    }
    
    /** The root directory. */
    private File root;
    
    /** Indicates if to include the directories in the result paths. */
    private boolean includeDir = true;  
    
    /**
     * Constructs a {@link DirScanner}.
     * 
     * @param root The root directory.
     */
    public DirScanner(File root) {
        this(root,true);
    }
    
    /**
     * Constructs a {@link DirScanner} object.
     * 
     * @param root The root directory.
     * @param includeDir Indicates if to include the directories in the result
     *     paths.
     */
    public DirScanner(File root,boolean includeDir) {
        this.root = root;
        this.includeDir = includeDir;
    }    
    
    /**
     * Builds the path to the directory currently stepped in.
     * 
     * @param stack The stack of the directories stepped in.
     * @return The path to the directory.
     */
    private String getPath(Stack<Item> stack) {
        String path = "";
        
    // from 1 to ignore the root directory
        for (int index = 1; index < stack.size(); index++) {
            if (path.length() > 0) {
                path += File.separator;
            }
            
            Item item = stack.get(index);
            path += item.getFile().getName();
        }
    
        if (path.length() > 0) {
            path += File.separator;
        }
        
        return path;
    }
    
    /**
     * Builds the full file list.
     * 
     * @param fileFilter The file filter.
     * @return The list of the path relative to the root directory.
     */
    public String[] build(FileFilter fileFilter) {
        List<String> files = new ArrayList<String>();        
        
        Stack<Item> stack = new Stack<Item>();        
        stack.push(new Item(root,fileFilter));
        
        while (stack.isEmpty() == false) {
            Item item = stack.peek();
            File nextFile = item.getNextSubfile();
            
            if (nextFile == null) {
                stack.pop();
                continue;
            }
            
            if (nextFile.isFile() == true) {
                files.add(getPath(stack) + nextFile.getName());
                continue;
            }
            
            if (includeDir == true) {
                files.add(getPath(stack) + nextFile.getName());
            }
            
            stack.push(new Item(nextFile,fileFilter));
        }
        
        return files.toArray(new String[]{});
    }
    
    /**
     * Builds the full file list.
     * 
     * @return The list of the path relative to the root directory.
     */
    public String[] build() {
        return build(null);
    }
    
    /**
     * Gets a file for a given path.
     *
     * @param path The path.
     * @return The corresponding file.
     */
    public File getFile(String path) {
        return new File(root,path);
    }
}
