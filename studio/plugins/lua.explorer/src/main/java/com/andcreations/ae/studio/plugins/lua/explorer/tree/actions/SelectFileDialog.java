package com.andcreations.ae.studio.plugins.lua.explorer.tree.actions;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.lua.explorer.tree.actions.resources.R;
import com.andcreations.ae.studio.plugins.ui.common.Dialog;
import com.andcreations.ae.studio.plugins.ui.common.layout.FormLayoutBuilder;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.resources.ResourceLoader;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class SelectFileDialog extends Dialog {
    /** */
    private JComboBox<File> fileCombo;
    
    /** */
    private JButton okButton;
    
    /** */
    private JButton cancelButton;
    
    /** */
    SelectFileDialog(String title,String label,String ok,String cancel) {
        super(MainFrame.get(),"",true);        
        create(title,label,ok,cancel);
    }
    
    /** */
    private void create(String title,String label,String ok,String cancel) {
        setTitle(title);
        makeEscapable();
        
    // file combo
        fileCombo = new JComboBox<>();
        
    // ok
        okButton = new JButton(ok);
        
    // cancel
        cancelButton = new JButton(cancel);
        
    // layout and create
        JPanel panel = layoutComponents(label);
        create(panel,okButton,cancelButton);
    }
    
    /** */
    private JPanel layoutComponents(String label) {
        FormLayoutBuilder builder;
        try {
            String src = ResourceLoader.loadAsString(
                R.class,"SelectFileDialog.formlayout");
            builder = new FormLayoutBuilder(src);
        } catch (IOException exception) {
            Log.error("Failed to read form layout: " + exception.getMessage());
            return null;
        }
        
        builder.addLabel(label,"l");
        builder.add(fileCombo,"c");
        
        return builder.getPanel();
    }    
    
    /** */
    File showDialog(List<File> files) {
    // combo
        fileCombo.removeAllItems();
        for (File file:files) {
            fileCombo.addItem(file);
        }
        
        if (showDialog() == okButton) {
            return (File)fileCombo.getSelectedItem();
        }
        return null;
    }
}