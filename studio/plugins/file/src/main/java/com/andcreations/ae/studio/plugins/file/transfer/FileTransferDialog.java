package com.andcreations.ae.studio.plugins.file.transfer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.andcreations.ae.studio.plugins.file.transfer.resources.R;
import com.andcreations.ae.studio.plugins.ui.common.Dialog;
import com.andcreations.ae.studio.plugins.ui.common.layout.FormLayoutUtil;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class FileTransferDialog extends Dialog {
    /** */
    private BundleResources res = new BundleResources(FileTransferDialog.class);    
    
    /** */
    private JLabel infoLabel;
    
    /** */
    private JProgressBar progressBar;

    /** */
    private JButton cancelButton;
    
    /** */
    FileTransferDialog() {
        super(MainFrame.get(),"",true);
        create();
    }
    
    /** */
    private void create() {
    // progress bar
        progressBar = new JProgressBar();
        progressBar.setStringPainted(false);
        
    // cancel
        cancelButton = new JButton(res.getStr("cancel"));        
        
    // layout, create
        JPanel panel = layoutComponents();
        create(panel,new JButton[]{cancelButton});        
    }    
    
    /** */
    private JPanel layoutComponents() {
        FormLayoutUtil layout = new FormLayoutUtil(
            R.class,"FileTransferDialog.formlayout");
        
    // info
        layout.addLabelText("il"," ");
        
    // progress
        layout.addComponent("pb",progressBar);
        
    // layout
        JPanel panel = layout.build();
        
    // info
        infoLabel = layout.getLabel("il");
        
        return panel;
    }     
    
    /** */
    void setInfo(String info) {
        infoLabel.setText(info);
    }
    
    /** */
    void setProgress(double progress) {
        progressBar.setValue((int)(progress * 100));
    }
    
    /** */
    boolean showFileTransferDialog() {
        progressBar.setValue(0);
        return showDialog() == cancelButton;
    }
    
    /** */
    void closeFileTransferDialog() {
        close(null);
    }
}