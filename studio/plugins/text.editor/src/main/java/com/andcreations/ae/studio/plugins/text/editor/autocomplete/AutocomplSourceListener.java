package com.andcreations.ae.studio.plugins.text.editor.autocomplete;

/**
 * @author Mikolaj Gucki
 */
public interface AutocomplSourceListener {
    /** */
    void completionAdded(TECompletion completion);
    
    /** */
    void completionRemoved(TECompletion completionl);
}