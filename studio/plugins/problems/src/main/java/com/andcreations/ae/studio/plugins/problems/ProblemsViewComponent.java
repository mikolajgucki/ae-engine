package com.andcreations.ae.studio.plugins.problems;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXTreeTable;

import com.andcreations.ae.studio.plugins.ui.common.JTableColumnsSizeSetter;

/**
 * The problems view component.
 *
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class ProblemsViewComponent extends JPanel {
    /** */
    private ProblemsTreeTableModel model;
    
    /** */
    private JXTreeTable table;    
    
    /** */
    ProblemsViewComponent(ProblemsTreeTableModel model) {
        this.model = model;
        create();
    }
    
    /** */
    private void create() {
        setLayout(new BorderLayout());
        
    // table
        table = new JXTreeTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        createTableMouseListener();
        JTableColumnsSizeSetter.set(table,new double[]{10,6,2});
        
    // table scroll
        JScrollPane tableScroll = new JScrollPane(table);
        add(tableScroll,BorderLayout.CENTER);
    }
    
    /** */
    private void createTableMouseListener() {
        table.addMouseListener(new MouseAdapter() {
            /** */
            @Override
            public void mousePressed(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    int row = table.rowAtPoint(event.getPoint());
                    rowDoubleClicked(row);
                }
            }
        });
    }   
    
    /** */
    private void rowDoubleClicked(int row) {
        TreePath path = table.getPathForRow(row);
        Object object = path.getLastPathComponent();
        
    // problem clicked
        if (object instanceof Problem) {
            Problem problem = (Problem)object;
            problem.notifyDoubleClicked();
        }
    }
}