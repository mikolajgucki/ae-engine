package com.andcreations.ae.studio.plugins.lua.debug.breakpoints;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.jdesktop.swingx.JXTable;

import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.ui.common.JTableColumnsSizeSetter;
import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class LuaBreakpointsViewComponent extends JPanel {
    /** */
    private BundleResources res = new BundleResources(
        LuaBreakpointsViewComponent.class);          
    
    /** */
    private LuaBreakpointsViewComponentListener listener;
    
    /** */
    private LuaBreakpointsTableModel model;
    
    /** */
    private JXTable table;         
    
    /** */ 
    private String dark;
    
    /** */
    LuaBreakpointsViewComponent(LuaBreakpointsViewComponentListener listener) {
        this.listener = listener;
        create();
    }
    
    /** */
    private void create() {
        dark = UIColors.toHex(UIColors.dark());
        setLayout(new BorderLayout());
        
    // model
        model = new LuaBreakpointsTableModel();
        model.addTableModelListener(new TableModelListener() {
            /** */
            @Override
            public void tableChanged(TableModelEvent event) {
                if (event.getColumn() == LuaBreakpointsTableModel.ENABLED_INDEX) {
                    int row = event.getFirstRow();
                    LuaBreakpoint breakpoint = model.getBreakpoint(row);
                    breakpoint.setEnabled(model.isEnabled(row));
                    listener.breakpointStateChanged(breakpoint);
                }
            }
        });
        
    // table
        table = new JXTable(model);
        table.setSortable(false);
        table.setShowGrid(false);
        table.getColumnModel().getColumn(2).setCellRenderer(
            new LuaBreakpointsTableCellRenderer());
        createTableMouseListener();
        JTableColumnsSizeSetter.set(table,new double[]{1,10}); 
        
        /** */
        table.addKeyListener(new KeyAdapter() {
            /** */
            public void keyPressed(KeyEvent event) {
                int[] rows = table.getSelectedRows();
                List<LuaBreakpoint> selected = new ArrayList<>();
                for (int row:rows) {
                    selected.add(model.getBreakpoint(row));
                }                                    
                    
            // delete
                if (event.getKeyCode() == KeyEvent.VK_DELETE) {                    
                    for (LuaBreakpoint breakpoint:selected) {
                        listener.removeBreakpoint(breakpoint);
                    }
                }
                
            // enable/disable
                if (event.getKeyCode() == KeyEvent.VK_SPACE) {
                    for (int row:rows) {
                        model.toggleEnabled(row);
                    }
                }
            }
        });
        
    // remove the breakpoint column
        table.removeColumn(table.getColumnModel().getColumn(
            LuaBreakpointsTableModel.BREAKPOINT_INDEX));
        
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
                    if (row < 0) {
                        return;
                    }
                    Object object = model.getValueAt(
                        row,LuaBreakpointsTableModel.BREAKPOINT_INDEX);
                    breakpointDoubleClicked((LuaBreakpoint)object);
                }
            }
        });
    }
    
    /** */
    private void breakpointDoubleClicked(LuaBreakpoint breakpoint) {
        int line = breakpoint.getLine();
        LuaFile.edit(breakpoint.getFile(),line,line + 8);
    }
    
    /** */
    private void sort(List<LuaBreakpoint> breakpoints) {
        Collections.sort(breakpoints,new Comparator<LuaBreakpoint>() {
            /** */
            @Override
            public boolean equals(Object obj) {
                return this == obj;
            }
            
            /** */
            @Override
            public int compare(LuaBreakpoint a,LuaBreakpoint b) {
                if (a.getSource().equals(b.getSource()) == true) {
                    return a.getLine() - b.getLine();
                }
                
                return a.getSource().compareTo(b.getSource());
            }
        });
    }
    
    /** */
    void updateModel(List<LuaBreakpoint> breakpoints) {
    // clear
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        
        List<LuaBreakpoint> sorted = new ArrayList<>(breakpoints);
        sort(sorted);
        
    // add
        for (LuaBreakpoint breakpoint:sorted) {
            Boolean enabled =
                breakpoint.isEnabled() ? Boolean.TRUE : Boolean.FALSE;
            String location = res.getStr("location",breakpoint.getSource(),
                Integer.toString(breakpoint.getLine()),dark);
            model.addRow(new Object[]{breakpoint,enabled,location});
        }
    }
    
    /** */
    List<LuaBreakpoint> getSelectedBreakpoints() {
        List<LuaBreakpoint> breakpoints = new ArrayList<>();
        
        int[] rows = table.getSelectedRows();
        for (int row:rows) {
            breakpoints.add(model.getBreakpoint(row));
        }
        
        return breakpoints;
    }
}