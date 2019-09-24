package com.andcreations.ae.studio.plugins.ui.common;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author Mikolaj Gucki
 */
public class DocumentChangedAdapter implements DocumentListener {
    /** */
    @Override
    public void changedUpdate(DocumentEvent event) {
        documentChanged();
    }
    
    /** */
    @Override
    public void removeUpdate(DocumentEvent event) {
        documentChanged();
    }
    
    /** */
    @Override
    public void insertUpdate(DocumentEvent event) {
        documentChanged();
    }     
    
    /**
     * Called when the document has changed.
     */
    public void documentChanged() {
    }
}