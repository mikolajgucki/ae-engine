package com.andcreations.ae.studio.plugins.todo;

import javax.swing.table.DefaultTableModel;

import com.andcreations.resources.BundleResources;

/**
 * The todo table model.
 *
 * @author Mikolaj Gucki
 */ 
@SuppressWarnings("serial")
class ToDoTableModel extends DefaultTableModel {
    /** */
    static final int ITEM_COLUMN_INDEX = 0;
    
    /** */
    static final int TAG_COLUMN_INDEX = 1;
    
    /** */
    static final int DESCRIPTION_COLUMN_INDEX = 2;
    
    /** */
    static final int RESOURCE_COLUMN_INDEX = 3;
    
    /** */
    static final int LOCATION_COLUMN_INDEX = 4;
    
    /** */
    private BundleResources res =
        new BundleResources(ToDoTableModel.class);
        
    /** */
    ToDoTableModel() {
        create();
    }
    
    /** */
    private void create() {
    // columns
        addColumn("item"); // hidden
        addColumn(res.getStr("tag"));
        addColumn(res.getStr("description"));
        addColumn(res.getStr("resource"));
        addColumn(res.getStr("location"));
    }
    
    /** */
    private int findRowIndex(ToDoItem item) {
        for (int row = 0; row < getRowCount(); row++) {
            if (getValueAt(row,ITEM_COLUMN_INDEX) == item) {
                return row;
            }
        }
        
        return -1;
    }
    
    /** */
    ToDoItem getItemByRow(int row) {
        if (row < 0 || row >= getRowCount()) {
            return null;
        }        
        return (ToDoItem)getValueAt(row,ITEM_COLUMN_INDEX);
    }
    
    /** */
    void addItem(ToDoItem item) {
        Object[] row = new Object[]{item,item.getTag(),item.getDescription(),
            item.getResource(),item.getLocation().toString()};
        addRow(row);
    }
    
    /** */
    void removeItem(ToDoItem item) {
        int row = findRowIndex(item);
        if (row >= 0) {
            removeRow(row);
        }
    }
}