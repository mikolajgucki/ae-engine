package com.andcreations.ae.studio.plugins.android;

import java.io.File;

/**
 * @author Mikolaj Gucki
 */
public interface AndroidPreferencesListener {
    /** */
    void androidSDKDirChanged(File oldSDKDir,File newSDKDir);
}