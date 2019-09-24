package com.andcreations.ae.studio.plugins.lua.validation;

import javax.swing.JTextField;

import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.ui.common.validation.RegexVerifier;

/**
 * @author Mikolaj Gucki
 */
public class LuaFileNameVerifier extends RegexVerifier {
    /** */
    private JTextField textField;
    
    /** */
    private boolean includeSuffix;
    
    /** */
    public LuaFileNameVerifier(JTextField textField,String errorToolTip,
        boolean includeSuffix) {
    //
        super(textField,errorToolTip,LuaFile.IDENTIFIER_REGEX);
        this.textField = textField;
        this.includeSuffix = includeSuffix;
    }
    
    /** */
    @Override
    public boolean verify() {
        String text = textField.getText();
        if (includeSuffix == true) {
            if (text.endsWith(LuaFile.DOT_SUFFIX) == false) {
                return false;
            }
            
            text = text.substring(0,
                text.length() - LuaFile.DOT_SUFFIX.length());
        }
        
        return match(text);
    }
}