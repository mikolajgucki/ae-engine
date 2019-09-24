package com.andcreations.ae.studio.plugins.project.select;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.andcreations.ae.studio.plugins.ui.common.Dialog;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class ProjectSelectDialog extends Dialog {
    /** */
    static enum Option {
        /** */
        OK,
        
        /** */
        CANCEL,
        
        /** */
        CREATE_PROJECT;
    }
    
    /** */
    private final BundleResources res =
        new BundleResources(ProjectSelectDialog.class);      
    
    /** */
    private JComboBox<String> projectDirCombo;
    
    /** */
    private JButton okButton;
    
    /** */
    private JButton createProjectButton;
    
    /** */
    ProjectSelectDialog(String selectedProjectDir,List<String> projectDirs) {
        super((Frame)null,null,true);
        setTitle(res.getStr("title"));
        create(selectedProjectDir,projectDirs);
    }
    
    /** */
    private void setEditedText(String text) {
        projectDirCombo.getEditor().setItem(text);
    }
    
    /** */
    private void selectAll() {
        projectDirCombo.getEditor().selectAll();
    }
    
    /** */
    private void chooseDir() {
        JFileChooser chooser = new JFileChooser(getProjectDir());
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
        
        int value = chooser.showOpenDialog(null);
        if (value == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            setEditedText(path);
            selectAll();
            projectDirCombo.requestFocus();
        }       
    }
    
    /** */
    private void create(String selectedProjectDir,List<String> projectDirs) {
        JPanel panel = new JPanel(new BorderLayout());
        
    // prompt
        JLabel promptLabel = new JLabel(res.getStr("prompt"));
        panel.add(promptLabel,BorderLayout.NORTH);
        
    // project directory field
        projectDirCombo = new JComboBox<String>();
        projectDirCombo.setEditable(true);
        projectDirCombo.setPrototypeDisplayValue(
            "/home/foobar/develop/projects/awesome/project");
        panel.add(projectDirCombo,BorderLayout.CENTER);
        projectDirCombo.getEditor().selectAll();
        projectDirCombo.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
            }            
        });
        
    // add project directories
        for (String projectDir:projectDirs) {
            projectDirCombo.addItem(projectDir);
        }
        
    // select the first projet directory
        String projectDir = selectedProjectDir;
        if (projectDir == null && projectDirs.size() >= 1) {
            projectDir = projectDirs.get(0);
        }
        if (projectDir == null) {
            projectDir = "";
        }
        setEditedText(projectDir);
        selectAll();
       
        projectDirCombo.getEditor().addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                close(okButton);
            }
        });
        
    // browse button
        JButton browseButton = new JButton(res.getStr("browse"));
        browseButton.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                chooseDir();
            }
        });
        panel.add(browseButton,BorderLayout.EAST);
        
    // ok button
        okButton = new JButton(res.getStr("ok"));
        
    // cancel button
        JButton cancelButton = new JButton(res.getStr("cancel"));
        
    // create project button
        createProjectButton = new JButton(res.getStr("create.project"));
        
    // create
        create(panel,new JButton[]{okButton,cancelButton,createProjectButton});
        makeEscapable();
    }
    
    /** */
    File getProjectDir() {
        String text = (String)projectDirCombo.getSelectedItem();
        if (text == null) {
            text = "";
        }
        return new File(text);
    }
    
    /** */
    Option showProjectSelectDialog() {
        JButton button = showDialog();
        if (button == okButton) {
            return Option.OK;
        }
        if (button == createProjectButton) {
            return Option.CREATE_PROJECT;
        }
        
        return Option.CANCEL;
    }    
}