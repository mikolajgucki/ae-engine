package com.andcreations.ae.studio.plugins.file.transfer;

import java.io.File;
import java.util.List;

import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class FileCopy {
    /** */
    private BundleResources res = new BundleResources(FileCopy.class);
    
    /** */
    private FileTransferDialog dialog;
    
    /** */
    FileCopy() {
        create();
    }
    
    /** */
    private void create() {
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                dialog = new FileTransferDialog();
                dialog.setTitle(res.getStr("dialog.title"));
            }
        });
    }
    
    /** */
    void copy(List<File> files,final File dstDir) {
        if (dstDir.isDirectory() == false) {
            return;
        }
        
        final List<FileToTransfer> filesToCopy =
            FileToTransfer.getFilesToTransfer(files,dstDir);
    // thread
        FileCopyRunnable runnable = new FileCopyRunnable(
            dialog,filesToCopy,dstDir);
        Thread thread = new Thread(runnable,"CopyFiles");
        thread.start();
        
    // dialog
        if (dialog.showFileTransferDialog() == true) {
            runnable.cancelTransfer();
        }
    }
}