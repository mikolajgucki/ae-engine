package com.andcreations.ae.studio.plugins.lua.wizards;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.lua.validation.LuaModuleNameVerifier;
import com.andcreations.ae.studio.plugins.lua.wizards.resources.R;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.project.ProjectProperties;
import com.andcreations.ae.studio.plugins.project.files.ProjectFiles;
import com.andcreations.ae.studio.plugins.ui.common.Dialog;
import com.andcreations.ae.studio.plugins.ui.common.DocumentChangedAdapter;
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
class CreateLuaFileDialog extends Dialog {
    /** */
    private BundleResources res =
        new BundleResources(CreateLuaFileDialog.class);  
    
    /** */
    private JLabel pathValue;
    
    /** */
    private JComboBox<File> dirCombo;
    
    /** */
    private JTextField moduleField;
    
    /** */
    private LuaModuleNameVerifier moduleVerifier;
    
    /** */
    private JLabel statusLabel;
    
    /** */
    private JButton createButton;
    
    /** */
    private JButton cancelButton;
            
    /** */
    CreateLuaFileDialog() {
        super(MainFrame.get(),"",true);
        create();
    }
    
    /** */
    private void create() {
        makeEscapable();
        setTitle(res.getStr("title"));
        
    // path
        pathValue = new JLabel();
        
    // directory
        dirCombo = new JComboBox<File>();
        dirCombo.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                dirChanged();
            }
        });
        
    // module
        moduleField = new JTextField();
        moduleField.addActionListener(new ActionListener() {
            /** */  
            @Override
            public void actionPerformed(ActionEvent event) {
                createButton.doClick();
            }
        });
        moduleVerifier = new LuaModuleNameVerifier(moduleField,
            res.getStr("invalid.module"));
        createDocumentListener();
        
    // create
        createButton = new JButton(res.getStr("create"));
        
    // cancel
        cancelButton = new JButton(res.getStr("cancel"));
        
    // layout, create
        JPanel panel = layoutComponents();
        create(panel,new JButton[]{createButton,cancelButton});
        
    // window listener
        addWindowListener(new JComponentRequestFocusWindowAdapter(moduleField));
    }
    
    /** */
    private void createDocumentListener() {
        moduleField.getDocument().addDocumentListener(
            new DocumentChangedAdapter() {
                /** */
                @Override
                public void documentChanged() {
                    moduleChanged();
                }                
            });
    }    
    
    /** */
    private JPanel layoutComponents() {
        FormLayoutUtil layout = new FormLayoutUtil(
            R.class,"CreateLuaFileDialog.formlayout");
        
    // path
        layout.addLabelText("pl",res.getStr("path"));
        layout.addComponent("pv",pathValue);
        
    // directory
        layout.addLabelText("dl",res.getStr("dir"));
        layout.addComponent("dv",dirCombo);
        
    // module
        layout.addLabelText("ml",res.getStr("module"));
        layout.addComponent("mv",moduleField);
        
    // status
        layout.addLabelText("sl"," ");
        
    // layout
        JPanel panel = layout.build();
        
    // status
        statusLabel = layout.getLabel("sl");
        
        return panel;
    }
    
    /** */
    private String getPath() {
        String module = moduleField.getText();
        return '/' + module.replace('.','/') + LuaFile.DOT_SUFFIX;
    }
    
    /** */
    File getFile() {
        File dir = (File)dirCombo.getSelectedItem();
        return new File(dir,getPath());
    }
    
    /** */
    private void setError(String msg) {
        statusLabel.setText(msg);
        statusLabel.setIcon(Icons.getIcon(DefaultIcons.ERROR));
    }
    
    /** */
    private void verify() {
        boolean ok = true;
        
    // verify module name
        if (moduleVerifier.verify() == false) {
            setError(moduleVerifier.getErrorToolTip());
            ok = false;
        }
        
    // check if module exists
        if (ok == true) {
            String path = getPath();        
            if (ProjectLuaFiles.get().getNodeByPath(path,true) != null) {
                setError(res.getStr("module.exists"));
                ok = false;
            }
        }
        
    // check if file exists
        if (ok == true) {
            File file = getFile();
            if (file.exists() == true) {
                setError(res.getStr("file.exists"));
                ok = false;
            }
        }
        
    // no issue
        if (ok == true) { 
            statusLabel.setText(" ");
            statusLabel.setIcon(null);
        }
        
        createButton.setEnabled(ok);
    } 
    
    /** */
    private void updatePath() {
        File file = getFile();
        pathValue.setText(ProjectFiles.get().getRelativePath(file));
    }
    
    /** */
    private void moduleChanged() {
        updatePath();
        verify();
    }
    
    /** */
    private void dirChanged() {
        updatePath();
        verify();
    }
    
    /** */
    private void updateDirectories() {
        dirCombo.removeAllItems();
        
        List<File> dirs = new ArrayList<>();
        dirs.addAll(Arrays.asList(ProjectProperties.get().getLuaSrcDirs()));
        ProjectFiles.get().sort(dirs);
    // for each Lua source directory
        for (File dir:dirs) {
            dirCombo.addItem(dir);
        }
    }
    
    /** */
    boolean showCreateLuaFileDialog() {
        updateDirectories();
        
    // module
        moduleField.setText(res.getStr("untitled"));        
        moduleField.selectAll();
        moduleField.grabFocus();
        
    // show
        return showDialog() == createButton;
    }
}
