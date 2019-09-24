package com.andcreations.ae.studio.plugins.ui.common.validation;

import java.util.regex.Pattern;

import javax.swing.JTextField;

/**
 * @author Mikolaj Gucki
 */
public class RegexVerifier extends DefaultVerifier {
    /** */
    private JTextField textField;
    
    /** */
    private Pattern pattern;
    
    /** */
    public RegexVerifier(JTextField textField,String errorToolTip,
        String regex) {
    //
        super(textField,errorToolTip);
        this.textField = textField;
        this.pattern = Pattern.compile(regex);
    }
    
    /** */
    protected boolean match(String text) {
        return pattern.matcher(textField.getText()).matches();
    }
    
    /** */
    public boolean verify() {
        return match(textField.getText());
    }
}