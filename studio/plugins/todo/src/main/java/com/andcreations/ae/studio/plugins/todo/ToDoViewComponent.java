package com.andcreations.ae.studio.plugins.todo;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.jdesktop.swingx.JXTable;

import com.andcreations.ae.studio.plugins.ui.common.JTableUtil;

/**
 * The todo view top component.
 *
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class ToDoViewComponent extends JPanel {
    /** */
    private ToDoTableModel model;
        
    /** */
    private JXTable table;
        
    /** */
    private AncestorListener ancestorListener;
    
    /** */
    ToDoViewComponent() {
        create();
    }
    
    /** */
    private void create() {
        setLayout(new BorderLayout());
        
    // model
        model = new ToDoTableModel(); 
        
    // table
        table = new JXTable();
        table.setModel(model);
        table.setEditable(false);
        table.setSortable(false);
        table.setShowGrid(false);
        createTableMouseListener();
        createTableAbcestorListner();
        
    // remove the item column
        table.removeColumn(table.getColumnModel().getColumn(
            ToDoTableModel.ITEM_COLUMN_INDEX));
        
    // table scroll
        JScrollPane tableScroll = new JScrollPane(table);
        add(tableScroll,BorderLayout.CENTER);
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
        
        JTableUtil.setColumnWidthsByRatio(table,1,10,6,2);
    }
    
    /** */
    private void createTableMouseListener() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    setTableColumnSizes();
                    int row = table.rowAtPoint(event.getPoint());
                    ToDoItem item = model.getItemByRow(row);
                    if (item != null) {
                        item.notifyDoubleClicked();
                    }
                }
            }
        });
    }
    
    /** */
    private void createTableAbcestorListner() {
        ancestorListener = new AncestorListener() {
            /** */                
            @Override
            public void ancestorAdded(AncestorEvent e) {
                setTableColumnSizes();
            }
            
            /** */
            @Override
            public void ancestorMoved(AncestorEvent e) {
            }
            
            /** */
            @Override
            public void ancestorRemoved(AncestorEvent e) {
            }
        };
        table.addAncestorListener(ancestorListener);        
    }
    
    /** */
    void addItem(ToDoItem item) {
        model.addItem(item);
    }
    
    /** */
    public void removeItem(ToDoItem item) {
        model.removeItem(item);
    }    
}