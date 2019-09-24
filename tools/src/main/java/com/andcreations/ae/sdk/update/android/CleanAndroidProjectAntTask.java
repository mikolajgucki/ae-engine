package com.andcreations.ae.sdk.update.android;

import java.io.IOException;

import org.apache.tools.ant.BuildException;

import com.andcreations.ae.sdk.update.SDKUpdateException;

/**
 * @author Mikolaj Gucki
 */
public class CleanAndroidProjectAntTask extends AndroidProjectAntTask {
    /** */
    @Override
    void run() {
        AndroidUpdateCfg cfg = new AndroidUpdateCfg(
            getAEDistDir(),getProjectDir(),getAndroidProjectDir());
        AndroidUpdater updater = new AndroidUpdater(cfg);
        updater.setSDKUpdaterListener(getSDKUpdaterListener());
        try {
            updater.clean();
        } catch (IOException exception) {
            throw new BuildException(exception);
        } catch (SDKUpdateException exception) {
            throw new BuildException(exception);
        }
    }
}