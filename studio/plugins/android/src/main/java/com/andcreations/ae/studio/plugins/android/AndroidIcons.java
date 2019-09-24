package com.andcreations.ae.studio.plugins.android;

import com.andcreations.ae.studio.plugins.ui.icons.IconSource;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.icons.ResourceIconSource;

/**
 * @author Mikolaj Gucki
 */
public class AndroidIcons {
    /** */
    public static final String ANDROID = "android";
    
    /** */
    public static final String ANDROID_DEVICE = "android_device";
    
    /** */
    public static final String ANDROID_DEVICES = "android_devices";
    
    /** */
    public static final String ANDROID_DEVICES_WARNING =
        "android_devices_warning";
    
    /** */
    public static final String DECO_ANDROID = "deco_android";
        
    /** */
    static void init() {
    // source
        IconSource iconSource =
            new ResourceIconSource(AndroidIcons.class,"resources/");       
        Icons.addIconSource(iconSource);
        
    // colorize
        Icons.colorize(ANDROID,Icons.green());
        Icons.colorize(ANDROID_DEVICE,Icons.green());
        Icons.colorize(ANDROID_DEVICES,Icons.green());
        Icons.colorize(ANDROID_DEVICES_WARNING,Icons.orange());
        Icons.colorize(DECO_ANDROID,Icons.green());
    }
}