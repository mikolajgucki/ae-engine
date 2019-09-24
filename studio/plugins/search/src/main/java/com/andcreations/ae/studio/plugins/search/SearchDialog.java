package com.andcreations.ae.studio.plugins.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.project.ProjectMenu;
import com.andcreations.ae.studio.plugins.search.resources.R;
import com.andcreations.ae.studio.plugins.ui.common.Dialog;
import com.andcreations.ae.studio.plugins.ui.common.UIKeys;
import com.andcreations.ae.studio.plugins.ui.common.layout.FormLayoutBuilder;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.resources.BundleResources;
import com.andcreations.resources.ResourceLoader;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class SearchDialog extends Dialog {
    /** */
    private BundleResources res = new BundleResources(SearchDialog.class);  
    
    /** */
    private JTextField searchTextField;
    
    /** */
    private JCheckBox matchCaseBox;
    
    /** */
    private JCheckBox regexBox;
    
    /** */
    private JButton searchButton;
    
    /** */
    private JButton cancelButton;
    
    /** */
    SearchDialog() {
        super(MainFrame.get(),"",false);
        create();
    }
    
    /** */
    private void create() {
        setTitle(res.getStr("title"));
        makeEscapable();
        
    // search text
        searchTextField = new JTextField();
        searchTextField.addFocusListener(new FocusListener() {
            /** */
            @Override
            public void focusGained(FocusEvent event) {
                searchTextField.selectAll();
            }
            
            /** */
            @Override
            public void focusLost(FocusEvent event) {
            }
        });
        searchTextField.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                search();
            }
        });
        createDocumentListener();
        
    // match case
        matchCaseBox = new JCheckBox(res.getStr("match.case"));
        
    // regex
        regexBox = new JCheckBox(res.getStr("regex"));
        
    // search
        searchButton = new JButton(res.getStr("search"));
        searchButton.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                search();
            }
        });
        
    // cancel
        cancelButton = new JButton(res.getStr("cancel"));
        cancelButton.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                setVisible(false);
            }
        });
        
    // layout
        layoutComponents();
        pack();        
        searchTextChanged();
    }
    
    /** */
    private void createDocumentListener() {
        DocumentListener documentListener = new DocumentListener() {
            /** */
            @Override
            public void changedUpdate(DocumentEvent event) {
                searchTextChanged();
            }
            
            /** */
            @Override
            public void removeUpdate(DocumentEvent event) {
                searchTextChanged();
            }
            
            /** */
            @Override
            public void insertUpdate(DocumentEvent event) {
                searchTextChanged();
            }            
        };
        searchTextField.getDocument().addDocumentListener(documentListener);              
    }
    
    /** */
    private void layoutComponents() {
        FormLayoutBuilder builder;
        try {
            String src = ResourceLoader.loadAsString(
                R.class,"SearchDialog.formlayout");
            builder = new FormLayoutBuilder(src);
        } catch (IOException exception) {
            Log.error("Failed to read form layout " + exception.getMessage());
            return;
        }        
        
        builder.addLabel(res.getStr("search.text"),"l");
        builder.add(searchTextField,"f");
        builder.add(matchCaseBox,"m");
        builder.add(searchButton,"s");
        builder.add(cancelButton,"c");
        
        // Uncomment when the regex matcher (RegexSearchMatcher.java) is fixed.
        //builder.add(regexBox,"r");
        
        getContentPane().add(builder.getPanel());
    }
    
    /** */
    void addMenuItem() {
        JMenuItem item = new JMenuItem(res.getStr("menu.text"));
        item.setIcon(Icons.getIcon(DefaultIcons.SEARCH));
        item.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_F,UIKeys.menuKeyMask() | KeyEvent.SHIFT_DOWN_MASK));
        item.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                if (isVisible() == false) {
                    center(MainFrame.get(),SearchDialog.this);
                    setVisible(true);
                }
            }
        });
        
        ProjectMenu.get().add(item);        
    }

    /** */
    private void searchTextChanged() {
        String text = searchTextField.getText();
        searchButton.setEnabled(text.length() > 0);
    }
    
    /** */
    private void search() {
        if (Search.get().search(searchTextField.getText(),
            matchCaseBox.isSelected(),regexBox.isSelected()) == true) {
        //
            setVisible(false);
        }
    }
}