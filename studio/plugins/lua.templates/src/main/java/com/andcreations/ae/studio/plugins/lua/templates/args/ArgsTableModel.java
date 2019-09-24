package com.andcreations.ae.studio.plugins.lua.templates.args;

import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class ArgsTableModel extends DefaultTableModel {
    /** */
    static final int COLUMN_NAME = 0;
    
    /** */
    static final int COLUMN_DESC = 1;
    
    /** */
    private BundleResources res = new BundleResources(ArgsTableModel.class);
    
    /** */
    private ArgsTableModelListener listener;
    
    /** */
    private boolean settingRows;
    
    /** */
    private List<ArgsTableRow> rows;
        
    /** */
    ArgsTableModel(ArgsTableModelListener listener) {
        this.listener = listener;
        create();
    }
    
    /** */
    private void create() {
    // columns
        addColumn(res.getStr("column.name"));
        addColumn(res.getStr("column.desc"));
        
    // listener
        addTableModelListener(new TableModelListener() {
            /** */
            @Override
            public void tableChanged(TableModelEvent event) {
                if (settingRows == true) {
                    return;
                }
                
                if (event.getType() == TableModelEvent.UPDATE) {
                    int row = event.getFirstRow();
                    if (event.getColumn() == COLUMN_DESC) {
                        String value = (String)getValueAt(row,COLUMN_DESC);
                        rows.get(row).setDesc(value);
                        listener.rowChanged(row);
                    }
                }
            }
        });
    }
    
    /** */
    @Override
    public boolean isCellEditable(int row,int column) {
        return column == COLUMN_DESC;
    }
    
    /** */
    void clear() {
        while (getRowCount() > 0) {
            removeRow(0);
        }
    }
    
    private void insertRow(ArgsTableRow row) {
        String[] values = new String[]{ row.getName(), row.getDesc() };
        addRow(values);
    }
    
    /** */
    private void updateRow(int rowIndex,ArgsTableRow row) {
        setValueAt(row.getName(),rowIndex,COLUMN_NAME);
        setValueAt(row.getDesc(),rowIndex,COLUMN_DESC);
    }
    
    /** */
    void setRows(List<ArgsTableRow> rows) {
        this.rows = rows;
        settingRows = true;
        
        int size = rows.size();
    // update, insert
        for (int index = 0; index < size; index++) {
            if (index >= getRowCount()) {
                insertRow(rows.get(index));
            }
            else {
                updateRow(index,rows.get(index));
            }
        }
        
    // remove
        while (getRowCount() > size) {
            removeRow(size);
        }
        settingRows = false;
    }
}