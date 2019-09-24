package com.andcreations.ae.studio.plugins.ae.dist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.dist.AEDistFiles;
import com.andcreations.io.FileUtil;

/**
 * @author Mikolaj Gucki
 */
public class AEDist {
    /** */
    private static AEDist instance;
    
    /** The directory with the AE distribution. */
    private File aeDistDir;    
    
    /** The identifiers of plugins from the AE distribution. */
    private List<String> pluginIds = new ArrayList<>();
    
    /** */
    public File getAEDistFile(String name) {
        File file = new File(aeDistDir,name);
        try {
            file = file.getCanonicalFile();
        } catch (IOException exception) {
            // ignored, simply couldn't obtain the canonical file
        }
        
        return file;
    }
    
    /** */
    public File getAEDistDir() {
        return aeDistDir;
    }
    
    /** */
    private File canonical(File file) {
        try {
            file = file.getCanonicalFile();
        } catch (IOException exception) {
        }        
        return file;
    }
    
    /**
     * Gets the directory with the binaries in the distribution.
     *
     * @return The bin directory.
     */
    public File getAEDistBin() {
        return AEDistFiles.getBinDir(aeDistDir);
    }
    
    /** */
    public boolean isDistFile(File file) {
        return FileUtil.isAncestor(aeDistDir,file);
    }
    
    /** */
    public File getPluginsDir() {
        return AEDistFiles.getPluginsDir(aeDistDir);
    }
    
    /** */
    public File getLuaSrcDir() {
        return getAEDistFile(AEDistFiles.LUA_PATH);
    }
    
    /** */
    public String getLuaSrcPath() {
        return getLuaSrcDir().getAbsolutePath();
    }
    
    /** */
    private void findAEDistributionPlugins() {
        if (getPluginsDir().exists() == false) {
            return;
        }
        
        File[] files = getPluginsDir().listFiles();
    // for each file
        for (File file:files) {
            if (file.isDirectory() == true) {
                pluginIds.add(file.getName());
            }
        }
    }
    
    /** */
    void init(File aeDistDir) {
        this.aeDistDir = aeDistDir;
        findAEDistributionPlugins();
    }
    
    /**
     * Gets the directories with plugins.
     *
     * @return The plugin directories.
     */
    public List<File> getPluginDirs() {
        File pluginsDir = getPluginsDir();
        
        List<File> dirs = new ArrayList<>();
    // for each plugin
        for (String pluginId:pluginIds) {
            dirs.add(new File(pluginsDir,pluginId));
        }
        
        return dirs;
    }
    
    /**
     * Gets the plugin directory.
     *
     * @param pluginId The plugin identifier.
     * @return The plugin directory.
     */
    public File getPluginDir(String pluginId) {
        File dir = new File(getPluginsDir(),pluginId);
        try {
            dir = dir.getCanonicalFile();
        } catch (IOException exception) {
        }
        return dir;
    }
    
    /**
     * Checks if a plugin is installed.
     *
     * @param pluginId The plugin identifier.
     * @return <code>true</code> if installed, <code>false</code> otherwise.
     */
    public boolean isPluginInstalled(String pluginId) {
        File dir = getPluginDir(pluginId);
        return dir.exists() && dir.isDirectory();
    }
    
    /**
     * Gets the directory with plugin Lua sources.
     *
     * @param pluginId The plugin identifier.
     * @return The Lua sources directory.
     */
    public File getPluginLuaSrcDir(String pluginId) {
        return canonical(
            AEDistFiles.getPluginLuaSrcDir(getAEDistDir(),pluginId));
    }
    
    /** */
    public static AEDist get() {
        if (instance == null) {
            instance = new AEDist();
        }
        
        return instance;
    }
}