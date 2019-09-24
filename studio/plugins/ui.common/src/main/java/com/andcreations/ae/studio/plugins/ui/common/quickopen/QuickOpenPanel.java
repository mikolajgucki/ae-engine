package com.andcreations.ae.studio.plugins.ui.common.quickopen;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class QuickOpenPanel extends JPanel {
    /** */
    private static final int DEFAULT_LIMIT = 50;
    
    /** */
    private BundleResources res = new BundleResources(QuickOpenPanel.class);      
    
    /** */
    private List<QuickOpenItem> items;
    
    /** */
    private QuickOpenPanelListener listener;
    
    /** */
    private JLabel patternLabel;
    
    /** */
    private JTextField patternField;
    
    /** */
    private JLabel info;
    
    /** */
    private DefaultListModel<QuickOpenItem> model;
    
    /** */
    private DefaultListModel<QuickOpenItem> allItemsModel;
    
    /** */
    private JList<QuickOpenItem> list;
    
    /** */
    private QuickOpenMatcher matcher;
    
    /** */
    private int limit = DEFAULT_LIMIT;
    
    /** */
    public QuickOpenPanel(List<QuickOpenItem> items,
        QuickOpenPanelListener listener) {
    //
        this.items = new ArrayList<>(items);
        this.listener = listener;
        create();
    }
    
    /** */
    private void create() {
    // UI
        setLayout(new BorderLayout());
        add(createPatternField(),BorderLayout.NORTH);
        add(createList(),BorderLayout.CENTER);

    // default matcher
        setMatcher(res.getStr("pattern.field.label"),
            new DefaultQuickOpenMatcher());
    }
    
    /** */
    private void setBorder(JPanel panel) {
        panel.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
    }
    
    /** */
    private JPanel createPatternField() {
        JPanel panel = new JPanel(new BorderLayout());
        setBorder(panel);
        
    // info
        patternLabel = new JLabel();
        panel.add(patternLabel,BorderLayout.NORTH);
        
    // field
        patternField = new JTextField();
        panel.add(patternField,BorderLayout.CENTER);
        
    // field listeners
        patternField.getDocument().addDocumentListener(new DocumentListener() {
            /** */
            @Override
            public void changedUpdate(DocumentEvent event) {
                updateModel();
            }
            
            /** */
            @Override
            public void removeUpdate(DocumentEvent evente) {
                updateModel();
            }
            
            /** */
            @Override
            public void insertUpdate(DocumentEvent event) {
                updateModel();
            }                
        });
        patternField.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                pickSelectedItem();
            }
        });
        patternField.addKeyListener(new KeyAdapter() {
            /** */
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_DOWN) {
                    moveDown();
                }
                
                if (event.getKeyCode() == KeyEvent.VK_UP) {
                    moveUp();
                }
            }
        });
        
        return panel;
    }
    
    /** */
    private JPanel createList() {
        JPanel panel = new JPanel(new BorderLayout());
        setBorder(panel);
        
    // info
        info = new JLabel(res.getStr("matching.items"));
        panel.add(info,BorderLayout.NORTH);
        
    // model
        model = new DefaultListModel<QuickOpenItem>();
        allItemsModel =  new DefaultListModel<QuickOpenItem>();
        for (QuickOpenItem item:items) {
            allItemsModel.addElement(item);
        }
        
    // list
        list = new JList<QuickOpenItem>(model);
        list.setCellRenderer(new QuickOpenListCellRenderer());
        updateModel();
        
    // list scroll
        JScrollPane listScrollPane = new JScrollPane(list);
        panel.add(listScrollPane,BorderLayout.CENTER);
        
    // selection
        list.getSelectionModel().setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION);
        
    // selection listener
        list.getSelectionModel().addListSelectionListener(
            new ListSelectionListener() {
            /** */
            @Override
            public void valueChanged(ListSelectionEvent event) {
                listSelectionChanged();
            }
        });
        
    // mouse listener
        list.addMouseListener(new MouseAdapter() {
            /** */
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    pickSelectedItem();
                }
            }
        });
        
        return panel;
    }
    
    /** */
    private void listSelectionChanged() {
        listener.quickOpenItemSelected(list.getSelectedValue());
    }
    
    /** */
    private void goToItem(int index) {
        if (index < 0) {
            return;
        }
        list.setSelectedIndex(index);
    }
    
    /** */
    private void updateModel() {
        String value = patternField.getText();
        info.setText(res.getStr("matching.items"));
        
    // nothing to match with
        if (value.length() == 0) {
            list.setModel(allItemsModel);
            return;
        }        
        
        model.clear();
        list.setModel(model);
        
        int count = 0;
        for (QuickOpenItem item:items) {
            if (matcher.match(item.getSearchValue(),value)) {
                model.addElement(item);
                count++;
                if (count == limit) {
                    break;
                }
            }
        }
        goToItem(0);
        
    // if hit limit
        if (count == limit) {
            info.setText(res.getStr("matching.items.limited",
                Integer.toString(limit)));
        }
    }
    
    /** */
    private void moveDown() {
        int index = list.getSelectionModel().getMinSelectionIndex();
        goToItem(index + 1);
    }
    
    /** */
    private void moveUp() {
        int index = list.getSelectionModel().getMinSelectionIndex();
        goToItem(index - 1);            
    }
    
    /** */
    private void pickSelectedItem() {
        QuickOpenItem selected = list.getSelectedValue();
        if (selected == null) {
            return;
        }
        listener.quickOpenItemPicked(selected);
    }
    
    /**
     * Gets the matcher.
     *
     * @return The matcher.
     */
    public QuickOpenMatcher getMatcher() {
        return matcher;
    }
    
    /** 
     * Gets the matcher label.
     *
     * @return The matcher label.
     */
    public String getMatcherLabel() {
        return patternLabel.getText();
    }
    
    /**
     * Sets the matcher.
     *
     * @param label The label displayed above the pattern field.
     * @param matcher The matcher.
     */
    public void setMatcher(String label,QuickOpenMatcher matcher) {
        patternLabel.setText(label);
        this.matcher = matcher;
    }
}