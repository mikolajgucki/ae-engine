package com.andcreations.ae.studio.plugins.project.select;

import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.studio.plugin.PluginState;

/**
 * The project select plugin state.
 * 
 * @author Mikolaj Gucki
 */
public class ProjectSelectState extends PluginState {
    /** The directories with projects. */
    private List<String> projectDirs;
    
    /** The selected project directory. */
    private String selectedProjectDir;
    
    /** */
    public void addProjectDir(String projectDir) {
        if (projectDirs == null) {
            projectDirs = new ArrayList<>();
        }
        
        if (projectDirs.contains(projectDir) == true) {
            return;
        }
        projectDirs.add(projectDir);
    }
    
    /** */
    public List<String> getProjectDirs() {
        if (projectDirs == null) {
            projectDirs = new ArrayList<>();
        }
        return projectDirs;
    }
    
    /** */
    public void setSelectedProjectDir(String selectedProjectDir) {
        this.selectedProjectDir = selectedProjectDir;
    }
    
    /** */
    public String getSelectedProjectDir() {
        return selectedProjectDir;
    }
}
