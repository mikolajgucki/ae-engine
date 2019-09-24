package com.andcreations.ae.studio.plugins.android;


/**
 * @author Mikolaj Gucki
 */
interface AndroidAppStateListener {
    /** */
    void androidSDKDirChanged(String oldSDKDir,String newSDKDir);
}