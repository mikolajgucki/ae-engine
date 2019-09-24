package com.andcreations.ae.studio.plugin;

/**
 * Provides plugin-related methods.
 * 
 * @author Mikolaj Gucki
 */
public class PluginUtil {
    /**
     * Checks if a class if a plugin class.
     * 
     * @param clazz The class.
     * @return <code>true</code> if the class is a plugin class,
     *   <code>false</code> otherwise.
     */
    public static boolean isPluginClass(Class<?> clazz) {
        while (clazz != null) {
            if (Plugin.class.getName().equals(clazz.getName())) {
                return true;
            }
            
            clazz = clazz.getSuperclass();
        }
        
        return false;
    }
}
