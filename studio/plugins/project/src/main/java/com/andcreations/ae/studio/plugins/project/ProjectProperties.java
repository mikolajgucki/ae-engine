package com.andcreations.ae.studio.plugins.project;

import java.io.File;
import java.util.List;
import java.util.Set;

import com.andcreations.ae.project.AEProjectHelper;
import com.andcreations.ae.project.AEProjectProperties;
import com.andcreations.resources.BundleResources;

/**
 * Provides the project properties.
 *
 * @author Mikolaj Gucki
 */
public class ProjectProperties {
    /** The names of the mandatory properties. */
    private static final String[] MANDATORY_NAMES = {
        AEProjectProperties.APP_NAME,
        AEProjectProperties.LUA_SRC_DIRS
    };
    
    /** */
    private final BundleResources res =
        new BundleResources(ProjectProperties.class);  
    
    /** */
    private static ProjectProperties instance;
    
    /** */
    private AEProjectHelper projectHelper;
    
    /** */
    private ProjectProperties() {
    }
    
    /** */
    private void checkPropertyExists(String name) throws ProjectInitException {
        if (projectHelper.getProperties().getProperty(name) == null) {
            throw new ProjectInitException(
                res.getStr("property.not.found",name));
        }
    }
    
    /** */
    void init(File projectDir,AEProjectHelper projectHelper)
        throws ProjectInitException {
    //
        this.projectHelper = projectHelper;
        
    // mandatory properties
        for (String name:MANDATORY_NAMES) {
            checkPropertyExists(name);
        }
    }
    
    /**
     * Gets the application name.
     *
     * @return The application name.
     */
    public String getAppName() {
        return projectHelper.getProperties().getProperty(
            AEProjectProperties.APP_NAME);
    }
    
    /**
     * Gets the simulated OS.
     *
     * @return The OS. 
     */
    public String getOS() {
        return projectHelper.getProperties().getProperty(
            AEProjectProperties.OS);
    }
    
    /**
     * Gets all the plugins declarated in the properties.
     *
     * @return The plugins.
     */
    public Set<String> getAllPlugins() {
        return AEProjectProperties.getAllPlugins(
            projectHelper.getPropertiesMap());
    }
    
    /**
     * Gets the Lua source directories (including directories with tests).
     *
     * @return The Lua source directories.
     */
    public File[] getLuaSrcDirs() {
    // project sources
        List<File> dirs = projectHelper.getLuaSrcDirs();
        
    // test sources
        dirs.addAll(projectHelper.getLuaTestSrcDirs());
        
        return dirs.toArray(new File[]{});
    }
    
    /**
     * Gets the Lua source directories with tests.
     *
     * @return The Lua source directories with tests.
     */
    public File[] getLuaTestSrcDirs() {
        return projectHelper.getLuaTestSrcDirs().toArray(new File[]{});
    }
    
    /**
     * Gets the project properties instance.
     *
     * @return The instance.
     */
    public static ProjectProperties get() {
        if (instance == null) {
            instance = new ProjectProperties();
        }
        
        return instance;
    }
}