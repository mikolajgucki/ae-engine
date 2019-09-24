package com.andcreations.ae.studio.plugins.ui.main.view.impl;

import bibliothek.extension.gui.dock.theme.eclipse.EclipseColorScheme;
import bibliothek.gui.DockUI;
import bibliothek.gui.dock.util.laf.LookAndFeelColors;
import bibliothek.util.Colors;

/**
 * @author Mikolaj Gucki
 */
public class AEStudioEclipseColorScheme extends EclipseColorScheme {
    /** */
    @Override
    protected void updateUI() {
        super.updateUI();
        
        setColor( "stack.tab.top.selected",
            DockUI.getColor(LookAndFeelColors.PANEL_BACKGROUND));
        setColor( "stack.tab.bottom.selected",
            DockUI.getColor(LookAndFeelColors.PANEL_BACKGROUND));
        
        setColor("stack.tab.top.selected.focused",Colors.darker(
            DockUI.getColor(LookAndFeelColors.PANEL_BACKGROUND)));
        setColor("stack.tab.bottom.selected.focused",
            DockUI.getColor(LookAndFeelColors.PANEL_BACKGROUND));
        setColor("stack.tab.border.selected.focused",
            DockUI.getColor(LookAndFeelColors.PANEL_BACKGROUND));
    }
}
