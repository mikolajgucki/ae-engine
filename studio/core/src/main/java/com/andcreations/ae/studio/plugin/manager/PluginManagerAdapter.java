package com.andcreations.ae.studio.plugin.manager;

/**
 * @author Mikolaj Gucki
 */
public class PluginManagerAdapter implements PluginManagerListener {
    /** */
    @Override
    public void failedToStartPlugins(Exception exception) {
    }
    
    /** */
    @Override
    public void startingPlugin(PluginDesc pluginDesc,int index,int count) {
    }
    
    /** */
    @Override
    public void failedToStartPlugin(PluginDesc plugin,Exception exception) {
    }
    
    /** */
    @Override
    public void pluginsStarted() {
    }
}