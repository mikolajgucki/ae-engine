package com.andcreations.ae.studio.plugins.lua.templates;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;

import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.ui.common.validation.RegexVerifier;

/**
 * @author Mikolaj Gucki
 */
public class LuaFuncNameArgsVerifier extends RegexVerifier {
    /** */
    private static final String ID = LuaFile.IDENTIFIER_REGEX;
    
    /** */
    private static final String FUNC_NAME_REGEX = String.format( 
        "%s(\\.%s)*(:%s)?",ID,ID,ID);
    
    /** */
    public static final String REGEX = String.format(
        "(%s)\\((%s(,%s)*)?\\)",FUNC_NAME_REGEX,ID,ID);
    
    /** */
    private JTextField textField;
    
    /** */
    public LuaFuncNameArgsVerifier(JTextField textField,String errorToolTip) {
        super(textField,errorToolTip,REGEX);
        this.textField = textField;
    }
    
    /** */
    protected boolean match(String text) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(textField.getText());
        
        return matcher.matches();
    }
    
    /** */
    private Matcher matcher() {
        Pattern pattern = Pattern.compile(REGEX);
        return pattern.matcher(textField.getText());
    }
    
    /** */
    String getName() {
        Matcher matcher = matcher();
        if (matcher.matches() == false) {
            return null;
        }
        
        return matcher.group(1);
    }
    
    /** */
    String getArgsStr() {
        Matcher matcher = matcher();
        if (matcher.matches() == false) {
            return null;
        }
        
        return matcher.group(matcher.groupCount() - 1);        
    }
    
    /** */
    String[] getArgs() {
        String argsStr = getArgsStr();
        if (argsStr == null) {
            return null;
        }
        return argsStr.split(",");
    }
}