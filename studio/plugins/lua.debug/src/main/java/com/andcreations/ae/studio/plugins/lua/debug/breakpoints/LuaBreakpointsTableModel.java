package com.andcreations.ae.studio.plugins.lua.debug.breakpoints;

import javax.swing.table.DefaultTableModel;

import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class LuaBreakpointsTableModel extends DefaultTableModel {
    /** */
    static final int BREAKPOINT_INDEX = 0;
    
    /** */
    static final int ENABLED_INDEX = 1;
    
    /** */
    private static final int LOCATION_INDEX = 2;
    
    /** */
    private BundleResources res = new BundleResources(
        LuaBreakpointsTableModel.class);      
    
    /** */
    LuaBreakpointsTableModel() {
        create();
    }
    
    /** */
    private void create() {
        addColumn("breakpoint"); // hidden
        addColumn(res.getStr("enabled"));
        addColumn(res.getStr("location"));
    }
    
    /** */
    @Override
    public Class<?> getColumnClass(int index) {
        if (index == ENABLED_INDEX) {
            return Boolean.class;
        }
        if (index == LOCATION_INDEX) {
            return String.class;
        }
        
        return super.getColumnClass(index);
    }
    
    /** */
    @Override
    public boolean isCellEditable(int row,int column) {
        if (column == ENABLED_INDEX) {
            return true;
        }
        
        return false;
    }
    
    /** */
    LuaBreakpoint getBreakpoint(int row) {
        return (LuaBreakpoint)getValueAt(row,BREAKPOINT_INDEX);
    }
    
    /** */
    private void setEnabled(int row,boolean enabled) {
        setValueAt(enabled ? Boolean.TRUE : Boolean.FALSE,row,ENABLED_INDEX);
    }
    
    /** */
    boolean isEnabled(int row) {
        Boolean value = (Boolean)getValueAt(row,ENABLED_INDEX);
        return value.booleanValue();
    }
    
    /** */
    void toggleEnabled(int row) {
        boolean enabled = isEnabled(row);
        setEnabled(row,!enabled);
    }
}