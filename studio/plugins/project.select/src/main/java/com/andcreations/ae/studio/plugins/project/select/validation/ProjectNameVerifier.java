package com.andcreations.ae.studio.plugins.project.select.validation;

import javax.swing.JTextField;

import com.andcreations.ae.studio.plugins.ui.common.validation.RegexVerifier;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class ProjectNameVerifier extends RegexVerifier {
    /** */
    public static final String NAME_REGEX = "[a-zA-Z_0-9\\-\\ ]+";
    
    /** */
    private static BundleResources res =
        new BundleResources(ProjectNameVerifier.class);
    
    /** */
    private JTextField textField;
    
    /** */
    public ProjectNameVerifier(JTextField textField,String errorToolTip) {
        super(textField,errorToolTip,NAME_REGEX);
        this.textField = textField;
    }
    
    /** */
    public boolean verify() {
        return match(textField.getText());
    }
    
    /** */
    public static String getAllowCharsMsg() {
        return res.getStr("allowed.chars");
    }
}
