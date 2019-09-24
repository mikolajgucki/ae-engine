package com.andcreations.ae.studio.launcher;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugin.manager.PluginDesc;
import com.andcreations.ae.studio.plugin.manager.PluginManager;
import com.andcreations.ae.studio.plugin.manager.PluginManagerCfg;
import com.andcreations.ae.studio.plugin.manager.PluginManagerAdapter;
import com.andcreations.resources.BundleResources;

/**
 * Start AE Studio.
 *
 * @author Mikolaj Gucki
 */
public class Main {
    /** */
    static final String PROPERTY_PLUGINS = "ae.studio.plugins.dirs";
    
    /** */
    static final char PROPERTY_PLUGINS_SEPARATOR = ';';
    
    /** */
    static final String PROPERTY_STORAGE = "ae.studio.storage.dir";
    
    /** */
    private static final BundleResources res =
        new BundleResources(Main.class);

    /** */
    private Main() {
    }
    
    /** */
    private void logEnv() {
        StringBuilder env = new StringBuilder();
        env.append("Environment variables:\n");
        for (String key:System.getenv().keySet()) {
            env.append(String.format("  %s=%s\n",key,System.getenv(key)));
        }
        
        Log.info(env.toString());
    }
    
    /** */
    private void addPluginManagerListener() {
        PluginManager.addPluginManagerListener(new PluginManagerAdapter() {
            /** */
            @Override
            public void failedToStartPlugins(Exception exception) {
                Log.fatal("Failed to start plugins:",exception);
            }

            /** */
            @Override
            public void failedToStartPlugin(PluginDesc plugin,
                Exception exception) {
            //
                Log.fatal(String.format("Failed to start plugin:",
                    plugin.getId()),exception);
            }            
        });
    }
    
    /** */
    private void run() {
        logEnv();
        addPluginManagerListener();
        
    // directory with the plugins
        String pluginsDir = System.getProperty(PROPERTY_PLUGINS);
        if (pluginsDir == null) {
            Log.fatal(res.getStr("missing.plugins.dir",PROPERTY_PLUGINS));
            System.exit(-1);
        }
        String[] pluginsDirs = pluginsDir.split("" + PROPERTY_PLUGINS_SEPARATOR);
        
    // storage directory
        String storageDir = System.getProperty(PROPERTY_STORAGE);
        if (storageDir == null) {
            Log.fatal(res.getStr("missing.storage.dir",PROPERTY_STORAGE));
            System.exit(-1);            
        }
        
        Log.info(res.getStr("plugins.dirs"));
        for (String pluginDir:pluginsDirs) {
            Log.info(res.getStr("plugin.dir",pluginDir));
        }
        
    // start
        PluginManagerCfg cfg = new PluginManagerCfg(pluginsDirs,storageDir);
        PluginManager.start(cfg);
    }
    
    /** */
    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }
}