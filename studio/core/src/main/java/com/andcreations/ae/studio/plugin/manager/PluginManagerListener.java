package com.andcreations.ae.studio.plugin.manager;

/**
 * @author Mikolaj Gucki
 */
public interface PluginManagerListener {
    /** */
    void failedToStartPlugins(Exception exception);
    
    /** */
    void startingPlugin(PluginDesc pluginDesc,int index,int count);
    
    /** */
    void failedToStartPlugin(PluginDesc plugin,Exception exception);
    
    /** */
    void pluginsStarted();
}