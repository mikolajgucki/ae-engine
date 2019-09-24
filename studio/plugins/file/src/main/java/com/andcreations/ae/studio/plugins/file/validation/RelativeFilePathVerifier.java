package com.andcreations.ae.studio.plugins.file.validation;

import javax.swing.JTextField;

import com.andcreations.ae.studio.plugins.file.Files;

/**
 * @author Mikolaj Gucki
 */
public class RelativeFilePathVerifier extends AbstractFilePathVerifier {
    /** */
    public RelativeFilePathVerifier(JTextField textField,String errorToolTip) {
        super(textField,errorToolTip,Files.RELATIVE_PATH_REGEX);
    }
}
