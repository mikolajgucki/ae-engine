package com.andcreations.ae.studio.plugins.file.transfer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.ui.common.MessageDialog;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class FileMoveRunnable extends FileTransferRunnable {
    /** */
    private BundleResources res = new BundleResources(FileMoveRunnable.class);    
    
    /** */
    private List<File> files;
    
    /** */
    FileMoveRunnable(FileTransferDialog transferDialog,
        List<FileToTransfer> filesToTransfer,File dstDir,List<File> files) {
    //
        super(transferDialog,filesToTransfer,dstDir);
        this.files = files;
        setTitle(res.getStr("title"));
    }
    
    /** */
    @Override
    protected void transfer(FileToTransfer file) {
        File srcFile = file.getSrcFile();
        File dstFile = file.getDstFile();
        
    // move if the file doesn't exist and didn't exist before moving
        if (dstFile.exists() == false && file.didDstFileExist() == false) {
            if (Files.get().rename(srcFile,dstFile) == false) {
                MessageDialog.error(getTransferDialog(),res.getStr("title"),
                    res.getStr("rename.failed.msg",srcFile.getAbsolutePath()));
                cancelTransfer();
            }
        }
    }    
    
    /** */
    @Override
    protected void transferFinished() {
    // delete the source files/directories if remained
        for (File file:files) {
            if (file.exists() == false) {
                continue;
            }
            if (file.isFile() == true) {
                Files.get().deleteFile(file);
            }
            else if (file.isDirectory() == true) {
                try {
                    Files.get().deleteDir(file);
                } catch (IOException exception) {
                    MessageDialog.error(getTransferDialog(),
                        res.getStr("title"),
                        res.getStr("rename.failed.msg",
                        file.getAbsolutePath()));                    
                }
            }
        }
        
        super.transferFinished();
    }
}