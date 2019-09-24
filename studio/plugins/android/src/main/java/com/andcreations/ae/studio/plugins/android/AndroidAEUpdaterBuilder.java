package com.andcreations.ae.studio.plugins.android;

import java.io.File;

import javax.swing.ImageIcon;

import com.andcreations.ae.sdk.update.SDKUpdaterListener;
import com.andcreations.ae.sdk.update.android.AndroidUpdateCfg;
import com.andcreations.ae.sdk.update.android.AndroidUpdater;
import com.andcreations.ae.studio.plugins.ae.dist.AEDist;
import com.andcreations.ae.studio.plugins.builders.AbstractBuilder;
import com.andcreations.ae.studio.plugins.console.DefaultConsole;
import com.andcreations.ae.studio.plugins.project.files.ProjectFiles;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
abstract class AndroidAEUpdaterBuilder extends AbstractBuilder {
    /** */
    private static BundleResources res =
        new BundleResources(AndroidAEUpdaterBuilder.class);
        
    /** */
    private AndroidPluginState state;
    
    /** */
    protected AndroidAEUpdaterBuilder(String id,String name,ImageIcon icon,
        String desc,AndroidPluginState state) {
    //
        super(id,name,icon,desc);
        this.state = state;
    }
    
    /** */
    protected AndroidPluginState getAndroidPluginState() {
        return state;
    }
    
    /** */
    private SDKUpdaterListener getSDKUpdaterListener() {
        return new SDKUpdaterListener() {
            /** */
            @Override
            public void log(String msg) {
                DefaultConsole.get().traceln(msg);
            }
            
            /** */
            @Override
            public void copying(File srcFile,File dstFile) {
                DefaultConsole.get().traceln(res.getStr("copying",
                    ProjectFiles.get().getRelativePath(srcFile),
                    ProjectFiles.get().getRelativePath(dstFile)));
            }
            
            /** */
            @Override
            public void deleting(File file) {
                DefaultConsole.get().traceln(res.getStr("deleting",
                    ProjectFiles.get().getRelativePath(file)));
            }
            
            /** */
            @Override
            public void skipping(File srcFile,File dstFile) {
            }
            
            /** */
            @Override
            public void updatingIcon(File file) {
                DefaultConsole.get().traceln(res.getStr("updating.icon",
                    ProjectFiles.get().getRelativePath(file)));
            }
        };
    }
    
    /** */
    protected abstract void runUpdater(AndroidUpdater updater)
        throws Exception;
    
    /** */
    protected void run(File androidProjectDir) {
        AndroidUpdateCfg cfg = new AndroidUpdateCfg(
            AEDist.get().getAEDistDir(),ProjectFiles.get().getProjectDir(),
            androidProjectDir);
        AndroidUpdater updater = new AndroidUpdater(cfg);
        if (state.getVerbose() == true) {
            updater.setSDKUpdaterListener(getSDKUpdaterListener());
        }
        
        try {
            runUpdater(updater);
        } catch (Exception exception) {
            exception.printStackTrace();
            CommonDialogs.error(res.getStr("error.title"),
                res.getStr("failed.to.update",exception.getMessage()));
            return;            
        }
    }
    
    /** */
    @Override
    public void build() {
    // get directory
        File androidProjectDir = AndroidProjectDir.get().tryToGet();
        if (androidProjectDir == null) {
            return;
        }
        
    // run Android updater
        run(androidProjectDir);
    }
}