package com.andcreations.ae.studio.plugins.console;

/**
 * @author Mikolaj Gucki
 */
public interface LinkListener {
    /**
     * Called when a link has been clicked.
     *
     * @param event The link event.
     */
    void linkClicked(LinkEvent event);
}