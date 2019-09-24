package com.andcreations.ae.studio.plugins.project.select;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;

import com.andcreations.ae.project.AEProject;
import com.andcreations.ae.project.template.ProjectTemplateBuilder;
import com.andcreations.ae.studio.plugin.manager.PluginManager;
import com.andcreations.ae.studio.plugins.ae.dist.AEDist;
import com.andcreations.ae.studio.plugins.project.ProjectInitException;
import com.andcreations.ae.studio.plugins.project.ProjectPlugin;
import com.andcreations.ae.studio.plugins.project.files.ProjectFiles;
import com.andcreations.ae.studio.plugins.project.select.ProjectSelectDialog.Option;
import com.andcreations.ae.studio.plugins.ui.common.MessageDialog;
import com.andcreations.io.DirUtil;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class ProjectSelect {
    /** */
    private final BundleResources res =
        new BundleResources(ProjectSelect.class);      
    
    /** */
    private ProjectSelectState state;

    /** */
    private ProjectPlugin projectCore;
    
    /** */
    private CreateProjectDialog createProjectDialog;
    
    /** */
    ProjectSelect(ProjectSelectState state,Object projectCorePlugin) {
        this.state = state;
        this.projectCore = (ProjectPlugin)projectCorePlugin;
        this.createProjectDialog = new CreateProjectDialog();   
    }
    
    /**
     * Initializes the app storage.
     */
    private void initStorage(String projectDir) {
        File storageDir = new File(projectDir + File.separator +
            ProjectFiles.STORAGE_DIR);
        PluginManager.initStorage(storageDir.getAbsolutePath());
    }
    
    /** */
    private File createProject(File projectDir) {
        while (true) {
            if (createProjectDialog.showDialog(projectDir) == true) {
                projectDir = createProjectDialog.getProjectDir();
                String appName = createProjectDialog.getAppName();
                
            // delete the project directory if exists
                if (projectDir.exists() == true) {
                    DirUtil.delete(projectDir);
                }
                
            // create project
                ProjectTemplateBuilder builder = new ProjectTemplateBuilder(
                    AEDist.get().getAEDistDir(),projectDir,appName);
                try {
                    builder.build();
                } catch (IOException exception) {
                    MessageDialog.error((Frame)null,
                        res.getStr("failed.to.create.project"),
                        exception.getMessage());                    
                    continue;
                }
                
                return projectDir;
            }
            break;
        }
        return null;
    }
    
    /**
     * Initializes the project specific AE Studio plugins.
     */
    private void initExtraPlugins(File projectDir) {
        File toolsBuildDir = new File(projectDir,AEProject.TOOLS_BUILD_PATH);
        PluginManager.addExtraPluginDir(toolsBuildDir);
    }
    
    /** */
    private boolean selectProject() {
        String selectedProjectDir = state.getSelectedProjectDir();
        
    // create dialog
        ProjectSelectDialog dialog = new ProjectSelectDialog(
            selectedProjectDir,state.getProjectDirs());
        
    // show dialog
        Option option = dialog.showProjectSelectDialog();
        if (option == null || option == Option.CANCEL) {
            System.exit(-1);
        }        
        File projectDir = dialog.getProjectDir();
        selectedProjectDir = projectDir.getAbsolutePath();
        state.setSelectedProjectDir(selectedProjectDir);
        
    // create project
        if (option == Option.CREATE_PROJECT) {
            projectDir = createProject(projectDir);
            if (projectDir == null) {
                System.exit(-1);
            }
        }
        
    // project core
        try {
            projectCore.init(projectDir);
        } catch (ProjectInitException exception) {
            MessageDialog.error((Frame)null,
                res.getStr("failed.to.open.project"),exception.getMessage());
            return false;
        }
        
    // storage
        state.addProjectDir(projectDir.getAbsolutePath());
        initStorage(projectDir.getAbsolutePath());
        
    // project files
        ProjectFiles.get().init(projectDir);
        
    // extra plugins
        initExtraPlugins(projectDir);
        
        return true;
    }
    
    /** */
    void run() {
        while (true) {
            if (selectProject() == true) {
                break;
            }
        }
    }
}