package com.andcreations.ae.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.andcreations.ae.dist.AEDistFiles;

/**
 * Contains the project properties.
 * 
 * @author Mikolaj Gucki
 */
public class AEProjectProperties {
    /** The name of the property with the application name. */
    public static final String APP_NAME = "app.name";
    
    /** The name of the property with the OS under which the app is simulated
        to run. */
    public static final String OS = "os";
    
    /** The name of the property with semi-colon separated directories
        with assets. */
    public static final String ASSET_DIRS = "asset.dirs";
    
    /** The name of the property with semi-colon separated directories
        with Lua sources. */
    public static final String LUA_SRC_DIRS = "lua.src.dirs";
    
    /** The name of the property with semi-colon separated directories
        with textures. */
    public static final String TEXTURE_DIRS = "texture.dirs";
    
    /** The name of the property with semi-colon separated directories
        with sound files. */
    public static final String SOUND_DIRS = "sound.dirs";
    
    /** The name of the property with semi-colon separated directories
        with music files. */
    public static final String MUSIC_DIRS = "music.dirs";
    
    /** The comma-separated list of Android plugins. */
    public static final String ANDROID_PLUGINS = "android.plugins";
    
    /** The comma-separated list of iOS plugins. */
    public static final String IOS_PLUGINS = "ios.plugins";
    
    /** The name of the property with directories with plugin Lua sources. */
    public static final String LUA_PLUGINS_DIRS = "LUA_PLUGINS_DIRS";
    
    /**
     * Gets the list of the directories (paths) from a property value.
     * 
     * @param propertyValue The property value.
     * @return The directories.
     */
    public static String[] getDirs(String propertyValue) {
        return propertyValue.split(";");
    }
    
    /** 
     * Replaces {name} with the environment variables in the property value.
     *
     * @param value The property value.
     * @param vars The variables.
     * @return The result value.
     * @throws NoSuchVariableException if the property values refers
     *     a non-existing variable.
     */    
    public static String replaceVariables(String propertyValue,
        Map<String,String> vars) throws NoSuchVariableException {
    //
        while (true) {
            int start = propertyValue.indexOf("{");
            if (start == -1) {
                break;
            }
            
            int end = propertyValue.indexOf("}");
            if (end == -1) {
                break;
            }
            
            String name = propertyValue.substring(start + 1,end);
            String value = vars.get(name);
            if (value == null) {
                throw new NoSuchVariableException(name);
            }
            
            propertyValue = propertyValue.substring(0,start) +
                value + propertyValue.substring(end + 1);
        }
        
        return propertyValue;    
    }
    
    /** 
     * Replaces variables in a property with envrionment variables.
     *
     * @param value The property value.
     * @return The result value.
     * @throws NoSuchVariableException if the property values refers
     *     a non-existing variable.
     */
    public static String replaceEnvVariables(String propertyValue)
        throws NoSuchVariableException {
    //
        return replaceVariables(propertyValue,System.getenv());
    }
    
    /** 
     * Replaces variables in a property with envrionment and extra variables.
     *
     * @param value The property value.
     * @return The result value.
     * @throws NoSuchVariableException if the property values refers
     *     a non-existing variable.
     */
    public static String replaceEnvVariables(String propertyValue,
        Map<String,String> extraVars) throws NoSuchVariableException {
    //
        Map<String,String> vars = new HashMap<String,String>();
        vars.putAll(extraVars);
        vars.putAll(System.getenv());        
        
        return replaceVariables(propertyValue,vars);
    }
    
    /**
     * Gets the OS name (without validation).
     *
     * @param properties The properties.
     * @return The OS name.
     */
    public static String getOSName(Map<String,String> properties) {
        return properties.get(OS);
    }
    
    /**
     * Gets the plugins from a property value.
     *
     * @param propertyValue The property value.
     * @return The plugins.
     */
    public static String[] getPlugins(String propertyValue) {
        return propertyValue.split(",");
    }
    
    /**
     * Gets plugins according to set OS.
     *
     * @param properties The properties.
     * @return The plugins or <code>null</code> if the OS is not set or unknown.
     */
    public static String[] getPlugins(Map<String,String> properties) {
        String osName = properties.get(OS);
        if (AEProjectOS.isValid(osName) == false) {
            return null;
        }
        
        return getPlugins(properties.get(String.format("%s.plugins",osName)));
    }
    
    /**
     * Gets all the plugins.     
     *
     * @param properties The properties.
     * @return The plugins.     
     */
    public static Set<String> getAllPlugins(Map<String,String> properties) {
        Set<String> plugins = new HashSet<>();
        
    // Android plugins
        String[] androidPlugins = getPlugins(properties.get(ANDROID_PLUGINS));
        for (String plugin:androidPlugins) {
            plugins.add(plugin);
        }
        
    // iOS plugins
        String[] iosPlugins = getPlugins(properties.get(IOS_PLUGINS));
        for (String plugin:iosPlugins) {
            plugins.add(plugin);
        }
        
        return plugins;
    }
    
    /**
     * Gets the property value with directories with plugins Lua sources.
     *
     * @param pluginsDir The plugins directories.
     * @param plugins The plugins.
     * @return The property value.
     */
    public static String getLuaPluginsDirs(String pluginsDir,String[] plugins) {
        StringBuilder luaPluginsDirs = new StringBuilder();
        for (String plugin:plugins) {
            if (luaPluginsDirs.length() > 0) {
                luaPluginsDirs.append(";");
            }
            luaPluginsDirs.append(pluginsDir + "/" +
                plugin + "/" + AEDistFiles.PLUGIN_LUA_SRC_PATH);
        }
        
        return luaPluginsDirs.toString();        
    }
    
    /**
     * Puts an extra variable with directories with plugins Lua sources.
     *
     * @param pluginsDir The plugins directories.
     * @param plugins The plugins.
     * @param extraVars The extra variables.     
     */
    public static void putLuaPluginsDirs(String pluginsDir,String[] plugins,
        Map<String,String> extraVars) {
    //
        extraVars.put(LUA_PLUGINS_DIRS,getLuaPluginsDirs(pluginsDir,plugins));
    }
    
    /**
     * Gets the Lua source directories.
     *
     * @param projectProperties The project properties.
     */
    public static File[] getLuaSrcDirs(File projectDir,
        Map<String,String> projectProperties) {
    //
        String propertyValue = projectProperties.get(LUA_SRC_DIRS);
        String[] paths = getDirs(propertyValue);
        
        return AEProjectIO.getDirsFromPaths(projectDir,paths);
    }
    
    /**
     * Reads project properties.
     *
     * @param projectDir The project directory.
     * @return The project properties.
     * @throws IOException on I/O error.
     */
    public static Map<String,String> readProperties(File projectDir)
        throws IOException {
    // read
        Properties properties = new Properties();
        FileInputStream input = null;
        try {
            input = new FileInputStream(
                new File(projectDir,AEProject.PROPERTIES_FILE));
            properties.load(input);
        } finally {
            if (input != null) {
                input.close();
            }
        }    
        
    // convert to map
        Map<String,String> propertiesMap = new HashMap<String,String>();
        for (String key:properties.stringPropertyNames()) {
            propertiesMap.put(key,properties.getProperty(key));
        }        
        
        return propertiesMap;
    }
}
