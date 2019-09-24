package com.andcreations.ae.studio.plugin;

import java.io.File;

import com.andcreations.ae.studio.state.AppState;
import com.andcreations.io.json.JSONException;

/**
 * The plugin superclass.
 *
 * @author Mikolaj Gucki
 */
public abstract class Plugin<TState extends PluginState> {
    /** The plugin storage directory. */
    private File storageDir;
    
    /**
     * Starts the plugin.
     * 
     * @throws PluginException on plugin error.
     */
    public void start() throws PluginException {        
    }
    
    /**
     * Indicates if the plugin can be stopped.
     * 
     * @return <code>true</code> if the plugin can be stopped,
     *   <code>false</code> otherwise.
     */
    public boolean canStop() {
    	return true;
    }
    
    /**
     * Stops the plugin.
     * 
     * @throws PluginException on plugin error.
     */
    public void stop() throws PluginException {
    }
    
    /**
     * Stores application-wide state.
     *
     * @param clazz The state class.
     * @param value The state object.
     */
    protected void storeAppState(Class<?> clazz,Object value)
        throws PluginException {
    //
        try {
            AppState.get().storeState(clazz,value);
        } catch (JSONException exception) {
            throw new PluginException(exception);
        }            
    }
    
    /**
     * Restores application-wide state.
     *
     * @param clazz The state class.
     * @return The state object.
     */
    protected <T> T restoreAppState(Class<T> clazz) throws PluginException {
        try {
            return AppState.get().restoreState(clazz);
        } catch (JSONException exception) {
            throw new PluginException(exception);
        }
    }
    
    /**
     * Gets the plugin state.
     * 
     * @return The plugin state.
     */
    public TState getState() {
        return null;
    }
    
    /**
     * Sets the plugin state.
     * 
     * @param state The plugin state.
     */
    public void setState(TState state) {        
    }
    
    /**
     * Sets the storage directory.
     *
     * @param storageDir The storage directory.
     */
    public void setStorageDir(File storageDir) {
        this.storageDir = storageDir;
    }
    
    /**
     * Gets the plugin storage directory.
     *
     * @return The plugin storage directory.
     */
    public File getStorageDir() {
        return storageDir;
    }
}