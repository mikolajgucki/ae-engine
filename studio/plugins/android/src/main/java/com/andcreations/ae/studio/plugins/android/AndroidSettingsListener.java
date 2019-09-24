package com.andcreations.ae.studio.plugins.android;


/**
 * @author Mikolaj Gucki
 */
public interface AndroidSettingsListener {
    /** */
    void androidProjectDirChanged(String dir);
    
    /** */
    void androidPackageChanged(String androidPackage);
}