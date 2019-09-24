package com.andcreations.ae.studio.plugins.android;

import com.andcreations.ae.sdk.update.android.AndroidUpdater;
import com.andcreations.ae.studio.plugins.console.DefaultConsole;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class AndroidAEProjectCleanBuilder extends AndroidAEUpdaterBuilder {
    /** */
    static final String ID = "android.ae.clean";
    
    /** */
    private static BundleResources res =
        new BundleResources(AndroidAEProjectCleanBuilder.class);
    
    /**
     * @param state  */
    AndroidAEProjectCleanBuilder(AndroidPluginState state) {
        super(ID,res.getStr("name"),
            Icons.getIcon(AndroidIcons.ANDROID,DefaultIcons.DECO_ERASER),
            res.getStr("desc"),state);
    }
    
    /** */
    @Override
    protected void runUpdater(AndroidUpdater updater)
        throws Exception {
    //
        updater.clean();
        DefaultConsole.get().println(res.getStr("cleaned"));
    }
}