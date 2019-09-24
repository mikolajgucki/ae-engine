package com.andcreations.ae.studio.plugins.text.editor;

import javax.swing.text.BadLocationException;

/**
 * @author Mikolaj Gucki
 */
interface TextEditorDocumentListener {
    /** */
    void removingText(int offset,int length,String text)
        throws BadLocationException;
}
