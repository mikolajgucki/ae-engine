package com.andcreations.ae.sdk.update.ios;

import java.io.IOException;

import org.apache.tools.ant.BuildException;

import com.andcreations.ae.sdk.update.SDKUpdateException;

/**
 * @author Mikolaj Gucki
 */
public class CleaniOSProjectAntTask extends iOSProjectAntTask {
    /** */
    @Override
    void run(){
        iOSUpdateCfg cfg = new iOSUpdateCfg(
            getAEDistDir(),getProjectDir(),getiOSProjectDir());
        iOSUpdater updater = new iOSUpdater(cfg);
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