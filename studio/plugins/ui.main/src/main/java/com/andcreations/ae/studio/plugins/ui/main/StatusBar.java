package com.andcreations.ae.studio.plugins.ui.main;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXStatusBar;

/**
 * The status bar.
 * 
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class StatusBar extends JXStatusBar {
	/** */
	private static final int INFO_LABEL_WIDTH = 512;
	
    /** */
    private static StatusBar instance;
    
    /** */
    private JLabel infoLabel;
    
    /** */
    private StatusBar() {
        create();
    }
    
    /** */
    private void create() {
        infoLabel = new JLabel();
        add(infoLabel,new JXStatusBar.Constraint(INFO_LABEL_WIDTH));
    }
    
    /** */
    public void setInfo(String text,Color color) {
        infoLabel.setText(text);
        infoLabel.setForeground(color);
    }
    
    /** */
    public void setInfo(String text) {
        setInfo(text,UIManager.getColor("Label.foregroundColor"));
    }
    
    /** */
    public void setNoInfo() {
        setInfo("");
    }
    
    /** */
    public void addStatusComponent(JComponent component) {
        add(component,new JXStatusBar.Constraint(
            JXStatusBar.Constraint.ResizeBehavior.FIXED));
    }
    
    /** */
    public void addStatusComponent(JComponent component,int width) {
        add(component,new JXStatusBar.Constraint(width));
    }
    
    /** */
    public static StatusBar get() {
        if (instance == null) {
            instance = new StatusBar();
        }
        
        return instance;
    }
}
