package com.andcreations.ae.studio.plugins.text.editor.autocomplete;

import org.fife.ui.autocomplete.AbstractCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;

/**
 * @author Mikolaj Gucki
 */
public class BasicAutocompl implements TECompletion {
    /** */
    private String replacementText;
    
    /** */
    private String description;
    
    /** */
    private BasicCompletion basicCompletion;
    
    /** */
    public BasicAutocompl(String replacementText,String description) {
        this.replacementText = replacementText;
        this.description = description;
    }
    
    /** */
    public BasicAutocompl(String replacementText) {
        this(replacementText,null);
    }
    
    /** */
    public AbstractCompletion getCompletion(CompletionProvider provider) {
        if (basicCompletion == null) {
            basicCompletion = new BasicCompletion(
                provider,replacementText,description);
        }
        
        return basicCompletion;
    }
}
