package com.andcreations.ae.studio.plugins.text.editor;

/**
 * @author Mikolaj Gucki
 */
interface LineHighlightsListener {
    /** */
    void lineHighlightAdded(LineHighlight highlight);
    
    /** */
    void lineHighlightRemoved(LineHighlight highlight);
}