package com.andcreations.ae.studio.plugins.ui.common.validation;

import javax.swing.JTextField;

/**
 * @author Mikolaj Gucki
 */
public class NonEmptyVerifier extends DefaultVerifier {
    /** */
    private JTextField textField;
    
    /** */
    public NonEmptyVerifier(JTextField textField,String errorToolTip) {
        super(textField,errorToolTip);
        this.textField = textField;
    }
    
    /** */
    public boolean verify() {
        return textField.getText().isEmpty() == false;
    }
}