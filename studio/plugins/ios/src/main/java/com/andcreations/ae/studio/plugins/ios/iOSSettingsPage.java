package com.andcreations.ae.studio.plugins.ios;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.andcreations.ae.studio.plugins.ios.resources.R;
import com.andcreations.ae.studio.plugins.project.ProjectSettingsPage;
import com.andcreations.ae.studio.plugins.ui.common.layout.FormLayoutUtil;
import com.andcreations.ae.studio.plugins.ui.common.settings.SettingsIssueLabelWrapper;
import com.andcreations.ae.studio.plugins.ui.common.settings.SettingsIssueLabelWrapper.Issue;
import com.andcreations.ae.studio.plugins.ui.common.settings.SettingsIssueLabelWrapperListener;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class iOSSettingsPage extends ProjectSettingsPage {
    /** */
    private BundleResources res =
        new BundleResources(iOSSettingsPage.class);     

    /** */
    private iOSPluginState state;
        
    /** */
    private JTextField iosProjectDir;
    
    /** */
    private SettingsIssueLabelWrapper iosProjectDirIssue;
    
    /** */
    private JCheckBox verboseCheckBox;
        
    /** */
    iOSSettingsPage(iOSPluginState state) {
        this.state = state;
        create();
    }
    
    /** */
    private void create() {
        setTitle(res.getStr("title"));
        setIconName(iOSIcons.IOS);
        
    // create components
        createComponents();
        
    // layout components
        JPanel panel = layoutComponents();
        setComponent(panel);
    }
    
    /** */
    private void createComponents() {
    // ios project directory
        iosProjectDir = new JTextField();
        addiOSProjectDirListeners();
        
    // verbose
        verboseCheckBox = new JCheckBox(res.getStr("verbose"));
    }
    
    /** */
    private void addiOSProjectDirListeners() {
    // action listener
        iosProjectDir.addActionListener(new ActionListener() {
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
                iosProjectDirChanged();
            }
            
            /** */
            @Override
            public void removeUpdate(DocumentEvent event) {
                iosProjectDirChanged();
            }
            
            /** */
            @Override
            public void insertUpdate(DocumentEvent event) {
                iosProjectDirChanged();
            }            
        };
        iosProjectDir.getDocument().addDocumentListener(documentListener);         
    }
    
    /** */
    private void iosProjectDirChanged() {
    // directory
        File dir = iOSProjectDir.fromPath(iosProjectDir.getText());
        
    // validate
        String error = iOSProjectDir.validate(dir);
        if (error != null) {
            iosProjectDirIssue.setIssue(Issue.ERROR,error);
            return;
        }
        iosProjectDirIssue.clear();
    }
    
    /** */
    private JPanel layoutComponents() {
        FormLayoutUtil layout = new FormLayoutUtil(R.class,
            "iOSSettingsPage.formlayout");
        
    // add components
        layout.addLabelText("pdl",res.getStr("ios.project.dir"));
        layout.addComponent("pdf",iosProjectDir);
        layout.addLabelText("pdi"," ");
        layout.addComponent("vcb",verboseCheckBox);
        
    // layout
        JPanel panel = layout.build();        
        
    // get components
        iosProjectDirIssue = new SettingsIssueLabelWrapper(
            layout.getLabel("pdi"),new SettingsIssueLabelWrapperListener() {
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
        if (iosProjectDirIssue.hasError()) {
            setPageError();
            return;
        }
        clearPageIssues();
    }
    
    /** */
    @Override
    public void update() {
        if (state.getiOSProjectDir() != null) {
            iosProjectDir.setText(state.getiOSProjectDir());
        }
        verboseCheckBox.setSelected(state.getVerbose());
    }
    
    /** */
    @Override
    public void apply() {
        state.setiOSProjectDir(iosProjectDir.getText());
        state.setVerbose(verboseCheckBox.isSelected());
    }
}