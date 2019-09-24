package com.andcreations.ae.studio.plugins.android;

import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class UninstallADBBuilder extends ExecBuilder {
    /** */
    static final String ID = "android.adb.uninstall";
    
    /** */
    private static BundleResources res =
        new BundleResources(UninstallADBBuilder.class);    
    		
	/** */
	UninstallADBBuilder() {
        super(ID,res.getStr("name"),Icons.getIcon(AndroidIcons.ANDROID),
            res.getStr("desc"));		
	}

	/** */
	@Override
	protected boolean canBuild() {
		String pckg = AndroidSettings.get().getAndroidPackage();
		if (pckg == null || pckg.isEmpty() == true) {
			CommonDialogs.error(res.getStr("no.package.title"),
				res.getStr("no.package.msg"));
			return false;
		}
		return true;
	}
	
	/** */
	@Override
	protected String[] getArgs() {
		String pckg = AndroidSettings.get().getAndroidPackage();
		return new String[]{"adb","uninstall",pckg};
	}
}
