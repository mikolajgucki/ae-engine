package com.andcreations.ae.studio.plugins.file.transfer;

import java.io.File;
import java.util.List;

import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.ui.common.MessageDialog;
import com.andcreations.ae.studio.plugins.ui.common.OptionDialog.Option;
import com.andcreations.ae.studio.plugins.ui.common.OptionDialog.OptionHolder;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
abstract class FileTransferRunnable implements Runnable {
    /** */
    private BundleResources res =
        new BundleResources(FileTransferRunnable.class);
    
    /** */  
    private String title;
        
    /** */
    private FileTransferDialog transferDialog;
    
    /** */
    private OverwriteDialog overwriteDialog;
    
    /** */
    private List<FileToTransfer> filesToTransfer;
    
    /** */
    private File dstDir;
    
    /** */
    private boolean cancelFlag;
    
    /** */
    private boolean yesToAll;
    
    /** */
    FileTransferRunnable(FileTransferDialog transferDialog,
        List<FileToTransfer> filesToTransfer,File dstDir) {
    //
        this.transferDialog = transferDialog;
        this.filesToTransfer = filesToTransfer;
        this.dstDir = dstDir;
    }
    
    /** */
    protected void setTitle(String title) {
        this.title = title;
    }
    
    /** */
    protected FileTransferDialog getTransferDialog() {
        return transferDialog;
    }
    
    /** */
    private OverwriteDialog getOverwriteDialog() {
        if (overwriteDialog == null) {
            overwriteDialog = new OverwriteDialog(transferDialog,title);
        }
        
        return overwriteDialog;
    }
    
    /** */
    @Override
    public void run() {
        int count = 0;
    // for each file to copy
        for (final FileToTransfer fileToTransfer:filesToTransfer) {
            File dstFile = new File(dstDir,fileToTransfer.getPath());
            final String dstPath = dstFile.getAbsolutePath();
            
        // replace file?
            if (fileToTransfer.didDstFileExist() == true &&
                dstFile.isFile() == true) {
            //
                if (yesToAll == false) {
                    final OptionHolder holder = new OptionHolder();
                    UICommon.invokeAndWait(new Runnable() {
                        /** */
                        @Override
                        public void run() {
                            OverwriteDialog dialog = getOverwriteDialog();
                            Option option = dialog.show(
                                res.getStr("overwrite.msg",dstPath));
                            holder.setOption(option);
                        }
                    });
                    
                    Option option = holder.getOption();
                    if (option == Option.NO) {
                        continue;
                    }
                    if (option == Option.CANCEL) {
                        break;
                    }
                    if (option == Option.YES_TO_ALL) {
                        yesToAll = true;
                    }
                }
                
            // delete the file to replace
                if (Files.get().deleteFile(dstFile) == false) {
                    MessageDialog.error(transferDialog,title,
                        res.getStr("delete.error.msg",dstPath));
                    break;
                }
                
            // consider as if the file didn't exist if overwriting
                fileToTransfer.clearDstFileExisted();
            }            
            
        // transfer
            transfer(fileToTransfer);
            count++;
            
        // update dialog
            final double progress = (double)count /
                (filesToTransfer.size() - 1);
            UICommon.invokeAndWait(new Runnable() {
                /** */
                @Override
                public void run() {
                    transferDialog.setInfo(fileToTransfer.getPath());
                    transferDialog.setProgress(progress);
                }
            });
            
        // cancel?
            if (cancelFlag == true) {
                break;
            }
        }
        
        transferFinished();
    }
    
    /** */
    void cancelTransfer() {
        cancelFlag = true;
    }
    
    /** */
    protected void transferFinished() {
        transferDialog.closeFileTransferDialog();
    }
    
    /** */
    protected abstract void transfer(FileToTransfer fileToTransfer);   
}