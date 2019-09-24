package com.andcreations.ae.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.andcreations.ae.dist.AEDistFiles;

/**
 * Provides various project information (properties, directories) in relation
 * to a AE distribution directory.
 *
 * @author Mikolaj Gucki
 */
public class AEProjectHelper {
    /** */
    private File aeDistDir;
    
    /** */
    private File projectDir;
    
    /** */
    private File propertiesFile;
    
    /** */
    private Properties properties;
    
    /** */
    private Map<String,String> propertiesMap;
    
    /**
     * Creates an {@link AEProjectHelper}.
     *
     * @param aeDistDir The AE distribution directory.
     * @param projectDir The project directory.
     * @throws IOException on I/O error.
     * @throws NoSuchVariableException if a property references unknown
     *   environment veriable.
     */
    public AEProjectHelper(File aeDistDir,File projectDir) {
        this.aeDistDir = aeDistDir;
        this.projectDir = projectDir;
        create();
    }
    
    /** */
    private void create() {
        propertiesFile = new File(projectDir,AEProject.PROPERTIES_FILE);
        properties = new Properties();
    }
    
    /** */
    private void readProperties() throws IOException {
        FileInputStream input = null;
        try {
            input = new FileInputStream(propertiesFile);
            properties.load(input);
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }
    
    /** */
    private void createPropertiesMap() {
        propertiesMap = new HashMap<String,String>();
        for (String key:properties.stringPropertyNames()) {
            propertiesMap.put(key,properties.getProperty(key));
        }
    }
    
    /** */
    private Map<String,String> getExtraVars() {
        Map<String,String> extraVars = new HashMap<String,String>();
        
    // plugins Lua source directories
        String[] plugins = AEProjectProperties.getPlugins(propertiesMap);
        if (plugins != null) {
            File pluginsDir = AEDistFiles.getPluginsDir(aeDistDir);
            AEProjectProperties.putLuaPluginsDirs(
                pluginsDir.getAbsolutePath(),plugins,extraVars);
        }
        
        return extraVars;
    }    
    
    /** */
    private void replaceEnvVariables() throws NoSuchVariableException {
        Map<String,String> extraVars = getExtraVars();       
        
    // for each property
        for (String key:properties.stringPropertyNames()) {
            String oldValue = properties.getProperty(key);
            String newValue = null;
            try {
                newValue = AEProjectProperties.replaceEnvVariables(
                    oldValue,extraVars);
            } catch (NoSuchVariableException exception) {
                exception.setReferringVarName(key);
                throw exception;
            }
            properties.setProperty(key,newValue);                
        }
    }    
    
    /** */
    public void init() throws IOException,NoSuchVariableException {
        readProperties();
        createPropertiesMap();
        replaceEnvVariables();
    }
    
    /** */
    public File getPropertiesFile() {
        return propertiesFile;
    }
    
    /** */
    public String getOS() {
        return AEProjectProperties.getOSName(propertiesMap);
    }
    
    /** */
    public Properties getProperties() {
        return properties;
    }
    
    /** */
    public Map<String,String> getPropertiesMap() {
        return propertiesMap;
    }
    
    /**
     * Gets the directories with Lua sources.
     *
     * @return The Lua source directories.
     */
    public List<File> getLuaSrcDirs() {
        String value = properties.getProperty(AEProjectProperties.LUA_SRC_DIRS);
        String[] paths = AEProjectProperties.getDirs(value);
        
        return new ArrayList<>(Arrays.asList(
            AEProjectIO.getDirsFromPaths(projectDir,paths)));
    }
    
    /**
     * Gets the directories with Lua tests sources.
     *
     * @return The Lua tests source directories.
     */
    public List<File> getLuaTestSrcDirs() {
        List<File> dirs = new ArrayList<>();
        dirs.add(AEProjectIO.getDirFromPath(
            projectDir,AEProject.TESTS_LUA_PATH));
        
        return dirs;
    }
}