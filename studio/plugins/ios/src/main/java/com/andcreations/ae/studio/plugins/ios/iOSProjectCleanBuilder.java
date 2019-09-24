package com.andcreations.ae.studio.plugins.ios;

import com.andcreations.ae.sdk.update.ios.iOSUpdater;
import com.andcreations.ae.studio.plugins.console.DefaultConsole;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class iOSProjectCleanBuilder extends iOSUpdaterBuilder {
    /** */
    private static final String ID = "ios.clean";
    
    /** */
    private static BundleResources res =
        new BundleResources(iOSProjectCleanBuilder.class);
    
    /** */
    iOSProjectCleanBuilder(iOSPluginState state) {
        super(ID,res.getStr("name"),
            Icons.getIcon(iOSIcons.IOS,DefaultIcons.DECO_ERASER),
            res.getStr("desc"),state);
    }
    
    /** */
    @Override
    protected void runUpdater(iOSUpdater updater)
        throws Exception {
    //
        updater.clean();
        DefaultConsole.get().println(res.getStr("cleaned"));
    }
}