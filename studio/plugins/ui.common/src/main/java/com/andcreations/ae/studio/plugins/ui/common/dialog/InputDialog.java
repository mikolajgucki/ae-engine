package com.andcreations.ae.studio.plugins.ui.common.dialog;

import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.andcreations.ae.studio.plugins.ui.common.Dialog;
import com.andcreations.ae.studio.plugins.ui.common.dialog.resources.R;
import com.andcreations.ae.studio.plugins.ui.common.layout.FormLayoutUtil;
import com.andcreations.resources.BundleResources;

/**
 * An dialog with a single input field and label.
 * 
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public abstract class InputDialog extends Dialog {
    /** */
    private BundleResources res = new BundleResources(InputDialog.class); 
    
	/** */
	private JLabel label;
	
	/** */
	private JTextField field;
	
	/** */
	private JButton okButton;
	
	/** */
	private JButton cancelButton;
	
	/** */
	protected InputDialog(Frame owner,boolean modal) {
		super(owner,modal);
		create();
	}
	
	/** */
	protected InputDialog(Frame owner ){
		this(owner,true);
	}
	
	/** */
	private void create() {
	// label
		label = new JLabel(" ");
		
	// field
		field = new JTextField();
		
	// ok
		okButton = new JButton(res.getStr("ok"));
		
	// cancel
		cancelButton = new JButton(res.getStr("cancel"));
		
    // layout, create
        JPanel panel = layoutComponents();
        create(panel,new JButton[]{okButton,cancelButton});		
	}
	
    /** */
    private JPanel layoutComponents() {
        FormLayoutUtil layout = new FormLayoutUtil(
    		R.class,"InputDialog.formlayout");

    // components
        layout.addComponent("l",label);
        layout.addComponent("f",field);
        
    // layout
        JPanel panel = layout.build();
        
        return panel;        
    }
    
    /** */
    public void setLabelText(String text) {
    	label.setText(text);
    }
    
    /** */
    public void setText(String text) {
    	field.setText(text);
    }
    
    /** */
    public String getText() {
    	return field.getText();
    }
    
    /** */
    public boolean showInputDialog() {
    	return showDialog() == okButton;
    }
}
