package com.andcreations.eclipse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class JavaProjectCfg extends ProjectCfg {
    /** The source directories. */
    private List<String> srcDirs = new ArrayList<>();
    
    /** The library files. */
    private List<String> libs = new ArrayList<>();
    
    /** The output directory. */
    private String outputDir;
    
    /** The referenced projects. */
    private List<String> projects = new ArrayList<>();
    
    /**
     * Constructs a {@link JavaProjectCfg}.
     *
     * @param name The project name.
     */
    public JavaProjectCfg(String name) {
        super(name);
    }
    
    /**
     * Adds a source directory.
     *
     * @param srcDir The source directory.
     */
    public void addSrcDir(String srcDir) {
        srcDirs.add(srcDir);
    }
    
    /**
     * Gets the source directories.
     *
     * @return The source directories.
     */
    public List<String> getSrcDirs() {
        return srcDirs;
    }
    
    /**
     * Adds a library.
     *
     * @param lib The library.
     */
    public void addLib(String lib) {
        libs.add(lib);
    }
    
    /**
     * Gets the libraries.
     *
     * @return The libraris.
     */
    public List<String> getLibs() {
        return libs;
    }    
    
    /**
     * Sets the output directory.
     *
     * @param outputDir The output directory.
     */
    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }
    
    /**
     * Gets the output directory.
     *
     * @return The output directory.
     */
    public String getOutputDir() {
        return outputDir;
    }
    
    /**
     * Adds a project.
     *
     * @param project The project.
     */
    public void addProject(String project) {
        projects.add(project);
    }
    
    /**
     * Adds project.
     *
     * @param projects The projects.
     */
    public void addProjects(String... projects) {
        for (String project:projects) {
            addProject(project);
        }
    }
    
    /**
     * Gets the projects.
     *
     * @return The projects.
     */
    public List<String> getProjects() {
        return projects;
    }
}