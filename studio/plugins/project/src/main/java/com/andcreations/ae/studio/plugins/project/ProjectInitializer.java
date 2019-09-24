package com.andcreations.ae.studio.plugins.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.andcreations.ae.project.AEProjectHelper;
import com.andcreations.ae.project.NoSuchVariableException;
import com.andcreations.ae.studio.plugins.ae.dist.AEDist;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class ProjectInitializer {
    /** */
    private final BundleResources res =
        new BundleResources(ProjectInitializer.class);          
    
    /** */
    private File projectDir;
    
    /** */
    private AEProjectHelper projectHelper;
    
    /** */
    ProjectInitializer(File projectDir) {
        this.projectDir = projectDir;
    }
    
    /** */
    AEProjectHelper getProjectHelper() {
        return projectHelper;
    }
    
    /** */
    Properties getProperties() {
        return projectHelper.getProperties();
    }
    
    /** */
    void init() throws ProjectInitException {
        projectHelper = new AEProjectHelper(
            AEDist.get().getAEDistDir(),projectDir);
        File propertiesFile = projectHelper.getPropertiesFile();

    // initialize
        try {
            projectHelper.init();
        } catch (FileNotFoundException exception) {
            throw new ProjectInitException(res.getStr(
                "properties.file.not.found",propertiesFile.getAbsolutePath()));
        } catch (IOException exception) {
            throw new ProjectInitException(res.getStr(
                "failed.to.read.project.properties",
                propertiesFile.getAbsolutePath(),exception.getMessage()));
        } catch (NoSuchVariableException exception) {
            throw new ProjectInitException(res.getStr(
                "env.var.not.found",exception.getName(),
                exception.getReferringVarName(),
                propertiesFile.getAbsolutePath()));
        }
    }
}
