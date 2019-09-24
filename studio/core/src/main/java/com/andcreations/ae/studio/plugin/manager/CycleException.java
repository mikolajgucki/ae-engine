package com.andcreations.ae.studio.plugin.manager;

/**
 * Thrown when a cycle in plugin start order has been found.
 * 
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class CycleException extends Exception {
    /** */
    CycleException(String msg) {
        super(msg);
    }
}
