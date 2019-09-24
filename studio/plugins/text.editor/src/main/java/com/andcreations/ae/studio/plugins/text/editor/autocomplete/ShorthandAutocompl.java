package com.andcreations.ae.studio.plugins.text.editor.autocomplete;

import org.fife.ui.autocomplete.AbstractCompletion;
import org.fife.ui.autocomplete.CompletionProvider;

/**
 * @author Mikolaj Gucki
 */
public class ShorthandAutocompl implements TECompletion {
    /** */
    private String text;
    
    /** */
    private String replacementText;
    
    /** */
    public ShorthandAutocompl(String text,String replacementText) {
        this.text = text;
        this.replacementText = replacementText;
    }
    
    /** */
    public String getText() {
        return text;
    }
    
    /** */
    public String getReplacementText() {
        return replacementText;
    }
    
    /** */
    public AbstractCompletion getCompletion(CompletionProvider provider) {
        return new TEShorthandCompletion(provider,this);
    }
}
