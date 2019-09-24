package com.andcreations.ae.studio.plugins.android;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.andcreations.ae.studio.plugins.android.resources.R;
import com.andcreations.ae.studio.plugins.ui.common.layout.FormLayoutUtil;
import com.andcreations.ae.studio.plugins.ui.common.settings.SettingsIssueLabelWrapper;
import com.andcreations.ae.studio.plugins.ui.common.settings.SettingsIssueLabelWrapper.Issue;
import com.andcreations.ae.studio.plugins.ui.common.settings.SettingsIssueLabelWrapperListener;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.preferences.PreferencesPage;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class AndroidPreferencesPage extends PreferencesPage {
    /** */
    private BundleResources res =
        new BundleResources(AndroidPreferencesPage.class);
        
    /** */
    private JTextField androidSDKDir;
    
    /** */
    private JButton androidSDKBrowse;
    
    /** */
    private SettingsIssueLabelWrapper androidSDKDirIssue;
    
    /** */
    private AndroidAppState appState;
    
    /** */
    AndroidPreferencesPage(AndroidAppState appState) {
        this.appState = appState;
        create();
    }
    
    /** */
    private void create() {
        setTitle(res.getStr("title"));
        setIconName(AndroidIcons.ANDROID);
        
    // create components
        createComponents();
        
    // layout components
        JPanel panel = layoutComponents();
        setComponent(panel);         
    }
    
    /** */
    private void createComponents() {
    // android project directory
        androidSDKDir = new JTextField();
        addAndroidSDKDirListeners();  
        
    // browse
        androidSDKBrowse = new JButton();
        androidSDKBrowse.setIcon(Icons.getIcon(DefaultIcons.DIRECTORY));
        androidSDKBrowse.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                chooseDir();
            }
        });
    }
    
    /** */
    private void addAndroidSDKDirListeners() {
    // action listener
        androidSDKDir.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
            }
        });
        
    // document listener
        DocumentListener documentListener = new DocumentListener() {
            /** */
            @Override
            public void changedUpdate(DocumentEvent event) {
                androidSDKDirChanged();
            }
            
            /** */
            @Override
            public void removeUpdate(DocumentEvent event) {
                androidSDKDirChanged();
            }
            
            /** */
            @Override
            public void insertUpdate(DocumentEvent event) {
                androidSDKDirChanged();
            }            
        };
        androidSDKDir.getDocument().addDocumentListener(documentListener);         
    }    
    
    /** */
    private File getCurrentAndroidSDKDir() {
        return AndroidSDKDir.fromPath(androidSDKDir.getText());
    }
    
    /** */  
    private void chooseDir() {
        JFileChooser chooser = new JFileChooser(getCurrentAndroidSDKDir());
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
        
        int value = chooser.showOpenDialog(null);
        if (value == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            androidSDKDir.setText(path);
            androidSDKDir.selectAll();
            androidSDKDir.requestFocus();
        }        
    }
    
    /** */
    private void androidSDKDirChanged() {
    // validate
        String error = AndroidSDKDir.validate(getCurrentAndroidSDKDir());
        if (error != null) {
            androidSDKDirIssue.setIssue(Issue.ERROR,error);
            return;
        }
        androidSDKDirIssue.clear();
    }
    
    /** */
    private JPanel layoutComponents() {
        FormLayoutUtil layout = new FormLayoutUtil(R.class,
            "AndroidPreferencesPage.formlayout");
        
    // add components
        layout.addLabelText("sdl",res.getStr("android.sdk.dir"));
        layout.addComponent("sdf",androidSDKDir);
        layout.addComponent("sdb",androidSDKBrowse);
        layout.addLabelText("sdi"," ");
        
    // layout
        JPanel panel = layout.build();        
        
    // get components
        androidSDKDirIssue = new SettingsIssueLabelWrapper(
            layout.getLabel("sdi"),new SettingsIssueLabelWrapperListener() {
                /** */
                @Override
                public void issueChanged(SettingsIssueLabelWrapper wrapper) {
                    updatePage();
                }
            });
        
        return panel;
    } 
    
    /** */
    private void updatePage() {
        if (androidSDKDirIssue.hasError()) {
            setPageError();
            return;
        }
        clearPageIssues();
    }    
    
    /** */
    @Override
    public void update() {
        androidSDKDir.setText(appState.getAndroidSDKDir());
    }
    
    /** */
    @Override
    public void apply() {
        appState.setAndroidSDKDir(androidSDKDir.getText());
    }
}