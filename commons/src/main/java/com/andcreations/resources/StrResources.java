package com.andcreations.resources;

import java.util.Set;

/**
 * @author Mikolaj Gucki
 */
public interface StrResources {
    /**
     * Gets a string for the given key.
     * 
     * @param key The key.
     * @return The string for the given key.
     */
    String getStr(String key);    
    
    /**
     * Gets a string for the given string and arguments.
     * 
     * @param key The key.
     * @param args The arguments.
     * @return The string for the given key and arguments.
     */
    String getStr(String key,String ... args);
    
    /**
     * Gets all the keys defined in these resources.
     *
     * @return The key set.
     */
    Set<String> getKeys();
}