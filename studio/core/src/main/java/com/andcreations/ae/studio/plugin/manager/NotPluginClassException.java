package com.andcreations.ae.studio.plugin.manager;

import com.andcreations.ae.studio.plugin.PluginException;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class NotPluginClassException extends PluginException {
    /** */
    NotPluginClassException(String message) {
        super(message);
    }
}