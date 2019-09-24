package com.andcreations.ae.studio.plugins.file.transfer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.ui.common.MessageDialog;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class FileCopyRunnable extends FileTransferRunnable {
    /** */
    private BundleResources res = new BundleResources(FileCopyRunnable.class);
    
    /** */
    FileCopyRunnable(FileTransferDialog transferDialog,
        List<FileToTransfer> filesToTransfer,File dstDir) {
    //
        super(transferDialog,filesToTransfer,dstDir);
        setTitle(res.getStr("title"));
    }    
    
    /** */
    @Override
    protected void transfer(FileToTransfer file) {
        File srcFile = file.getSrcFile();
        File dstFile = file.getDstFile();
        String dstPath = dstFile.getAbsolutePath();
        
        if (srcFile.isFile() == true) {
            boolean dstFileExists = dstFile.exists(); 
            try {
                FileUtils.copyFile(srcFile,dstFile);
            } catch (IOException exception) {
                MessageDialog.error(getTransferDialog(),
                    res.getStr("copy.error.title"),
                    res.getStr("copy.error.msg",dstPath,
                    exception.getMessage()));
                cancelTransfer();
                return;
            }
            
            Files.get().touch(dstFile);
            if (dstFileExists == true) {
                Files.get().notifyFileChanged(dstFile);
            }
            else {
                Files.get().notifyFileCreated(dstFile);
            }
        }
    // create directory
        else if (srcFile.isDirectory() == true && dstFile.exists() == false) {
            if (dstFile.mkdirs() == false) {                    
                MessageDialog.error(getTransferDialog(),
                    res.getStr("mkdirs.error.title"),
                    res.getStr("mkdirs.error.msg",dstPath));
                cancelTransfer();
                return;
            }
            Files.get().notifyDirCreated(dstFile);
        }        
    }     
}