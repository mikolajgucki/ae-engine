package com.andcreations.ae.studio.plugins.android;

import com.andcreations.ae.sdk.update.android.AndroidUpdater;
import com.andcreations.ae.studio.plugins.console.DefaultConsole;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class AndroidAEProjectUpdateBuilder extends AndroidAEUpdaterBuilder {
    /** */
    static final String ID = "android.ae.update";
    
    /** */
    private static BundleResources res =
        new BundleResources(AndroidAEProjectUpdateBuilder.class);
    
    /** */
    AndroidAEProjectUpdateBuilder(AndroidPluginState state) {
        super(ID,res.getStr("name"),Icons.getIcon(AndroidIcons.ANDROID),
            res.getStr("desc"),state);
    }
    
    /** */
    @Override
    protected void runUpdater(AndroidUpdater updater)
        throws Exception {
    //
        updater.update();
        DefaultConsole.get().println(res.getStr("updated"));
    }
}