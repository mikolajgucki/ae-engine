package com.andcreations.ae.plugin.ui.studio.plugin.layout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JPanel;

import com.andcreations.resources.BundleResources;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.common.Dialog;
import com.andcreations.ae.studio.plugins.ui.common.DocumentChangedAdapter;
import com.andcreations.ae.studio.plugins.ui.common.layout.FormLayoutUtil;
import com.andcreations.ae.studio.plugins.ui.common.validation.IntegerVerifier;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.ae.plugin.ui.studio.plugin.layout.resources.R;

/**
 * @author Mikolaj Gucki
 */
class LayoutViewerSettingsDialog extends Dialog {
    /** */
    private BundleResources res =
        new BundleResources(LayoutViewerSettingsDialog.class);  
    
    /** */
    private JRadioButton simulatorSizeButton;
    
    /** */
    private JRadioButton customSizeButton;
        
    /** */
    private JTextField widthField;    
    
    /** */
    private IntegerVerifier widthVerifier;
    
    /** */
    private JTextField heightField;    
    
    /** */
    private IntegerVerifier heightVerifier;
    
    /** */
    private JLabel statusLabel;    
    
    /** */
    private JButton okButton;
    
    /** */
    private JButton cancelButton;
        
    /** */
    LayoutViewerSettingsDialog() {
        super(MainFrame.get(),"",true);
        create();
    }
    
    /** */
    private void create() {
        makeEscapable();
        setTitle(res.getStr("title"));
        
        ActionListener buttonListener = new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                buttonsChanged();
            }
        };
        
    // simulator size
        simulatorSizeButton = new JRadioButton(res.getStr("simulator.size"));
        simulatorSizeButton.addActionListener(buttonListener);
        
    // custom size
        customSizeButton = new JRadioButton(res.getStr("custom.size"));
        customSizeButton.addActionListener(buttonListener);
        
    // button group
        ButtonGroup sizeGroup = new ButtonGroup();
        sizeGroup.add(simulatorSizeButton);
        sizeGroup.add(customSizeButton);
        
    // width, height
        createSizeComponents();
        
    // ok
        okButton = new JButton(res.getStr("ok"));
        
    // cancel
        cancelButton = new JButton(res.getStr("cancel"));
        
    // layout, create
        JPanel panel = layoutComponents();
        create(panel,new JButton[]{okButton,cancelButton});
    }
    
    /** */
    private void createSizeComponents() {
        final int min = 1;
        final int max = 4096;        
        
    // document listener
        DocumentChangedAdapter documentListener = new DocumentChangedAdapter() {
            /** */
            @Override
            public void documentChanged() {
                verify();
            }
        };
        
    // width
        widthField = new JTextField(4);
        widthField.getDocument().addDocumentListener(documentListener);
        widthVerifier = new IntegerVerifier(widthField,
            res.getStr("width.error.tool.tip",Integer.toString(min),
            Integer.toString(max)));
        widthVerifier.setMin(min);
        widthVerifier.setMax(max);
        
    // height
        heightField = new JTextField(4);
        heightField.getDocument().addDocumentListener(documentListener);
        heightVerifier = new IntegerVerifier(heightField,
            res.getStr("height.error.tool.tip",Integer.toString(min),
            Integer.toString(max)));
        heightVerifier.setMin(min);
        heightVerifier.setMax(max);
    }
    
    /** */
    private JPanel layoutComponents() {
        FormLayoutUtil layout = new FormLayoutUtil(
            R.class,"LayoutViewerSettingsDialog.formlayout");
        
    // simulator size
        layout.addComponent("ssr",simulatorSizeButton);
        
    // custom size
        layout.addComponent("csr",customSizeButton);
        
    // width
        layout.addLabelText("wl",res.getStr("width"));
        layout.addComponent("wf",widthField);
        
    // height
        layout.addLabelText("hl",res.getStr("height"));
        layout.addComponent("hf",heightField);
        
    // status
        layout.addLabelText("sl"," ");        
        
    // layout
        JPanel panel = layout.build();
        
    // status
        statusLabel = layout.getLabel("sl");        
        
        return panel;
    }
    
    /** */
    private void buttonsChanged() {
        widthField.setEnabled(customSizeButton.isSelected());
        heightField.setEnabled(customSizeButton.isSelected());
    }
    
    /** */
    private void setError(String msg) {
        statusLabel.setText(msg);
        statusLabel.setIcon(Icons.getIcon(DefaultIcons.ERROR));
    }    
    
    /** */
    private void verify() {
        boolean ok = true;
        
    // width
        if (widthVerifier.verify() == false) {
            setError(widthVerifier.getErrorToolTip());
            ok = false;
        }
        
    // height
        if (ok == true && heightVerifier.verify() == false) {
            setError(heightVerifier.getErrorToolTip());
            ok = false;
        }
        
    // no issue
        if (ok == true) { 
            statusLabel.setText(" ");
            statusLabel.setIcon(null);
        }        
        
        okButton.setEnabled(ok);
    }
    
    /** */
    private void init(LayoutSize layoutSize) {
    // size
        widthField.setText(Integer.toString(layoutSize.getWidth()));
        heightField.setText(Integer.toString(layoutSize.getHeight()));
        
        boolean simulatorSizeFlag = layoutSize.getUseSimulatorSize();
    // buttons
        simulatorSizeButton.setSelected(simulatorSizeFlag);
        customSizeButton.setSelected(simulatorSizeFlag == false);
        buttonsChanged();
    }
    
    /** */
    boolean showLayoutViewerSettingsDialog(LayoutSize layoutSize) {
    // initialize
        init(layoutSize);
        verify();
        
    // show the dialog
        if (showDialog() == okButton) {
            layoutSize.setUseSimulatorSize(simulatorSizeButton.isSelected());
            layoutSize.setWidth(Integer.parseInt(widthField.getText()));
            layoutSize.setHeight(Integer.parseInt(heightField.getText()));
            
            return true;
        }
        return false;
    }
}