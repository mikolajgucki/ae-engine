package com.andcreations.ae.studio.plugins.lua.validation;

import javax.swing.JTextField;

import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.ui.common.validation.RegexVerifier;

/**
 * @author Mikolaj Gucki
 */
public class LuaModuleNameVerifier extends RegexVerifier {
    /** */
    private static final String REGEX = String.format(
        "(%s)(\\.%s)*",LuaFile.IDENTIFIER_REGEX,LuaFile.IDENTIFIER_REGEX);
    
    /** */
    private JTextField textField;
    
    /** */
    public LuaModuleNameVerifier(JTextField textField,String errorToolTip) {
        super(textField,errorToolTip,REGEX);
        this.textField = textField;
    }
    
    /** */
    @Override
    public boolean verify() {
        return match(textField.getText());
    }
}