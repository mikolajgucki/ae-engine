package com.andcreations.ant;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import com.andcreations.resources.BundleResources;
import com.andcreations.resources.ResourceLoader;
import com.andcreations.resources.StrResources;

/**
 * The superclass for Ant tasks with utility methods.
 *
 * @author Mikolaj Gucki
 */
public class AETask extends Task {
    /** The string resources. */
    private StrResources res = new BundleResources(AETask.class);

    /**
     * Invoked when an element is duplicated.
     * 
     * @param name The element name.
     */
    protected void duplicatedElement(String name) {
        throw new BuildException(res.getStr("duplicated.element",name));
    }
    
    /**
     * Invoked when an element is missing.
     * 
     * @param name The element name.
     */
    protected void missingElement(String name) {
        throw new BuildException(res.getStr("missing.element",name));
    }    
    
    /**
     * Verifies that an element is set (not null). Throws exception if the
     * element is not set.
     *
     * @param element The element.
     * @param name The element name.
     */
    protected void verifyElementSet(Object element,String name) {
        if (element == null) {
            missingElement(name);
        }
    }
    
    /**
     * Logs a verbose message.
     * 
     * @param msg The message.
     */
    protected void verbose(String msg) {
        log(msg,Project.MSG_VERBOSE);
    }
    
    /**
     * Logs a warning message.
     *
     * @param msg The message.
     */
    protected void warning(String msg) {
        log(msg,Project.MSG_WARN);
    }
    
    /**
     * Logs an error message.
     *
     * @param msg The message.
     */
    protected void error(String msg) {
        log(msg,Project.MSG_ERR);
    }
    
    /**
     * Builds path from file names.
     *
     * @param files The file names.
     * @return The path.     
     */
    protected String buildPath(String ... files) {
        String path = "";
        for (String file:files) {
            path += File.separator + file;
        }
        
        return path;
    }
    
    /**
     * Loads a resource.
     *
     * @param clazz The class in whose package to look for the resource.
     * @param name The resource name.
     * @return The resource as string.
     */
    protected String loadResourceAsString(Class<?> clazz,String name) {
        ResourceLoader loader = new ResourceLoader(clazz);
        
        StringBuffer buffer;
        try {
            buffer = loader.loadAsStringBuffer(name);
        } catch (IOException exception) {
            throw new BuildException(res.getStr("failed.to.load.resource",
                name,exception.getMessage()));
        }
        
        return buffer.toString();        
    }
}