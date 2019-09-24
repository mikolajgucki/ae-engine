package com.andcreations.ae.studio.plugins.splash;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.StartBeforeAndRequire;
import com.andcreations.ae.studio.plugin.manager.PluginDesc;
import com.andcreations.ae.studio.plugin.manager.PluginManager;
import com.andcreations.ae.studio.plugin.manager.PluginManagerAdapter;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;

/**
 * @author Mikolaj Gucki
 */
@StartBeforeAndRequire(id={
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ui.common"
})
public class SplashPlugin extends Plugin<PluginState> {
    /** */
    private Splash splash;
    
    /** */
    @Override
    public void start() throws PluginException {
        if (System.getProperty("ae.no.splash") != null) {
            return;
        }
        
        createSplash();
        createPluginManagerListener();
    }
    
    /** */
    private void createSplash() {
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                splash = new Splash();
                splash.show();
            }
        });
    }
    
    /** */
    private void createPluginManagerListener() {
        PluginManager.addPluginManagerListener(new PluginManagerAdapter() {
            /** */
            public void startingPlugin(PluginDesc pluginDesc,int index,
                int count) {
            //
                splash.setProgress("",//pluginDesc.getId(),
                    (double)index / (count - 1));
            }
            
            /** */
            public void pluginsStarted() {
                splash.hide();
            }                
        });
    }
}