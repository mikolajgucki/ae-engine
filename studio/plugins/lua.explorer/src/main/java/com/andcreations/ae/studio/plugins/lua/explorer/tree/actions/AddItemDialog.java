package com.andcreations.ae.studio.plugins.lua.explorer.tree.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.lua.explorer.tree.actions.resources.R;
import com.andcreations.ae.studio.plugins.lua.validation.LuaFileNameVerifier;
import com.andcreations.ae.studio.plugins.project.files.ProjectFiles;
import com.andcreations.ae.studio.plugins.ui.common.Dialog;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.common.layout.FormLayoutBuilder;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.io.FileNode;
import com.andcreations.resources.BundleResources;
import com.andcreations.resources.ResourceLoader;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public abstract class AddItemDialog extends Dialog {
    /** */
    private BundleResources res = new BundleResources(AddItemDialog.class);  
    
    /** */
    private JPanel panel;
    
    /** */
    private JLabel itemLabel;
    
    /** */
    private JLabel itemValue;
    
    /** */
    protected JComboBox<File> directoryCombo;
    
    /** */
    protected JTextField nameField;
    
    /** */
    private LuaFileNameVerifier nameVerifier;
    
    /** */
    private JLabel statusLabel;
    
    /** */
    private JButton addButton;
    
    /** */
    private JButton cancelButton;   
    
    /** */
    protected String itemPrefix;
    
    /** */
    public AddItemDialog() {
        super(MainFrame.get(),"",true);
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
                addItem();
            }
        };
        
    // item
        itemValue = new JLabel();
        
    // directory
        directoryCombo = new JComboBox<File>();
        
    // name
        nameField = new JTextField();
        nameField.addActionListener(addItemListener);
        nameVerifier = new LuaFileNameVerifier(nameField,
            res.getStr("invalid.name"),false);
        createDocumentListener();
      
    // add
        addButton = new JButton(res.getStr("add"));
        addButton.addActionListener(addItemListener);
        
    // cancel
        cancelButton = new JButton(res.getStr("cancel"));
        
    // layout and create
        panel = layoutComponents();        
        create(panel,addButton,cancelButton);
    }
    
    /** */
    private void createDocumentListener() {
        DocumentListener documentListener = new DocumentListener() {
            /** */
            @Override
            public void changedUpdate(DocumentEvent event) {
                nameChanged();
            }
            
            /** */
            @Override
            public void removeUpdate(DocumentEvent event) {
                nameChanged();
            }
            
            /** */
            @Override
            public void insertUpdate(DocumentEvent event) {
                nameChanged();
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
        
        itemLabel = builder.addLabel(" ","ml");
        builder.add(itemValue,"mv");
        builder.addLabel(res.getStr("directory"),"dl");
        builder.add(directoryCombo,"dv");
        builder.addLabel(res.getStr("name"),"nl");
        builder.add(nameField,"nv");        
        statusLabel = builder.addLabel(" ","sl");
        
        return builder.getPanel();
    }
    
    /** */
    protected void setItemLabel(String label) {
        itemLabel.setText(label);
    }
    
    /** */
    protected void setError(String text) {
        statusLabel.setText(text);
        statusLabel.setIcon(Icons.getIcon(DefaultIcons.ERROR));        
    }
    
    /** */
    protected boolean verify() {
    // verify name
        boolean ok = nameVerifier.verify();
        if (ok == false) {
            setError(nameVerifier.getErrorToolTip());
        }
        else {
            statusLabel.setText(" ");
            statusLabel.setIcon(null);
        }
        
        return ok;
    }
    
    /** */
    private void nameChanged() {
        itemValue.setText(getItemValue());
        
        boolean ok = verify();
    // add button
        addButton.setEnabled(ok);
    }
    
    /** */
    public void showDialog(FileNode node) {
    // item prefix
        itemPrefix = node.getPath();
        itemValue.setText(itemPrefix);
        
    // combo
        List<File> dirs = new ArrayList<>(node.getFiles());
        ProjectFiles.get().sort(dirs);
        directoryCombo.removeAllItems();
        for (File dir:dirs) {
            directoryCombo.addItem(dir);
        }
        
    // name
        nameField.setText("untitled");
        
    // focus
        if (dirs.size() == 1) {
            UICommon.invoke(new Runnable() {
                /** */
                @Override
                public void run() {
                    nameChanged();
                    nameField.requestFocus();
                    nameField.selectAll();
                }
            });
        }
        
    // show
        showDialog();
    }
    
    /** */
    protected abstract String getItemValue();
    
    /** */
    protected abstract void addItem();
}