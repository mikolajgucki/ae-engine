package com.andcreations.ae.studio.plugins.file.explorer.tree.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.andcreations.ae.studio.plugins.file.explorer.tree.actions.resources.R;
import com.andcreations.ae.studio.plugins.file.validation.FileNameVerifier;
import com.andcreations.ae.studio.plugins.ui.common.Dialog;
import com.andcreations.ae.studio.plugins.ui.common.layout.FormLayoutUtil;
import com.andcreations.ae.studio.plugins.ui.common.window.JComponentRequestFocusWindowAdapter;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class RenameDialog extends Dialog {
    /** */
    private BundleResources res = new BundleResources(RenameDialog.class);      
    
    /** */
    private JLabel oldNameLabel;
    
    /** */
    private JTextField newNameField;
    
    /** */
    private FileNameVerifier newNameVerifier;
    
    /** */
    private JLabel statusLabel;
    
    /** */
    private JButton renameButton;
    
    /** */
    private JButton cancelButton;    
    
    /** */
    RenameDialog() {
        super(MainFrame.get(),"",true);
        create();
    }
    
    /** */
    private void create() {
        makeEscapable();    
        setTitle(res.getStr("title"));
        
    // old name
        oldNameLabel = new JLabel();
        
    // new name
        newNameField = new JTextField();
        newNameField.addActionListener(new ActionListener() {
            /** */  
            @Override
            public void actionPerformed(ActionEvent event) {
                renameButton.doClick();
            }
        });  
        newNameVerifier = new FileNameVerifier(newNameField,
            res.getStr("invalid.new.name"));
        createDocumentListener();
        
    // rename
        renameButton = new JButton(res.getStr("rename"));
        
    // cancel
        cancelButton = new JButton(res.getStr("cancel"));
        
    // layout, create
        JPanel panel = layoutComponents();
        create(panel,new JButton[]{renameButton,cancelButton});
        
    // window listener
        addWindowListener(
            new JComponentRequestFocusWindowAdapter(newNameField));        
    }
    
    /** */
    private void createDocumentListener() {
        DocumentListener documentListener = new DocumentListener() {
            /** */
            @Override
            public void changedUpdate(DocumentEvent event) {
                newNameChanged();
            }
            
            /** */
            @Override
            public void removeUpdate(DocumentEvent event) {
                newNameChanged();
            }
            
            /** */
            @Override
            public void insertUpdate(DocumentEvent event) {
                newNameChanged();
            }            
        };
        newNameField.getDocument().addDocumentListener(documentListener);              
    }    
    
    /** */
    private JPanel layoutComponents() {
        FormLayoutUtil layout = new FormLayoutUtil(
            R.class,"RenameDialog.formlayout");
        
    // old name
        layout.addLabelText("ol",res.getStr("old.name"));
        layout.addComponent("ov",oldNameLabel);
        
    // new name
        layout.addLabelText("nl",res.getStr("new.name"));
        layout.addComponent("nv",newNameField);
        
    // status
        layout.addLabelText("sl"," ");
        
    // layout
        JPanel panel = layout.build();
        
    // status
        statusLabel = layout.getLabel("sl");
        
        return panel;
    }    
    
    /** */
    private void setError(String msg) {
        statusLabel.setText(msg);
        statusLabel.setIcon(Icons.getIcon(DefaultIcons.ERROR));
    }
    
    /** */
    private void verify() {
        boolean ok = true;
        
    // verify new name
        if (newNameVerifier.verify() == false) {
            setError(newNameVerifier.getErrorToolTip());
            ok = false;
        }       
    // no issue
        if (ok == true) { 
            statusLabel.setText(" ");
            statusLabel.setIcon(null);
        }
        
        renameButton.setEnabled(ok);        
    }
    
    /** */
    private void newNameChanged() {
        verify();
    }
    
    /** */
    String getNewName() {
        return newNameField.getText();
    }
    
    /** */
    boolean showRenameDialog(File src) {
    // old name
        oldNameLabel.setText(src.getName());
        
    // new name
        newNameField.setText(src.getName());
        newNameField.selectAll();
        newNameField.grabFocus();
        
    // verify
        verify();
        
    // show
        return showDialog() == renameButton;
    }
}