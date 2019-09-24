package com.andcreations.ae.studio.plugins.ui.common.validation;

import javax.swing.JTextField;

/**
 * @author Mikolaj Gucki
 */
public class IntegerVerifier extends DefaultVerifier {
    /** */
    private JTextField textField;
    
    /** */
    private boolean verifyMin;
    
    /** */
    private int min;
        
    /** */
    private boolean verifyMax;
    
    /** */
    private int max;
    
    /** */
    public IntegerVerifier(JTextField textField,String errorToolTip) {
        super(textField,errorToolTip);
        this.textField = textField;
    }
    
    /** */
    public void setMin(int min) {
        this.min = min;
        this.verifyMin = true;
    }
    
    /** */
    public void setMax(int max) {
        this.max = max;
        this.verifyMax = true;
    }
    
    /** */
    public boolean verify() {
        String text = textField.getText();
        if (text.isEmpty() == true) {
            return false;
        }
        
        int value;
        try {
            value = Integer.parseInt(text);
        } catch (NumberFormatException exception) {
            return false;
        }
        
        if (verifyMin == true && value < min) {
            return false;
        }
        if (verifyMax == true && value > max) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Gets the value. Use this method only if verification is successful.
     *
     * @return The integer value.     
     */
    public int getValue() {
        int value;
        try {
            value = Integer.parseInt(textField.getText());
        } catch (NumberFormatException exception) {
            return -1;
        }
        
        return value;
    }
}