package com.andcreations.ae.sdk.update.ios;

import java.io.IOException;

import org.apache.tools.ant.BuildException;

import com.andcreations.ae.sdk.update.SDKUpdateException;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class UpdateiOSProjectAntTask extends iOSProjectAntTask {
    /** */
    private BundleResources res =
        new BundleResources(UpdateiOSProjectAntTask.class);
        
    /** */
    @Override
    void run(){
        if (getAppIconSetDir() == null) {
            verbose(res.getStr("no.app.icon.set.dir"));
        }
        
        iOSUpdateCfg cfg = new iOSUpdateCfg(
            getAEDistDir(),getProjectDir(),getiOSProjectDir());
        cfg.setAppIconSetDir(getAppIconSetDir());
        iOSUpdater updater = new iOSUpdater(cfg);
        updater.setSDKUpdaterListener(getSDKUpdaterListener());
        try {
            updater.update();
        } catch (IOException exception) {
            throw new BuildException(exception);
        } catch (SDKUpdateException exception) {
            throw new BuildException(exception);
        }

        log(res.getStr("summary.files",Integer.toString(getCopiedFileCount()),
            Integer.toString(getDeletedFileCount())));        
    }    
}