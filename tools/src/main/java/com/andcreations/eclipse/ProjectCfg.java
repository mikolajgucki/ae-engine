package com.andcreations.eclipse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class ProjectCfg {
    /** The project name. */
    private String name;
    
    /** The directories. */
    private List<String> dirs = new ArrayList<>();
    
    /** The linked resources. */
    private List<LinkedResource> linkedResources = new ArrayList<>();
    
    /**
     * Constructs a {@link ProjectCfg}.
     *
     * @param name The project name.
     */
    public ProjectCfg(String name) {
        this.name = name;
    }
    
    /** */
    public String getName() {
        return name;
    }
    
    /**
     * Adds a directory.
     *
     * @param dir The directory.
     */
    public void addDir(String dir) {
        dirs.add(dir);
    }
    
    /**
     * Gets the directories.
     *
     * @return The directories.
     */
    public List<String> getDirs() {
        return dirs;
    }
    
    /**
     * Adds a linked resource.
     *
     * @param linkedResource The linked resource.
     */
    public void addLinkedResource(LinkedResource linkedResource) {
        linkedResources.add(linkedResource);
    }
    
    /**
     * Gets the linked resources.
     *
     * @return The linked resoures.
     */
    public List<LinkedResource> getLinkedResources() {
        return linkedResources;
    }
}