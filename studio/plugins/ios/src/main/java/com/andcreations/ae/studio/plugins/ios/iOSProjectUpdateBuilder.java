package com.andcreations.ae.studio.plugins.ios;

import com.andcreations.ae.sdk.update.ios.iOSUpdater;
import com.andcreations.ae.studio.plugins.console.DefaultConsole;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class iOSProjectUpdateBuilder extends iOSUpdaterBuilder {
    /** */
    private static final String ID = "ios.update";
    
    /** */
    private static BundleResources res =
        new BundleResources(iOSProjectUpdateBuilder.class);
    
    /** */
    iOSProjectUpdateBuilder(iOSPluginState state) {
        super(ID,res.getStr("name"),Icons.getIcon(iOSIcons.IOS),
            res.getStr("desc"),state);
    }
    
    /** */
    @Override
    protected void runUpdater(iOSUpdater updater)
        throws Exception {
    //
        updater.update();
        DefaultConsole.get().println(res.getStr("updated"));
    }
}