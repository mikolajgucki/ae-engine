package com.andcreations.ae.studio.plugins.ui.common;

import javax.swing.JTree;

/**
 * Provides JTree utility methods.
 *
 * @author Mikolaj Gucki
 */
public class JTreeUtil {
    /**
     * Expands all the nodes.
     *
     * @param tree The tree.
     */
    public static void expandAll(JTree tree) {
        int j = tree.getRowCount();
        int i = 0;
        while (i < j) {
            tree.expandRow(i);
            i += 1;
            j = tree.getRowCount();
        }
    }
    
    /**
     * Collapses all the nodes.
     *
     * @param tree The tree.
     */
    public static void collapseAll(JTree tree) {
        int row = tree.getRowCount() - 1;
        while (row >= 0) {
            tree.collapseRow(row);
            row--;
        }
    }
}