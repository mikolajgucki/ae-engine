package com.andcreations.ae.studio.plugins.lua.debug.breakpoints;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class LuaBreakpointsTableCellRenderer extends DefaultTableCellRenderer {
    /** */
    @Override
    public Component getTableCellRendererComponent(JTable table,Object value,
        boolean isSelected,boolean hasFocus,int row,int column) {
    //
        LuaBreakpointsTableModel model =
            (LuaBreakpointsTableModel)table.getModel();
        LuaBreakpoint breakpoint = model.getBreakpoint(row);
    
        JLabel label = (JLabel)super.getTableCellRendererComponent(
            table,value,isSelected,hasFocus,row,column);
        if (isSelected == false) {
            label.setText(breakpoint.getHtml());
        }
        else {
            label.setText(breakpoint.getText());
        }
        return label;
    }
}