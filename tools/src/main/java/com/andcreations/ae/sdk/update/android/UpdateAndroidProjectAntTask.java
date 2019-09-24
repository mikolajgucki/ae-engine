package com.andcreations.ae.sdk.update.android;

import java.io.IOException;

import org.apache.tools.ant.BuildException;

import com.andcreations.ae.sdk.update.SDKUpdateException;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class UpdateAndroidProjectAntTask extends AndroidProjectAntTask {
    /** */
    private BundleResources res =
        new BundleResources(UpdateAndroidProjectAntTask.class);  
        
    /** */
    @Override
    void run() {
        AndroidUpdateCfg cfg = new AndroidUpdateCfg(
            getAEDistDir(),getProjectDir(),getAndroidProjectDir());
        AndroidUpdater updater = new AndroidUpdater(cfg);
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