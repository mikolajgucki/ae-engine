package com.andcreations.ae.studio.plugins.file.validation;

import javax.swing.JTextField;

import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.ui.common.validation.RegexVerifier;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class FileNameVerifier extends RegexVerifier {
    /** */
    private static BundleResources res =
        new BundleResources(FileNameVerifier.class);
    
    /** */
    private JTextField textField;
    
    /** */
    public FileNameVerifier(JTextField textField,String errorToolTip) {
        super(textField,errorToolTip,Files.NAME_REGEX);
        this.textField = textField;
    }
    
    /** */
    @Override    
    public boolean verify() {
        return match(textField.getText());
    }
    
    /** */
    public static String getAllowCharsMsg() {
        return res.getStr("allowed.chars");
    }
}
