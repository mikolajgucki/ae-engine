package com.andcreations.ae.sdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.tools.ant.BuildException;

import com.andcreations.ae.project.AEProject;
import com.andcreations.ant.AETask;
import com.andcreations.resources.BundleResources;
import com.andcreations.resources.StrResources;

/**
 * The superclass with helper methods of the project update Ant tasks.
 *
 * @author Mikolaj Gucki
 */
public abstract class UpdateProjectAntTask extends AETask {
    /** The name of the Ant property with the project directory. */
    private static final String AE_PROJECT_DIR = "ae.project.dir";
    
    /** The string resources. */
    private StrResources res = new BundleResources(UpdateProjectAntTask.class);      
    
    /** The path to the project directory. */
    private String projectDir;    
    
    /** The project properties. */
    private Properties properties;
    
    /**
     * Gets the project directory.
     *
     * @return The project directory.
     */
    protected String getProjectDir() {
        return projectDir;
    }
    
    /**
     * Gets a file relative to the project directory.
     *
     * @param file The file.
     * @return The file relative to the project directory.
     */
    protected File getProjectRelativeFile(File file) {
        if (file.isAbsolute() == true) {
            return file;
        }
        
        return new File(new File(getProjectDir()),file.getPath());
    }
    
    /**
     * Reads the project properties.
     */
    private void readProperties() {
        properties = new Properties();
        
        try {
            FileInputStream input = new FileInputStream(projectDir +
                File.separator + AEProject.PROPERTIES_FILE);
            properties.load(input);
            input.close();
        } catch (IOException exception) {
            throw new BuildException(exception);
        }
    }    
    
    /**
     * Gets the project properties.
     *
     * @return The project properties.
     */
    protected Properties getProperties() {
        return properties;
    }
    
    /**
     * Gets the value of a property.
     *
     * @param key The property key.
     */
    protected String getProperty(String key) {
        return (String)getProperties().get(key);
    }
    
    /** */
    @Override
    public void execute() {
        projectDir = getProject().getProperty(AE_PROJECT_DIR);
        if (projectDir == null) {
            throw new BuildException(res.getStr("no.ae.project.dir",
                AE_PROJECT_DIR));
        }
        readProperties();
    }
}