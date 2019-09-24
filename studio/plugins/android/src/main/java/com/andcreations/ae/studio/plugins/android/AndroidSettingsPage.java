package com.andcreations.ae.studio.plugins.android;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang3.StringUtils;

import com.andcreations.ae.studio.plugins.android.resources.R;
import com.andcreations.ae.studio.plugins.project.ProjectSettingsPage;
import com.andcreations.ae.studio.plugins.ui.common.layout.FormLayoutUtil;
import com.andcreations.ae.studio.plugins.ui.common.settings.SettingsIssueLabelWrapper;
import com.andcreations.ae.studio.plugins.ui.common.settings.SettingsIssueLabelWrapper.Issue;
import com.andcreations.ae.studio.plugins.ui.common.settings.SettingsIssueLabelWrapperListener;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class AndroidSettingsPage extends ProjectSettingsPage {
    /** */
    private BundleResources res =
        new BundleResources(AndroidSettingsPage.class);     

    /** */
    private AndroidPluginState state;
        
    /** */
    private JTextField androidProjectDir;
    
    /** */
    private SettingsIssueLabelWrapper androidProjectDirIssue;
    
    /** */
    private JTextField androidPackage;
    
    /** */
    private SettingsIssueLabelWrapper androidPackageIssue;
    
    /** */
    private JCheckBox verboseCheckBox;
        
    /** */
    AndroidSettingsPage(AndroidPluginState state) {
        this.state = state;
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
        androidProjectDir = new JTextField();
        addAndroidProjectDirListeners();
        
    // android package
        androidPackage = new JTextField();
        addAndroidPackageListeners();
        
    // verbose
        verboseCheckBox = new JCheckBox(res.getStr("verbose"));
    }
    
    /** */
    private void addAndroidProjectDirListeners() {
    // action listener
        androidProjectDir.addActionListener(new ActionListener() {
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
                androidProjectDirChanged();
            }
            
            /** */
            @Override
            public void removeUpdate(DocumentEvent event) {
                androidProjectDirChanged();
            }
            
            /** */
            @Override
            public void insertUpdate(DocumentEvent event) {
                androidProjectDirChanged();
            }            
        };
        androidProjectDir.getDocument().addDocumentListener(documentListener);         
    }
    
    /** */
    private void addAndroidPackageListeners() {
    // action listener
        androidPackage.addActionListener(new ActionListener() {
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
                androidPackageChanged();
            }
            
            /** */
            @Override
            public void removeUpdate(DocumentEvent event) {
                androidPackageChanged();
            }
            
            /** */
            @Override
            public void insertUpdate(DocumentEvent event) {
                androidPackageChanged();
            }            
        };
        androidPackage.getDocument().addDocumentListener(documentListener);        
    }
    
    /** */
    private void androidProjectDirChanged() {
    // directory
        File dir = AndroidProjectDir.fromPath(androidProjectDir.getText());
        
    // validate
        String error = AndroidProjectDir.validate(dir);
        if (error != null) {
            androidProjectDirIssue.setIssue(Issue.ERROR,error);
            return;
        }
        androidProjectDirIssue.clear();
    }
    
    /** */
    private void androidPackageChanged() {
        if (androidPackage.getText() == null ||
            androidPackage.getText().length() == 0) {
        //
            androidPackageIssue.setIssue(Issue.ERROR,
                res.getStr("no.android.package"));
            return;
        }
        androidPackageIssue.clear();
    }
    
    /** */
    private JPanel layoutComponents() {
        FormLayoutUtil layout = new FormLayoutUtil(R.class,
            "AndroidSettingsPage.formlayout");
        
    // add components
        layout.addLabelText("pdl",res.getStr("android.project.dir"));
        layout.addComponent("pdf",androidProjectDir);
        layout.addLabelText("pdi"," ");
        layout.addLabelText("apl",res.getStr("android.package"));
        layout.addComponent("apf",androidPackage);
        layout.addLabelText("api"," ");
        layout.addComponent("vcb",verboseCheckBox);
        
    // layout
        JPanel panel = layout.build();        
        
    // get components
        androidProjectDirIssue = new SettingsIssueLabelWrapper(
            layout.getLabel("pdi"),new SettingsIssueLabelWrapperListener() {
                /** */
                @Override
                public void issueChanged(SettingsIssueLabelWrapper wrapper) {
                    updatePage();
                }
            });        
        androidPackageIssue = new SettingsIssueLabelWrapper(
            layout.getLabel("api"),new SettingsIssueLabelWrapperListener() {
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
        if (androidProjectDirIssue.hasError() == true ||
            androidPackageIssue.hasError() == true) {
        //
            setPageError();
            return;
        }
        clearPageIssues();
    }
    
    /** */
    @Override
    public void update() {
        androidProjectDir.setText(state.getAndroidProjectDir());
        if (state.getAndroidPackage() != null) {
            androidPackage.setText(state.getAndroidPackage());
        }
        else {
            androidPackage.setText("");
        }
        verboseCheckBox.setSelected(state.getVerbose());
        updatePage();
    }
    
    /** */
    @Override
    public void apply() {
        state.setVerbose(verboseCheckBox.isSelected());
        
    // project directory
        String oldDir = state.getAndroidProjectDir();
        String newDir = androidProjectDir.getText();
        if (StringUtils.equals(oldDir,newDir) == false) {
            state.setAndroidProjectDir(newDir);
            AndroidSettings.get().notifyAndroidProjectDirChanged(newDir);
        }

    // package
        String oldPackage = state.getAndroidPackage();
        String newPackage = androidPackage.getText();
        if (StringUtils.equals(oldPackage,newPackage) == false) {
            state.setAndroidPackage(newPackage);
            AndroidSettings.get().notifyAndroidPackageChanged(newPackage);
        }
    }
}