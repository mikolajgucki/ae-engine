package com.andcreations.ae.studio.plugins.ant;

import java.io.File;

import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class AntHome {
    /** */
    private static BundleResources res = new BundleResources(AntHome.class);    
    
    /** */
    public static String validate(File antHome) {
        if (antHome == null) {
            return res.getStr("not.set");
        }
        if (antHome.exists() == false) {
            return res.getStr("not.found");
        }
        if (antHome.isDirectory() == false) {
            return res.getStr("not.dir");
        }        
        
        return null;
    }
}