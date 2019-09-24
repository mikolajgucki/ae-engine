package com.andcreations.ae.studio.plugins.ui.common;

import javax.swing.JTable;

/**
 * Provides JTable utility methods.
 *
 * @author Mikolaj Gucki
 */
public class JTableUtil {
    /**
     * Sets the width of columns by ration.
     *
     * @param table The table.
     * @param ratios The ratios.
     */
    public static void setColumnWidthsByRatio(JTable table,double... ratios) {
    // normalize
        double totalRatio = 0;
        for (double ratio:ratios) {
            totalRatio += ratio;
        }
        for (int index = 0; index < ratios.length; index++) {
            ratios[index] /= totalRatio;
        }
        
    // set widths
        for (int index = 0; index < ratios.length; index++) {
            int width = (int)(table.getWidth() * ratios[index]);
            table.getColumnModel().getColumn(index).setPreferredWidth(width);
        }
    }
}