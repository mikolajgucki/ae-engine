package com.andcreations.ae.studio.plugin.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A plugin descriptor.
 * 
 * @author Mikolaj Gucki
 */
public class PluginDesc {
    /** The plugin identifier. */
    private String id;
    
    /** The plugin class name. */
    private String className;
    
    /** The plugin class. */
    private Class<?> pluginClass;
    
    /** The state class. */
    private Class<?> stateClass;
    
    /** The identifiers of the plugins on which this plugin depends. */
    private List<PluginDependency> dependencies = new ArrayList<>();
    
    /** The identifiers of the plugins on which must be started in order to
     *  start this plugin. */
    private List<StartDependency> startDependencies = new ArrayList<>();
    
    /** The plugin instance. */
    private Object instance;
    
    /** Indicates if the this plugin is an initializer. */
    private boolean initializer;
    
    /** Indicates if the this plugin is a finalizer. */
    private boolean finalizer;
    
    /** The plugin storage directory. */
    private File pluginStorageDir;
    
    /** */
    PluginDesc(String id,String className) {
        this.id = id;
        this.className = className;
    }
    
    /**
     * Gets the plugin identifier.
     * 
     * @return The plugin identifier.
     */
    public String getId() {
        return id;
    }
    
    /**
     * Gets the JAR file containing the plugin.
     * 
     * @return The JAR file.
     */
//    public File getJarFile() {
//        return jarFile;
//    }
    
    /**
     * Gets the class name.
     * 
     * @return The class name.
     */
    public String getClassName() {
        return className;
    }
    
    /**
     * Sets the plugin class.
     *
     * @param pluginClass The plugin class.
     */
    void setPluginClass(Class<?> pluginClass) {
        this.pluginClass = pluginClass;        
    }
    
    /**
     * Gets the plugin class.
     *
     * @return The plugin class.
     */
    Class<?> getPluginClass() {
        return pluginClass;
    }
    
    /**
     * Sets the state class.
     * 
     * @param stateClass The state class.
     */
    void setStateClass(Class<?> stateClass) {
    	this.stateClass = stateClass;
    }
    
    /**
     * Gets the state class.
     * 
     * @return The state class.
     */
    Class<?> getStateClass() {
    	return stateClass;
    }
    
    /**
     * Adds a dependency.
     * 
     * @param dependency The plugin on which this plugin depends.
     */
    void addDependency(PluginDependency dependency) {
        dependencies.add(dependency);
    }
    
    /**
     * Gets the dependencies.
     *
     * @return The identifiers of the plugins in which this plugin depends.
     */
    List<PluginDependency> getDependencies() {
        return dependencies;
    }
    
    /**
     * Adds a start dependency.
     * 
     * @param pluginId The identifier of the plugin which must be started in
     *   order to start this plugin.
     */
    void addStartDependency(String pluginId) {
        startDependencies.add(new StartDependency(pluginId));
    }
    
    /**
     * Gets the start dependencies.
     *
     * @return The identifiers of the plugins which must be started in orer
     *   to start this plugin.
     */
    List<StartDependency> getStartDependencies() {
        return startDependencies;
    }
    
    /**
     * Checks if this plugin has a start dependency.
     * 
     * @param pluginId The identifier of the dependent plugin.
     * @return <code>true</code> if has, <code>false</code> otherwise.
     */
    boolean hasStartDependency(String pluginId) {
    	for (StartDependency dependency:startDependencies) {
    		if (pluginId.equals(dependency.getPluginId())) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    /**
     * Sets the plugin instance.
     *
     * @param instance The plugin instance.
     */
    void setInstance(Object instance) {
        this.instance = instance;
    }
    
    /**
     * Gets the plugin instance.
     *
     * @return The plugin instance.
     */
    Object getInstance() {
        return instance;
    }
    
    /** */
    void setInitializer(boolean initializer) {
        this.initializer = initializer;
    }
    
    /** */
    boolean isInitializer() {
        return initializer;
    }
    
    /** */
    void setFinalizer(boolean finalizer) {
    	this.finalizer = finalizer;
    }
    
    /** */
    boolean isFinalizer() {
    	return finalizer;
    }
    
    /** */
    void setPluginStorageDir(File pluginStorageDir) {
    	this.pluginStorageDir = pluginStorageDir;
    }
    
    /** */
    File getPluginStorageDir() {
    	return pluginStorageDir;
    }
}
