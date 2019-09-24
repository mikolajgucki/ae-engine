package com.andcreations.ae.studio.plugin.manager;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.resources.BundleResources;

/**
 * The storage for plugin states.
 * 
 * @author Mikolaj Gucki
 */
class PluginStorage {
    /** */
    private static final String STATE_FILE = ".state";
    
    /** */
    private final BundleResources res =
        new BundleResources(PluginStorage.class);     
    
    /** The storage directory. */
    private File dir;
    
    /** */
    void init(String storageDir) {
        dir = new File(storageDir);
        Log.info(res.getStr("storage.dir.is",dir.getAbsolutePath()));
        if (dir.exists() == false) {
            dir.mkdirs();
        }
    }
    
    /**
     * Gets the plugin directory.
     * 
     * @param pluginDesc The plugin descriptor.
     * @return The plugin directory.
     */
    File getPluginDir(PluginDesc pluginDesc) {
        return new File(dir,pluginDesc.getId());
    }
    
    /**
     * Restores plugin state.
     * 
     * @param pluginDesc The plugin descriptor.
     * @return The restored state object.
     * @throws IOException on I/O error.
     */
    Object restoreState(PluginDesc pluginDesc) throws IOException {
        File pluginDir = getPluginDir(pluginDesc);
        pluginDesc.setPluginStorageDir(pluginDir);
        if (pluginDir.exists() == false) {
            return null;
        }
        
    // read the file
        File stateFile = new File(pluginDir,STATE_FILE);
        if (stateFile.exists() == false) {
            return null;
        }
        String json = FileUtils.readFileToString(stateFile,"UTF-8");

    // deserialize
        Object state = PluginState.deserialize(json,pluginDesc.getStateClass());        
        return state;
    }
    
    /**
     * Stores plugin state.
     * 
     * @param pluginDesc The plugin descriptor.
     * @param state The plugin state.
     * @throws IOException on I/O error.
     */
    void storeState(PluginDesc pluginDesc,Object state) throws IOException {
        File pluginDir = pluginDesc.getPluginStorageDir();
        if (pluginDir.exists() == false) {
            pluginDir.mkdirs();
        }        
        
    // read the file
        File stateFile = new File(pluginDir,STATE_FILE);        
        if (state == null) {
            if (stateFile.exists() == true) {
                stateFile.delete();
            }            
            return;
        }
        
    // serialize
        String json = PluginState.serialize(state);
        
    // write
        FileUtils.write(stateFile,json,"UTF-8");
    }
}
