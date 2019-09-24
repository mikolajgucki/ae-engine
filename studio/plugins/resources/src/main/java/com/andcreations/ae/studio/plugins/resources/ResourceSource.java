package com.andcreations.ae.studio.plugins.resources;

import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public interface ResourceSource {
    /**
     * Gets resources.
     *
     * @return The resources.
     */
    List<Resource> getResources();
    
    /**
     * Opens a resource.
     *
     * @param resource The resources.
     */
    void openResource(Resource resource);
}