package com.andcreations.ae.studio.plugins.ui.common;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Responsible for creation buttons on the bottom of frames and dialogs.
 *
 * @author Mikolaj Gucki
 */
public class ButtonPanel {
    /** The insets for grid bag constraints for button panel. */
    private static final Insets BUTTON_PANEL_GRID_BAG_INSETS =
        new Insets(0,2,0,2);
    
    /**
     * Creates a panel with button laid in a row.
     *
     * @param buttons The buttons to lay out in the panel.
     * @return The panel with the buttons.
     */
    public static JPanel createButtonPanel(JButton... buttons) {
        JPanel panel = new JPanel();
    
    // the panel layout
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = BUTTON_PANEL_GRID_BAG_INSETS;
        panel.setLayout(gridBag);
    
    // add the buttons to the panel
        for (int index = 0; index < buttons.length; index++) {
            if (buttons[index] != null) {
                gridBag.setConstraints(buttons[index],constraints);
                panel.add(buttons[index]);
            }
        }
    
        return panel;
    }
}
 