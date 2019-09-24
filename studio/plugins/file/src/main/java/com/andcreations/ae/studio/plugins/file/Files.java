package com.andcreations.ae.studio.plugins.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;

import com.andcreations.ae.studio.util.ListenerList;
import com.andcreations.io.DirScanner;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class Files {
    /** */
    @SuppressWarnings("rawtypes")
    private class DeleteWalker extends DirectoryWalker {
        /** */
        private File root;      
        
        /** */
        DeleteWalker(File root) {
            this.root = root;
        }
        
        /** */
        @SuppressWarnings("unchecked")
        private void delete() throws IOException {
            walk(root,null);
        }
        
        /** */
        @Override
        protected void handleDirectoryEnd(File dir,int depth,
            Collection results) {
        //
            deleteEmptyDir(dir);
        }

        /** */
        @Override
        protected void handleFile(File file,int depth,Collection results)
            throws IOException {
        //
            deleteFile(file);
        }
    }
    
    /** */
    public static final String NAME_REGEX = "[a-zA-Z_0-9\\.]+";
    
    /** */
    public static final char SEPARATOR = '/';
    
    /** */
    public static final String PATH_REGEX = String.format(
        "%c?%s(%c%s)*",SEPARATOR,NAME_REGEX,SEPARATOR,NAME_REGEX);
    
    /** */
    public static final String RELATIVE_PATH_REGEX = String.format(
        "%s(%c%s)*",NAME_REGEX,SEPARATOR,NAME_REGEX);
    
    /** */
    public static final String ABSOLUTE_PATH_REGEX = String.format(
        "%c%s(%c%s)*",SEPARATOR,NAME_REGEX,SEPARATOR,NAME_REGEX);
    
    /** */
    private static Files instance;
    
    /** */
    private BundleResources res = new BundleResources(Files.class);      
    
    /** */
    private List<FileEntry> entries = new ArrayList<>();
    
    /** */
    private ListenerList<FileListener> listeners =
        new ListenerList<FileListener>();
    
    /** */
    private File tmpDir;
        
    /** */
    private FileEntry getEntry(File file) {
        if (file == null) {
            return null;
        }
        
        synchronized (entries) {
            for (FileEntry entry:entries) {
                if (entry.getFile().equals(file)) {
                    return entry;
                }
            }
            
            FileEntry entry = new FileEntry(file);
            entries.add(entry);
            
            return entry;
        }        
    }
    
    /**
     * Adds a listener listening to all the files.
     *
     * @param listener The file listener.
     */
    public void addFileListener(FileListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Removes a listener listening to all the files.
     *
     * @param listener The file listener.
     */
    public void removeFileListener(FileListener listener) {
        listeners.remove(listener);
    }
    
    /** */
    public void addIssue(File file,FileIssue issue) {
        FileEntry entry = getEntry(file);
        entry.addIssue(issue);
        notifyFileIssuesChanged(entry);
    }
    
    /** */
    public void removeIssue(File file,FileIssue issue) {
        FileEntry entry = getEntry(file);
        if (entry.removeIssue(issue) == true) {
            notifyFileIssuesChanged(entry);
        }
    }
    
    /** */
    public boolean hasIssues(File file) {
        FileEntry entry = getEntry(file);
        if (entry == null) {
            return false;
        }
        
        return entry.hasIssues();
    }
        
    /** */
    public boolean hasIssues(File file,FileIssueSeverity severity) {
        FileEntry entry = getEntry(file);
        if (entry == null) {
            return false;
        }
        
        return entry.hasIssues(severity);
    }
    
    /** */
    public boolean hasErrors(File file) {
        return hasIssues(file,FileIssueSeverity.ERROR);       
    }
    
    /** */
    public boolean hasWarnings(File file) {
        return hasIssues(file,FileIssueSeverity.WARNING);       
    }
    
    /** */
    public List<FileIssue> getIssues(File file) {
        FileEntry entry = getEntry(file);
        if (entry == null) {
            return null;
        }
        
        return entry.getIssues();
    }
    
    /** */
    public void notifyFileChanged(File file) {
        synchronized (listeners) {
            listeners.iterationStart();
            for (FileListener listener:listeners) {
                listener.fileChanged(file);
            }
            listeners.iterationEnd();
        }        
    }
    
    /** */
    private void notifyFileIssuesChanged(FileEntry entry) {
        synchronized (listeners) {
            listeners.iterationStart();
            for (FileListener listener:listeners) {
                listener.fileIssuesChanged(entry.getFile(),entry.getIssues());
            }
            listeners.iterationEnd();
        }
    }
    
    /** */
    public void notifyFileCreated(File file) {
        synchronized (listeners) {
            listeners.iterationStart();
            for (FileListener listener:listeners) {
                listener.fileCreated(file);
            }
            listeners.iterationEnd();
        }             
    }    
    
    /** */
    public void notifyFileDeleted(File file) {
        synchronized (listeners) {
            listeners.iterationStart();
            for (FileListener listener:listeners) {
                listener.fileDeleted(file);
            }
            listeners.iterationEnd();
        }             
    }  
    
    /** */
    public void notifyDirCreated(File dir) {
        synchronized (listeners) {
            listeners.iterationStart();
            for (FileListener listener:listeners) {
                listener.dirCreated(dir);
            }
            listeners.iterationEnd();
        }             
    }     
    
    /** */
    public void notifyDirDeleted(File dir) {
        synchronized (listeners) {
            listeners.iterationStart();
            for (FileListener listener:listeners) {
                listener.dirDeleted(dir);
            }
            listeners.iterationEnd();
        }             
    }     
    
    /** */
    public void notifyFileRenamed(File src,File dst) {
        synchronized (listeners) {
            listeners.iterationStart();
            for (FileListener listener:listeners) {
                listener.fileRenamed(src,dst);
            }
            listeners.iterationEnd();
        }             
    }    
    
    /**
     * Sets the last-modified time of a time to the current time.
     *
     * @param file The file.
     */
    public void touch(File file) {
        file.setLastModified(System.currentTimeMillis());
    }
    
    /**
     * Creates a new file.
     *
     * @param file The file.
     * @param content The file content.
     * @throws IOException if the file already exists or cannot be created.     
     */
    public void createFile(File file,String content) throws IOException {
        if (file.exists() == true) {
            throw new IOException(res.getStr("file.exists",
                file.getAbsolutePath()));
        }
        
        if (content == null) {
            FileOutputStream stream = new FileOutputStream(file);
            stream.close();
        }
        else {
            FileUtils.writeStringToFile(file,content,"UTF-8");
        }
        
        notifyFileCreated(file);
    }
    
    /**
     * Creates a new file.
     *
     * @param file The file.
     * @throws IOException if the file already exists or cannot be created.
     */
    public void createFile(File file) throws IOException {
        createFile(file,null);
    }
    
    /**
     * Deletes a file.
     *
     * @param file The file.
     * @return <code>true</code> on success, <code>false</code> otherwise.
     * @throws IOException if the file cannot be deleted.
     */
    public boolean deleteFile(File file) {
        if (file.exists() == false) {
            return true;
        }
        
        boolean result = file.delete();
        if (result == true) {
            notifyFileDeleted(file);
        }
        
        return result;
    }
    
    /**
     * Creates a new directory.
     *
     * @param dir The directory.
     * @throws IOException if the directory already exists or cannot be created.
     */
    public void createDir(File dir) throws IOException {
        if (dir.exists() == true) {
            throw new IOException(res.getStr("dir.exists",
                dir.getAbsolutePath()));
        }
        
        dir.mkdirs();
        notifyDirCreated(dir);
    }   
    
    /** */
    private void deleteEmptyDir(File dir) {
        if (dir.exists() == false) {
            return;
        }
        dir.delete();
        notifyDirDeleted(dir);
    }
    
    /** 
     * Deletes a directory.
     *
     * @param dir The directory.
     * @throws IOException if the file cannot be deleted.
     */
    public void deleteDir(File dir) throws IOException {
        if (dir.exists() == false) {
            return;
        }
        
        DeleteWalker walker = new DeleteWalker(dir);
        walker.delete();
    }
    
    /**
     * Renames a file.
     *
     * @param src The source file.
     * @param dst The destination file.
     * @return <code>true</code> on success, <code>false</code> otherwise.
     */
    public boolean rename(File src,File dst) {
        boolean result = src.renameTo(dst);        
        if (result == true) {
            notifyFileRenamed(src,dst);
            touch(dst);
        }
        
        return result;
    }
    
    /** */
    private String generateTmpId() {
        String id = "";
        for (int index = 0; index < 8; index++) {
            int value = (int)(Math.random() * 26);
            id = id + (char)(value + 'a');
        }
        
        return id;
    }
    
    /** */
    public synchronized File getTmpFile(String name) {
        while (true) {
            File file = new File(tmpDir,generateTmpId() + "_" + name);
            if (file.exists() == false) {
                return file;
            }
        }
    }
    
    /** */
    public static File canonical(File file) {
        try {
            file = file.getCanonicalFile();
        } catch (IOException exception) {
        }        
        return file;
    }
    
    /** */
    public static boolean equal(File a,File b) {
        return canonical(a).equals(canonical(b));
    }
    
    /** */
    public static File[] listTree(File file) {
        List<File> files = new ArrayList<>();
        
        DirScanner scanner = new DirScanner(file);
    // for each descendant
        for (String path:scanner.build()) {
            files.add(new File(file,path));
        }
        
        return files.toArray(new File[]{});
    }
    
    /** */
    void init(File storageDir) {
        tmpDir = new File(storageDir,"tmp");
        if (tmpDir.exists() == false) {
            tmpDir.mkdirs();
        }
    }
    
    /** */
    public static Files get() {
        if (instance == null) {
            instance = new Files();
        }
        
        return instance;
    }
}