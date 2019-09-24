package com.andcreations.ae.studio.plugin.manager;

/**
 * The plugin manager configuration.
 * 
 * @author Mikolaj Gucki
 */
public class PluginManagerCfg {
    /** */
    private String[] pluginDirs;
    
    /** */
    private String storageDir;
    
    /**
     * Constructs a {@link PluginManagerCfg}.
     *  
     * @param pluginDirs The directories with the plugins.
     * @param storageDir The storage directory for application state.
     */
    public PluginManagerCfg(String[] pluginDirs,String storageDir) {
        this.pluginDirs = pluginDirs;
        this.storageDir = storageDir;
    }
    
    /** */
    public String[] getPluginDirs() {
        return pluginDirs;
    }
    
    /** */
    public String getStorageDir() {
        return storageDir;
    }
}
