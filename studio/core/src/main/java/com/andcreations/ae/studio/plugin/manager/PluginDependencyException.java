package com.andcreations.ae.studio.plugin.manager;

import com.andcreations.ae.studio.plugin.PluginException;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class PluginDependencyException extends PluginException {
    /** */
    PluginDependencyException(String message) {
        super(message);
    }
}