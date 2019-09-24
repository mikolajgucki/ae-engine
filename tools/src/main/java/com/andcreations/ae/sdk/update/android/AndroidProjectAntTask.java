package com.andcreations.ae.sdk.update.android;

import java.io.File;

import com.andcreations.ae.sdk.update.SDKUpdaterListener;
import com.andcreations.ant.AETask;
import com.andcreations.ant.AntPath;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
abstract class AndroidProjectAntTask extends AETask {
    /** */
    private class AndroidSDKUpdaterListener implements SDKUpdaterListener {
        /** */
        @Override
        public void log(String msg) {
            verbose(msg);
        }
        
        /** */
        @Override
        public void copying(File srcFile,File dstFile) {
            copyCount++;
            verbose(res.getStr("copying",srcFile.getAbsolutePath(),
                dstFile.getAbsolutePath()));
        }
        
        /** */
        @Override
        public void deleting(File file) {
            deleteCount++;
            verbose(res.getStr("deleting",file.getAbsolutePath()));
        }
        
        /** */
        @Override
        public void skipping(File srcFile,File dstFile) {
        }                
        
        /** */
        @Override
        public void updatingIcon(File file) {
            verbose(res.getStr("updating.icon",file.getAbsolutePath()));
        }
    }
    
    /** */
    private BundleResources res =
        new BundleResources(AndroidProjectAntTask.class);   
    
    /** */
    private AntPath aeDistDir;
    
    /** */
    private AntPath projectDir;
    
    /** */
    private AntPath androidProjectDir;
    
    /** */
    private int copyCount;
    
    /** */
    private int deleteCount;
    
    /** */
    public AntPath createAEDistDir() {
        if (aeDistDir != null) {
            duplicatedElement("aedistdir");
        }
        
        aeDistDir = new AntPath();
        return aeDistDir;
    }    
    
    /** */
    public AntPath createProjectDir() {
        if (projectDir != null) {
            duplicatedElement("projectdir");
        }
        
        projectDir = new AntPath();
        return projectDir;
    } 
    
    /** */
    public AntPath createAndroidProjectDir() {
        if (androidProjectDir != null) {
            duplicatedElement("androidprojectdir");
        }
        
        androidProjectDir = new AntPath();
        return androidProjectDir;
    }  
    
    /** */
    protected File getAEDistDir() {
        return aeDistDir.getFile();
    }
    
    /** */
    protected File getProjectDir() {
        return projectDir.getFile();
    }
    
    /** */
    protected File getAndroidProjectDir() {
        return androidProjectDir.getFile();
    }
     
    /** */
    protected int getCopiedFileCount() {
        return copyCount;
    }
    
    /** */
    protected int getDeletedFileCount() {
        return deleteCount;
    }
    
    /** */
    protected SDKUpdaterListener getSDKUpdaterListener() {
        return new AndroidSDKUpdaterListener();
    }

    /** */
    abstract void run();
    
    /** */
    @Override
    public void execute() {
        verifyElementSet(aeDistDir,"aedistdir");
        verifyElementSet(projectDir,"projectdir");
        verifyElementSet(androidProjectDir,"androidprojectdir");
        run();
    }    
}