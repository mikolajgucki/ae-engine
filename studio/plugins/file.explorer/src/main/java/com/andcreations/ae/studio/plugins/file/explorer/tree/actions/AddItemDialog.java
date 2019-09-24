package com.andcreations.ae.studio.plugins.file.explorer.tree.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.file.explorer.tree.actions.resources.R;
import com.andcreations.ae.studio.plugins.file.validation.FileNameVerifier;
import com.andcreations.ae.studio.plugins.ui.common.Dialog;
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
class AddItemDialog extends Dialog {
    /** */
    private BundleResources res = new BundleResources(AddItemDialog.class);
    
    /** */
    private JLabel dirLabel;
    
    /** */
    private JTextField nameField;
    
    /** */
    private JLabel statusLabel;
    
    /** */
    private JButton addButton;
    
    /** */
    private JButton cancelButton;  
    
    /** */
    private FileNameVerifier nameVerifier;
    
    /** */
    AddItemDialog(String title) {
        super(MainFrame.get(),title,true);
        create();
    }
    
    /** */
    private void create() {
        makeEscapable();
        
    // listener
        ActionListener addItemListener = new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                addButton.doClick();
            }
        };
        
    // name
        nameField = new JTextField();
        nameField.addActionListener(addItemListener);
        nameVerifier = new FileNameVerifier(nameField,
            res.getStr("invalid.name",FileNameVerifier.getAllowCharsMsg()));
        createDocumentListener();
        
    // add
        addButton = new JButton(res.getStr("add"));
        
    // cancel
        cancelButton = new JButton(res.getStr("cancel"));  
        
    // layout and create
        JPanel panel = layoutComponents();        
        create(panel,addButton,cancelButton);        
    }
    
    /** */
    private void createDocumentListener() {
        DocumentListener documentListener = new DocumentListener() {
            /** */
            @Override
            public void changedUpdate(DocumentEvent event) {
                verify();
            }
            
            /** */
            @Override
            public void removeUpdate(DocumentEvent event) {
                verify();
            }
            
            /** */
            @Override
            public void insertUpdate(DocumentEvent event) {
                verify();
            }            
        };
        nameField.getDocument().addDocumentListener(documentListener);              
    }     
    
    /** */
    private JPanel layoutComponents() {
        FormLayoutBuilder builder;
        try {
            String src = ResourceLoader.loadAsString(
                R.class,"AddItemDialog.formlayout");
            builder = new FormLayoutBuilder(src);
        } catch (IOException exception) {
            Log.error("Failed to read form layout: " + exception.getMessage());
            return null;
        }
        
        builder.addLabel(res.getStr("dir"),"dl");
        dirLabel = builder.addLabel(" ","dv");
        builder.addLabel(res.getStr("name"),"nl");
        builder.add(nameField,"iv");
        statusLabel = builder.addLabel(" ","sl");
        
        return builder.getPanel();
    }    
    
    /** */
    private boolean verify() {
    // verify name
        boolean ok = nameVerifier.verify();
        if (ok == false) {
            statusLabel.setText(nameVerifier.getErrorToolTip());
            statusLabel.setIcon(Icons.getIcon(DefaultIcons.ERROR)); 
        }
        else {
            statusLabel.setText(" ");
            statusLabel.setIcon(null);
        }
        
        return ok;
    }
    
    /** */
    public String showDialog(String dir) {
    // directory
        dirLabel.setText(dir);
        
    // name
        nameField.setText(res.getStr("untitled"));      
        nameField.requestFocus();
        nameField.selectAll();
        
    // show
        if (showDialog() == addButton) {
            return nameField.getText();
        }
        
        return null;
    }
}