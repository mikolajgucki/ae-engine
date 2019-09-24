package com.andcreations.ae.plugin.ui.studio.plugin.layout;

import java.util.List;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.andcreations.resources.BundleResources;
import com.andcreations.ae.studio.plugins.ui.common.layout.FormLayoutUtil;
import com.andcreations.ae.studio.plugins.ui.common.DocumentChangedAdapter;
import com.andcreations.ae.studio.plugins.project.ProjectSettingsPage;
import com.andcreations.ae.plugin.ui.studio.plugin.UIIcons;
import com.andcreations.ae.plugin.ui.studio.plugin.UIPluginState;
import com.andcreations.ae.plugin.ui.studio.plugin.layout.resources.R;

/**
 * @author Mikolaj Gucki
 */
class LayoutSettingsPage extends ProjectSettingsPage {
    /** */
    private BundleResources res =
        new BundleResources(LayoutSettingsPage.class);  
        
    /** */
    private LayoutViewerState state;
    
    /** */
    private DefaultListModel<String> moduleListModel;
    
    /** */
    private JList<String> moduleList;
    
    /** */
    private JScrollPane moduleListScroll;
    
    /** */
    private JButton deleteButton;
    
    /** */
    private JTextField moduleField;
    
    /** */
    private JButton addButton;
    
    /** */
    LayoutSettingsPage(LayoutViewerState state) {
        this.state = state;
        create();
    }
    
    /** */
    private void create() {
        setTitle(res.getStr("title"));
        setIconName(UIIcons.LAYOUT);  
        
    // create components
        createComponents();
        
    // layout components
        JPanel panel = layoutComponents();
        setComponent(panel);        
        
    // initialize
        selectionChanged();
        fieldChanged();
    }
    
    /** */
    private void createComponents() {
    // module list
        moduleListModel = new DefaultListModel<String>();
        moduleList = new JList<String>(moduleListModel);
        moduleList.addListSelectionListener(new ListSelectionListener() {
            /** */
            @Override
            public void valueChanged(ListSelectionEvent event) {
                selectionChanged();
            }
        });
        moduleListScroll = new JScrollPane(moduleList);
        
    // delete button
        deleteButton = new JButton(res.getStr("delete"));
        deleteButton.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                deleteModules();
            }
        });
        
    // add field
        moduleField = new JTextField();
        moduleField.getDocument().addDocumentListener(
            new DocumentChangedAdapter() {
                /** */
                @Override
                public void documentChanged() {
                    fieldChanged();
                }                
            });
        moduleField.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                addModule();
            }
        });        
        
    // add button
        addButton = new JButton(res.getStr("add"));
        addButton.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                addModule();
            }
        });
    }
    
    /** */
    private JPanel layoutComponents() {
        FormLayoutUtil layout = new FormLayoutUtil(R.class,
            "LayoutSettingsPage.formlayout");
        
    // add components
        layout.addLabelText("lmm",res.getStr("lua.modules"));
        layout.addComponent("ml",moduleListScroll);
        layout.addComponent("db",deleteButton);
        layout.addLabelText("al",res.getStr("add.module.label"));
        layout.addComponent("af",moduleField);
        layout.addComponent("ab",addButton);
        
    // layout
        JPanel panel = layout.build();
        
    // align labels
        layout.getLabel("al").setHorizontalAlignment(SwingConstants.RIGHT);

        return panel;        
    }
    
    /** */
    private void selectionChanged() {
        deleteButton.setEnabled(moduleList.getSelectedValue() != null);
    }
    
    /** */
    private void fieldChanged() {
        addButton.setEnabled(moduleField.getText().length() > 0);
    }
    
    /** */
    private void addModule() {
        String module = moduleField.getText();
        if (module.length() == 0) {
            return;
        }
        
    // add
        moduleListModel.addElement(module);
        
    // clear
        moduleField.setText("");
    }
    
    /** */
    private void deleteModules() {
        List<String> modulesToDelete = new ArrayList<>();
        for (int index:moduleList.getSelectedIndices()) {
            modulesToDelete.add(moduleListModel.getElementAt(index)); 
        }
        for (String module:modulesToDelete) {
            moduleListModel.removeElement(module);
        }
    }
    
    /** */
    @Override
    public void update() {
        moduleListModel.clear();
        for (String module:state.getUserModulesToLoad()) {
            moduleListModel.addElement(module);
        }
    }
    
    /** */
    @Override
    public void apply() {
        state.getUserModulesToLoad().clear();
        for (int index = 0; index < moduleListModel.getSize(); index++) {
            state.getUserModulesToLoad().add(
                moduleListModel.getElementAt(index));
        }
    }
}