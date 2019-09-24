package com.andcreations.android.adb;

import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public interface ADBLogCatListener {
    /** */
    void log(List<ADBLogCatMessage> messages);
}