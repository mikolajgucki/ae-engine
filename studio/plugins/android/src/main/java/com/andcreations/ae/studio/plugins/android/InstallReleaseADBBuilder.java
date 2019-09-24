package com.andcreations.ae.studio.plugins.android;

import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class InstallReleaseADBBuilder extends ExecBuilder {
    /** */
    static final String ID = "android.adb.install.release";
    
    /** */
    private static BundleResources res =
        new BundleResources(InstallReleaseADBBuilder.class);    
    		
	/** */
	InstallReleaseADBBuilder() {
        super(ID,res.getStr("name"),Icons.getIcon(AndroidIcons.ANDROID),
            res.getStr("desc"));		
	}

	/** */
	@Override
	protected String[] getArgs() {
		return new String[]{
			"adb","install","-r","build/outputs/apk/android-release.apk"};
	}
}
