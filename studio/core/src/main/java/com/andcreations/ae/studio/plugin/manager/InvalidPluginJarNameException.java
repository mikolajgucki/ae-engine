package com.andcreations.ae.studio.plugin.manager;

import com.andcreations.ae.studio.plugin.PluginException;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class InvalidPluginJarNameException extends PluginException {
    /** */
    InvalidPluginJarNameException(String message) {
        super(message);
    }
}