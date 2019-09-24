package com.andcreations.ae.studio.plugins.android;

import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.resources.BundleResources;
import com.andcreations.system.OS;

/**
 * @author Mikolaj Gucki
 */
class CleanGradleBuilder extends ExecBuilder {
    /** */
    static final String ID = "android.gradle.clean";
    
    /** */
    private static BundleResources res =
        new BundleResources(CleanGradleBuilder.class);    
    		
	/** */
	CleanGradleBuilder() {
        super(ID,res.getStr("name"),Icons.getIcon(AndroidIcons.ANDROID),
            res.getStr("desc"));		
	}

	/** */
	@Override
	protected String[] getArgs() {
		if (OS.getOS() == OS.WINDOWS) {
			return new String[]{"cmd","/c","gradlew.bat","clean"};
		}
		return new String[]{"gradlew","clean"};
	}
}
