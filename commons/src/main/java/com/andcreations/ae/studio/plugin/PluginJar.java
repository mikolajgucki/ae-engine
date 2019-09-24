package com.andcreations.ae.studio.plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Retrieves data from plugin jar file.
 * 
 * @author Mikolaj Gucki
 */
public class PluginJar {
    /** The section of a JAR manifest with plugin information. */
    public static final String JAR_SECTION_PLUGIN = "AEStudio-Plugin";
    
    /** The attributes of a JAR manifest with plugin class name. */
    public static final String JAR_PLUGIN_CLASS_NAME = "Plugin-Class";
    
    /** The plugin jar. */
    private File pluginJar;

    /** */
    public PluginJar(File pluginJar) {
        this.pluginJar = pluginJar;
    }
    
    /**
     * Gets the jar file.
     * 
     * @return The jar file.
     * @throws IOException on I/O error.
     */
    public JarFile getJarFile() throws IOException {
        return new JarFile(pluginJar.getAbsolutePath(),true);
    }
    
    /**
     * Reads the manifest from the jar.
     * 
     * @return The manifest.
     * @throws IOException on I/O error.
     */
    private Manifest readManifest() throws IOException {
        JarFile jar = null;
        try {
            jar = getJarFile();
            
        // get the manifest file as stream
            InputStream manifestInputStream = jar.getInputStream(
                    jar.getEntry(JarFile.MANIFEST_NAME));
          
        // load the manifest
            return new Manifest(manifestInputStream);                
        } finally {
            try {
                if (jar != null) {
                    jar.close();
                }
            } catch (IOException exception) {
            }
        }        
    }
    
    /**
     * Gets the plugin identifier.
     * 
     * @return The plugin identifier.
     */
    public String getPluginId() {
        String path = pluginJar.getName();
        String id = path.substring(0,path.length() - 4); // without .jar
        
        return id;
    }
    
    /**
     * Checks if the manifest has the plugin section.
     * 
     * @return <code>true</code> if has, <code>false</code> otherwise.
     * @throws IOException on I/O error.
     */
    public boolean hasPluginSection() throws IOException {
        Manifest manifest = readManifest();
        
    // attributes
        Attributes attributes = manifest.getAttributes(JAR_SECTION_PLUGIN);
        return attributes != null;
    }
    
    /**
     * Gets the name of the plugin class.
     * 
     * @return The class name.
     * @throws IOException on I/O error.
     */
    public String getClassName() throws IOException {
        Manifest manifest = readManifest();

    // plugin section
        Attributes attributes = manifest.getAttributes(JAR_SECTION_PLUGIN);
        if (attributes == null) {
            return null;
        }
        
    // class name
        return attributes.getValue(JAR_PLUGIN_CLASS_NAME);
    }
}
