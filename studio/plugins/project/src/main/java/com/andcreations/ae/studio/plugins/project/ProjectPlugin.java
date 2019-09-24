package com.andcreations.ae.studio.plugins.project;

import java.io.File;
import java.util.Properties;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.Required;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id={
    "com.andcreations.ae.studio.plugins.ae.dist",
    "com.andcreations.ae.studio.plugins.file",
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.ui.main",
    "com.andcreations.ae.studio.plugins.lua"
})
public class ProjectPlugin extends Plugin<ProjectState> {
    /** */
    @Required(id="com.andcreations.ae.studio.plugins.problems")
    private Object problemsPluginObject;
    
    /** */
    @Required(id="com.andcreations.ae.studio.plugins.lua")
    private Object luaPluginObject;
    
    /** */
    @Required(id="com.andcreations.ae.studio.plugins.console")
    private Object consolePluginObject;
    
    /** */
    @Required(id="com.andcreations.ae.studio.plugins.project.files")
    private Object projectFilesPluginObject;
    
    /** */
    private ProjectState state;
    
    /** The directory with the project. */
    private File projectDir;
    
    /** */
    @Override
    public void start() {
        ProjectMenu.get().init();
        ProjectSettings.get().init();
    }
    
    /** */
    @Override
    public ProjectState getState() {
        return state;
    }
    
    /** */
    @Override
    public void setState(ProjectState state) {
        if (state == null) {
            state = new ProjectState();
        }
        
        this.state = state;
    }
    
    /** */
    private void logProperties(Properties properties) {
        Log.info("Read project properties:");
        for (String key:properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            Log.info(String.format("  %s = %s",key,value));
        }
    }
    
    /** */
    private void logLuaSrcDirs() {
        Log.info("Lua source directories:");
        File[] dirs = ProjectProperties.get().getLuaSrcDirs();
        for (File dir:dirs) {
            Log.info(String.format("  %s",dir.getAbsolutePath()));
        }
    }
    
    /**
     * Initializes the plugin.
     * 
     * @param projectDir The project directory.
     * @throws ProjectInitException if the project core plugin cannot be
     *   initialized.
     */
    public void init(File projectDir) throws ProjectInitException {
        Log.info("Initializing project core with project directory " +
            projectDir.getAbsolutePath());
        this.projectDir = projectDir;
        
    // initialize
        ProjectInitializer initializer = new ProjectInitializer(projectDir);
        initializer.init();
        
    // project properties
        ProjectProperties.get().init(projectDir,initializer.getProjectHelper());
        
    // project files
        ProjectLuaFiles.get().init();
        
    // log
        logProperties(initializer.getProjectHelper().getProperties());
        logLuaSrcDirs();
        
    // validator
        ProjectValidator.init();        
    }
    
    /**
     * Gets the directory with the project.
     *
     * @return The project directory.
     */
    public File getProjectDir() {
        return projectDir;
    }
}