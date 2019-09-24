package com.andcreations.ae.studio.plugins.lua.debug.globals;

/**
 * @author Mikolaj Gucki
 */
public interface GlobalsItemListener {
    /**
     * Called when a globals item has been selected.
     *
     * @param item The globals item.
     */
    void globalsItemSelected(GlobalsItem item);    
    
    /**
     * Called when a globals item has been double-clicked.
     *
     * @param item The globals item.
     */
    void globalsItemDoubleClicked(GlobalsItem item);
    
    /**
     * Called when a globals item has been expanded.
     *
     * @param item The globals item.
     */
    void globalsItemExpanded(GlobalsItem item);    
}