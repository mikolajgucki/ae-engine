package com.andcreations.ae.studio.plugins.android;

import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.resources.BundleResources;
import com.andcreations.system.OS;

/**
 * @author Mikolaj Gucki
 */
class AssembleDebugGradleBuilder extends ExecBuilder {
    /** */
    static final String ID = "android.gradle.assemble.debug";
    
    /** */
    private static BundleResources res =
        new BundleResources(AssembleDebugGradleBuilder.class);    
    		
	/** */
	AssembleDebugGradleBuilder() {
        super(ID,res.getStr("name"),Icons.getIcon(AndroidIcons.ANDROID),
            res.getStr("desc"));		
	}

	/** */
	@Override
	protected String[] getArgs() {
		if (OS.getOS() == OS.WINDOWS) {
			return new String[]{"cmd","/c","gradlew.bat","assembleDebug"};
		}
		return new String[]{"gradlew","assembleDebug"};
	}
}
