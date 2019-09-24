package com.andcreations.ae.studio.plugins.ui.main.view.impl;

import bibliothek.gui.dock.action.DockAction;

/**
 * @author Mikolaj Gucki
 */
interface ViewAction {
    /**
     * Gets the dock action associated with this view action.
     *
     * @return The dock action.
     */
    DockAction getDockAction();
}