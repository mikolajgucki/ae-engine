package com.andcreations.ae.studio.plugins.ui.common.dialog;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.andcreations.ae.studio.plugins.ui.common.Dialog;
import com.andcreations.ae.studio.plugins.ui.common.dialog.resources.R;
import com.andcreations.ae.studio.plugins.ui.common.layout.FormLayoutUtil;
import com.andcreations.ae.studio.plugins.ui.common.window.JComponentRequestFocusWindowAdapter;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public abstract class AbstractCreateFileDialog extends Dialog {
    /** */
    private BundleResources res =
        new BundleResources(AbstractCreateFileDialog.class);  
    
    /** */
    private JLabel pathValue;    
    
    /** */
    private JTextField nameField;    
    
    /** */
    private JLabel statusLabel;    
    
    /** */
    private JButton createButton;
    
    /** */
    private JButton cancelButton;    
    
    /** */
    protected AbstractCreateFileDialog(Frame owner) {
        super(owner,"",true);
    }
    
    /** */
    protected void create(String pathLabelText,String nameLabelText) {
    // path
        pathValue = new JLabel();
        
    // name
        nameField = new JTextField();
        nameField.addActionListener(new ActionListener() {
            /** */  
            @Override
            public void actionPerformed(ActionEvent event) {
                createButton.doClick();
            }
        });
        createDocumentListener();
        
    // create
        createButton = new JButton(res.getStr("create"));
        
    // cancel
        cancelButton = new JButton(res.getStr("cancel"));   
        
    // layout, create
        JPanel panel = layoutComponents(pathLabelText,nameLabelText);
        create(panel,new JButton[]{createButton,cancelButton});
        
    // window listener
        addWindowListener(new JComponentRequestFocusWindowAdapter(nameField));        
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
    private JPanel layoutComponents(String pathLabelText,String nameLabelText) {
        FormLayoutUtil layout = new FormLayoutUtil(
            R.class,"AbstractCreateFileDialog.formlayout");
        
    // path
        layout.addLabelText("pl",pathLabelText);
        layout.addComponent("pv",pathValue);
        
    // name
        layout.addLabelText("nl",nameLabelText);
        layout.addComponent("nv",nameField);
        
    // status
        layout.addLabelText("sl"," ");
        
    // layout
        JPanel panel = layout.build();
        
    // status
        statusLabel = layout.getLabel("sl");
        
        return panel;
    }    
    
    /** */
    protected JTextField getNameField() {
        return nameField;
    }
    
    /** */
    protected String getFileName() {
        return getNameField().getText();
    }

    /** */
    private void nameChanged() {
        nameChanged(nameField.getText());
    }
    
    /** */
    protected void setPath(String path) {
        pathValue.setText(path);
    }
    
    /** */
    protected void setError(String msg) {
        statusLabel.setText(msg);
        statusLabel.setIcon(Icons.getIcon(DefaultIcons.ERROR));        
        createButton.setEnabled(false);
    }
    
    /** */
    protected void setNoError() {
        statusLabel.setText(" ");
        statusLabel.setIcon(null);
        createButton.setEnabled(true);
    }
    
    /** */
    protected boolean showCreateFileDialog() {
    // name
        nameField.setText(res.getStr("untitled"));        
        nameField.selectAll();
        nameField.grabFocus();
        
    // shw
        if (showDialog() == createButton) {            
            return true;
        }
        
        return false;
    }
    
    /** */
    protected abstract void nameChanged(String name);
}