package com.andcreations.ae.studio.plugin.manager;

import java.lang.reflect.Field;

/**
 * A plugin dependency (annotated with
 * {@link com.andcreations.ae.studio.plugin.Required},
 * {@link com.andcreations.ae.studio.plugin.StartAfterAndRequire}) or
 * {@link com.andcreations.ae.studio.plugin.StartBeforeAndRequire}).
 *
 * @author Mikolaj Gucki
 */
class PluginDependency {
    /** The plugin identifier. */
    private String pluginId;
    
    /** The annotated field. */
    private Field field;
    
    /**
     * Constructs a {@link PluginDependency}.
     *
     * @param pluginId The plugin identifier.
     * @param field The annotated field.
     */ 
    PluginDependency(String pluginId,Field field) {
        this.pluginId = pluginId;
        this.field = field;
    }
    
    /** */    
    String getPluginId() {
        return pluginId;
    }
    
    /** */
    Field getField() {
        return field;
    }
}