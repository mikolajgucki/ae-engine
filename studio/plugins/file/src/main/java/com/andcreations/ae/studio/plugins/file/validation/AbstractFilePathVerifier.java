package com.andcreations.ae.studio.plugins.file.validation;

import javax.swing.JTextField;

import com.andcreations.ae.studio.plugins.ui.common.validation.RegexVerifier;

/**
 * @author Mikolaj Gucki
 */
class AbstractFilePathVerifier extends RegexVerifier {
    /** */
    private JTextField textField;
    
    /** */
    public AbstractFilePathVerifier(JTextField textField,String errorToolTip,
        String regex) {
    //
        super(textField,errorToolTip,regex);
        this.textField = textField;
    }
    
    /** */
    @Override
    public boolean verify() {
        return match(textField.getText());
    }
}
