package com.andcreations.ae.studio.plugins.file.transfer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.andcreations.io.DirScanner;

/**
 * @author Mikolaj Gucki
 */
class FileToTransfer {
    /** */
    private File srcFile;
    
    /** */
    private String path;
    
    /** */
    private File dstFile;
    
    /** */
    private boolean dstFileExisted;
    
    /** */
    private FileToTransfer(File srcFile,String path,File dstDir) {
        this.srcFile = srcFile;
        this.path = path;
        
        this.dstFile = new File(dstDir,path);
        this.dstFileExisted = this.dstFile.exists();        
    }
    
    /** */
    File getSrcFile() {
        return srcFile;
    }
    
    /** */
    String getPath() {
        return path;
    }
    
    /** */
    File getDstFile() {
        return dstFile;
    }
    
    /** */
    boolean didDstFileExist() {
        return dstFileExisted;
    }
    
    /** */
    void clearDstFileExisted() {
        dstFileExisted = false;
    }
    
    /** */
    private static void sort(List<FileToTransfer> filesToTransfer) {
        Collections.sort(filesToTransfer,new Comparator<FileToTransfer>() {
            /** */
            @Override
            public int compare(FileToTransfer a,FileToTransfer b) {
                return a.getPath().compareTo(b.getPath());
            }
        });
    }
    
    /** */
    private static List<FileToTransfer> getFilesToTransfer(File srcDir,
        File dstDir) {
    //
        List<FileToTransfer> filesToTransfer = new ArrayList<>();
        
    // the input directory itself
        filesToTransfer.add(new FileToTransfer(srcDir,srcDir.getName(),dstDir));
            
        DirScanner scanner = new DirScanner(srcDir,true);
    // for each file/directory contained in the input directory
        for (String path:scanner.build()) {
            String dstPath = srcDir.getName() + File.separator + path;
            filesToTransfer.add(new FileToTransfer(
                new File(srcDir,path),dstPath,dstDir));
        }
        
        return filesToTransfer;
    }    
    
    /** */
    static List<FileToTransfer> getFilesToTransfer(
        List<File> files,File dstDir) {
    //
        List<FileToTransfer> filesToTransfer = new ArrayList<>();
        
    // for each file
        for (File file:files) {
            if (file.isFile() == true) {
                filesToTransfer.add(new FileToTransfer(
                    file,file.getName(),dstDir));
            }
            else if (file.isDirectory() == true) {
                filesToTransfer.addAll(getFilesToTransfer(file,dstDir));
            }
        }
        
        sort(filesToTransfer);
        return filesToTransfer;
    }    
}