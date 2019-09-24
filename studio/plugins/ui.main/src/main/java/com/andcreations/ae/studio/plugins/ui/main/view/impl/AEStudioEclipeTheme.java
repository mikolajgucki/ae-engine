package com.andcreations.ae.studio.plugins.ui.main.view.impl;

import bibliothek.extension.gui.dock.theme.EclipseTheme;
import bibliothek.gui.DockController;

/**
 * @author Mikolaj Gucki
 */
class AEStudioEclipeTheme extends EclipseTheme {
    /** */
    @Override
    public void install(DockController controller) {
        super.install(controller);
        
        setColorScheme(new AEStudioEclipseColorScheme());
        controller.getProperties().set(PAINT_ICONS_WHEN_DESELECTED,true); 
    }
}
