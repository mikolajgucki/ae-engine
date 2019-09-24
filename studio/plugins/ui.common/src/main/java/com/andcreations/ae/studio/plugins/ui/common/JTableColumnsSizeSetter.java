package com.andcreations.ae.studio.plugins.ui.common;

import javax.swing.JTable;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * @author Mikolaj Gucki
 */
public class JTableColumnsSizeSetter {
    /** */
    private JTable table;
    
    /** */
    private double[] ratios;
    
    /** */
    private AncestorListener ancestorListener;        
    
    /** */
    private JTableColumnsSizeSetter(JTable table,double[] ratios) {
        this.table = table;
        this.ratios = ratios;
    }
    
    /** */
    private void setTableColumnSizes() {
        if (ancestorListener == null) {
            return;
        }
        
        int width = table.getWidth();
        if (width == 0) {
            return;
        }
        
        table.removeAncestorListener(ancestorListener);
        ancestorListener = null;
        
        JTableUtil.setColumnWidthsByRatio(table,ratios);
    }    
    
    /** */
    private void init() {
        ancestorListener = new AncestorListener() {
            /** */                
            @Override
            public void ancestorAdded(AncestorEvent event) {
                setTableColumnSizes();
            }
            
            /** */
            @Override
            public void ancestorMoved(AncestorEvent event) {
            }
            
            /** */
            @Override
            public void ancestorRemoved(AncestorEvent event) {
            }
        };
        table.addAncestorListener(ancestorListener);        
    }
    
    /** */
    public static void set(JTable table,double[] ratios) {
        JTableColumnsSizeSetter setter =
            new JTableColumnsSizeSetter(table,ratios);
        setter.init();
    }
}