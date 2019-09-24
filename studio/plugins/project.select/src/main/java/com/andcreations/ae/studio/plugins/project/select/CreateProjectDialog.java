package com.andcreations.ae.studio.plugins.project.select;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.project.select.resources.R;
import com.andcreations.ae.studio.plugins.project.select.validation.ProjectNameVerifier;
import com.andcreations.ae.studio.plugins.ui.common.Dialog;
import com.andcreations.ae.studio.plugins.ui.common.layout.FormLayoutBuilder;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.resources.BundleResources;
import com.andcreations.resources.ResourceLoader;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class CreateProjectDialog extends Dialog {
    /** */
    private BundleResources res =
        new BundleResources(CreateProjectDialog.class);
    
    /** */
    private JTextField dirField;        
    
    /** */
    private JTextField nameField;        
    
    /** */
    private JButton browseButton;
        
    /** */
    private JLabel statusLabel;    
    
    /** */
    private JButton createButton;
    
    /** */
    private JButton cancelButton;
    
    /** */
    private ProjectNameVerifier nameVerifier;
    
    /** */
    private DocumentListener documentListener;    
        
    /** */
    CreateProjectDialog() {
        super((Frame)null,null,true);
        setTitle(res.getStr("title"));
        create();
    }
    
    /** */
    private void create() {
        makeEscapable();
        createDocumentListener();
        
    // directory
        dirField = new JTextField();
        dirField.getDocument().addDocumentListener(documentListener); 
        
    // browse
        browseButton = new JButton(res.getStr("browse"));
        browseButton.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                chooseDir();
            }
        });

    // name
        nameField = new JTextField();
        nameField.getDocument().addDocumentListener(documentListener); 
        nameVerifier = new ProjectNameVerifier(nameField,
            res.getStr("invalid.name",ProjectNameVerifier.getAllowCharsMsg()));
        
    // create
        createButton = new JButton(res.getStr("create"));
        
    // cancel
        cancelButton = new JButton(res.getStr("cancel"));
        
    // layout and create
        JPanel panel = layoutComponents();        
        create(panel,createButton,cancelButton);           
    }

    /** */
    private void createDocumentListener() {
        documentListener = new DocumentListener() {
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
    }
    
    /** */
    private JPanel layoutComponents() {
        FormLayoutBuilder builder;
        try {
            String src = ResourceLoader.loadAsString(
                R.class,"CreateProjectDialog.formlayout");
            builder = new FormLayoutBuilder(src);
        } catch (IOException exception) {
            Log.error("Failed to read form layout: " + exception.getMessage());
            return null;
        }
        
        builder.addLabel(res.getStr("directory"),"dl");
        builder.add(dirField,"d");
        builder.add(browseButton,"b");        
        builder.add(nameField,"n");
        builder.addLabel(res.getStr("name"),"nl");
        statusLabel = builder.addLabel(" ","sl");
        
        return builder.getPanel();
    }    
    
    /** */
    private void chooseDir() {
        JFileChooser chooser = new JFileChooser(dirField.getText());
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
        
        int value = chooser.showOpenDialog(null);
        if (value == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            dirField.setText(path);
            dirField.selectAll();
            dirField.requestFocus();
        }        
    }
    
    /** */
    private void setError(String text) {
        statusLabel.setText(text);
        statusLabel.setIcon(Icons.getIcon(DefaultIcons.ERROR));  
        createButton.setEnabled(false);
    }
    
    /** */
    private void setWarning(String text) {
        statusLabel.setText(text);
        statusLabel.setIcon(Icons.getIcon(DefaultIcons.WARNING));
        createButton.setEnabled(true);
    }
    
    /** */
    private void verify() {
        File projectDir = getProjectDir();
    // must be a directory
        if (projectDir.exists() == true) {
            if (projectDir.isFile() == true) {
                setError(res.getStr("project.dir.is.file"));
                return;
            }
            if (projectDir.isDirectory() == true) {
                setWarning(res.getStr("project.dir.exists"));
                return;
            }
        }
        
    // project name
        if (nameVerifier.verify() == false) {
            setError(nameVerifier.getErrorToolTip());
            return;
        }
        
    // no issues
        statusLabel.setText(" ");
        statusLabel.setIcon(null);
        createButton.setEnabled(true);
    }
    
    /** */
    File getProjectDir() {
        return new File(dirField.getText());
    }
    
    /** */
    String getAppName() {
        return nameField.getText();
    }        
    
    /** */
    boolean showDialog(File projectDir) {
        dirField.setText(projectDir.getAbsolutePath());
        nameField.setText(res.getStr("untitled"));
        nameField.selectAll();
        nameField.requestFocus();
        nameField.requestFocusInWindow();
        
        if (showDialog() == createButton) {
            return true;
        }
        return false;
    }
}