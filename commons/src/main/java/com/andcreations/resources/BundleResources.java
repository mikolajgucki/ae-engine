package com.andcreations.resources;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author Mikolaj Gucki
 */
public class BundleResources extends DefaultResources {
    /** The bundle with the resources. */
    private ResourceBundle bundle;
    
    /**
     * Constructs a {@link BundleResources} object.
     * 
     * @param baseName The base name.
     */
    public BundleResources(String baseName) {
        this.bundle = ResourceBundle.getBundle(baseName);
    }
    
    /**
     * Constructs a {@link BundleResources} object.
     * 
     * @param packageName The package name.
     * @param baseName The base name.
     */
    public BundleResources(String packageName,String baseName) {
        this(packageName + "." + baseName);
    }
    
    /**
     * Constructs a {@link BundleResources} object.
     * 
     * @param baseName The base name.
     * @param loader The class loader from which to load the resources.
     */
    public BundleResources(String baseName,ClassLoader loader) {
        this.bundle = ResourceBundle.getBundle(
            baseName,Locale.getDefault(),loader);
    }
    
    /**
     * Constructs a {@link BundleResources} object.
     * 
     * @param packageName The package name.
     * @param baseName The base name.
     * @param loader The class loader from which to load the resources.
     */
    public BundleResources(String packageName,String baseName,
        ClassLoader loader) {
    //
        this(packageName + "." + baseName,loader);
    }    
    
    /**
     * Constructs a {@link BundleResources} object.
     * 
     * @param clazz The class for which to the get the resources.
     */
    public BundleResources(Class<?> clazz) {
        this(clazz.getPackage().getName() + ".resources",clazz.getSimpleName(),
            clazz.getClassLoader());
    }

    /**
     * Tests if the resources contain given key.
     *
     * @param key The key.
     * @return <code>true</code> if contains, <code>false</code> otherwise.
     */
    public boolean containsKey(String key) {
        return bundle.containsKey(key);
    }
    
    /** */
    @Override
    public String getStr(String key) {
        return bundle.getString(key);
    }
    
    /** */
    @Override
    public Set<String> getKeys() {
        return bundle.keySet();
    }
}
