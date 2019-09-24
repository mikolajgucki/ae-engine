package com.andcreations.ae.studio.plugins.lua.templates.args;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;

import com.andcreations.ae.studio.plugins.ui.common.UICommon;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class ArgsTable extends JTable {
    /** */
    private ArgsTableModel model;
    
    /** All the accumulated rows. */
    private List<ArgsTableRow> accumRows = new ArrayList<>();
    
    /** */
    public ArgsTable() {
        create();
    }
    
    /** */
    private void create() {
    // model
        model = new ArgsTableModel(new ArgsTableModelListener() {
            /** */
            @Override
            public void rowChanged(int row) {
                ArgsTable.this.rowChanged(row);
            }
        });
        setModel(model);
    
    // selection
        getSelectionModel().setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION);
        
    // key listener
        addKeyListener(new KeyAdapter() {
            /** */
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                    event.consume();
                    final int row = getSelectedRow();
                    UICommon.invoke(new Runnable() {
                        @Override
                        public void run() {                       
                            editCellAt(row,ArgsTableModel.COLUMN_DESC);
                            ArgsTable.this.getEditorComponent().requestFocus();
                        }
                    });
                }
            }
        });
    }
    
    /** */
    public void clear() {
        accumRows.clear();
        model.clear();
    }
    
    /** */
    private ArgsTableRow findAccumRow(String argName) {
        for (ArgsTableRow row:accumRows) {
            if (row.getName().equals(argName) == true) {
                return row;
            }
        }
        
        return null;
    }
    
    /** */
    private void rowChanged(final int row) {
        System.out.println("rowChanged " + row);
    }
    
    /** */
    public void setArgs(String[] args) {
        if (args == null) {
            model.clear();
            return;
        }
        
        List<ArgsTableRow> rows = new ArrayList<>();
        for (String arg:args) {
            ArgsTableRow row = findAccumRow(arg);
            if (row == null) {
                row = new ArgsTableRow(arg);
                accumRows.add(row);
            }
            rows.add(row);
        }
        model.setRows(rows);
    }
    
    /** */
    public List<ArgsTableRow> getRows(String[] args) {
        List<ArgsTableRow> rows = new ArrayList<>();
        
    // get rows
        if (args != null) {
            for (String arg:args) {
                rows.add(findAccumRow(arg));
            }
        }
        
        return rows;
    }
    
    /** */
    public void stopCellEditing() {
        TableCellEditor editor = getCellEditor();
        if (editor != null) {
            editor.stopCellEditing();
        }
    }
}