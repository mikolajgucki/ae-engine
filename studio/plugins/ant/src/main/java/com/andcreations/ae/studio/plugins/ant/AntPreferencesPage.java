package com.andcreations.ae.studio.plugins.ant;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.andcreations.ae.studio.plugins.ant.resources.R;
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
class AntPreferencesPage extends PreferencesPage {
    /** */
    private BundleResources res =
        new BundleResources(AntPreferencesPage.class);    
    
    /** */
    private JTextField antHome;
    
    /** */
    private JButton antHomeBrowse;
    
    /** */
    private SettingsIssueLabelWrapper antHomeIssue;        
        
    /** */
    private AntAppState appState;
    
    /** */
    AntPreferencesPage(AntAppState appState) {
        this.appState = appState;
        create();
    }
    
    /** */
    private void create() {
        setTitle(res.getStr("title"));
        setIconName(AntIcons.ANT);
        
    // create components
        createComponents();
        
    // layout components
        JPanel panel = layoutComponents();
        setComponent(panel);         
    }
    
    /** */
    private void createComponents() {
    // android project directory
        antHome = new JTextField();
        addAntHomeListeners();  
        
    // browse
        antHomeBrowse = new JButton();
        antHomeBrowse.setIcon(Icons.getIcon(DefaultIcons.DIRECTORY));
        antHomeBrowse.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                chooseDir();
            }
        });
    }
    
    /** */
    private void addAntHomeListeners() {
    // action listener
        antHome.addActionListener(new ActionListener() {
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
                antHomeChanged();
            }
            
            /** */
            @Override
            public void removeUpdate(DocumentEvent event) {
                antHomeChanged();
            }
            
            /** */
            @Override
            public void insertUpdate(DocumentEvent event) {
                antHomeChanged();
            }            
        };
        antHome.getDocument().addDocumentListener(documentListener);         
    }    
    
    /** */
    private File getCurrentAntHome() {
        return new File(antHome.getText());
    }
    
    /** */  
    private void chooseDir() {
        JFileChooser chooser = new JFileChooser(getCurrentAntHome());
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
        
        int value = chooser.showOpenDialog(null);
        if (value == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            antHome.setText(path);
            antHome.selectAll();
            antHome.requestFocus();
        }        
    }
    
    /** */
    private void antHomeChanged() {
    // validate
        File antHome = getCurrentAntHome();
        String error = AntHome.validate(antHome);
        if (error != null) {
            antHomeIssue.setIssue(Issue.ERROR,error);
            return;
        }
        antHomeIssue.clear();        
    }
    
    /** */
    private JPanel layoutComponents() {
        FormLayoutUtil layout = new FormLayoutUtil(R.class,
            "AntPreferencesPage.formlayout");
        
    // add components
        layout.addLabelText("ahl",res.getStr("ant.home"));
        layout.addComponent("ahf",antHome);
        layout.addComponent("ahb",antHomeBrowse);
        layout.addLabelText("ahi"," ");
        
    // layout
        JPanel panel = layout.build();        
        
    // get components
        antHomeIssue = new SettingsIssueLabelWrapper(
            layout.getLabel("ahi"),new SettingsIssueLabelWrapperListener() {
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
        if (antHomeIssue.hasError()) {
            setPageError();
            return;
        }
        clearPageIssues();
    }    
    
    /** */
    @Override
    public void update() {
        antHome.setText(appState.getAntHome());
    }
    
    /** */
    @Override
    public void apply() {
        appState.setAntHome(antHome.getText());
    }
}