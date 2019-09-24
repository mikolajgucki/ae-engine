package com.andcreations.ae.studio.plugins.lua.debug.traceback;

/**
 * @author Mikolaj Gucki
 */
public interface TracebackItemListener {
    /**
     * Called when a traceback item has been selected.
     *
     * @param item The traceback item.
     */
    void tracebackItemSelected(TracebackItem item);    
    
    /**
     * Called when a traceback item has been double-clicked.
     *
     * @param item The traceback item.
     */
    void tracebackItemDoubleClicked(TracebackItem item);
    
    /**
     * Called when a traceback item has been expanded.
     *
     * @param item The traceback item.
     */
    void tracebackItemExpanded(TracebackItem item);    
}