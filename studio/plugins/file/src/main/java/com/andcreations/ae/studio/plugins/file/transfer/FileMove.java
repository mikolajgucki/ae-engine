package com.andcreations.ae.studio.plugins.file.transfer;

import java.io.File;
import java.util.List;

import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class FileMove {
    /** */
    private BundleResources res = new BundleResources(FileMove.class);  
    
    /** */
    private FileTransferDialog dialog;    

    /** */
    FileMove() {
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
    void move(List<File> files,File dstDir) {
        for (File file:files) {
            if (file.equals(dstDir) == true) {
            // cannot move a directory to itself
                return;
            }
        }
        if (dstDir.isDirectory() == false) {
            return;
        }
        List<FileToTransfer> filesToTransfer =
            FileToTransfer.getFilesToTransfer(files,dstDir);
        
    // thread
        FileMoveRunnable runnable = new FileMoveRunnable(
            dialog,filesToTransfer,dstDir,files);
        Thread thread = new Thread(runnable,"MoveFiles");
        thread.start();
        
    // dialog
        if (dialog.showFileTransferDialog() == true) {
            runnable.cancelTransfer();
        }
    }
}