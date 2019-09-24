package com.andcreations.ae.studio.plugins.adb;

import com.andcreations.android.adb.ADBDevice;

/**
 * @author Mikolaj Gucki
 */
interface LogCatViewComponentListener {
    /** */
    void adbDeviceSelected(ADBDevice device);
}