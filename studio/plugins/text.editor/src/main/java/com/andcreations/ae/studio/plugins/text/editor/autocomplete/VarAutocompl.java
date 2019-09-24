package com.andcreations.ae.studio.plugins.text.editor.autocomplete;

import org.fife.ui.autocomplete.AbstractCompletion;
import org.fife.ui.autocomplete.CompletionProvider;

/**
 * @author Mikolaj Gucki
 */
public class VarAutocompl extends AbstractAutocompl {
    /** */
    public VarAutocompl(String name,String prefix) {
        super(name,prefix);
    }    
    
    /** */
    @Override
    AbstractCompletion createCompletion(CompletionProvider provider) {
        return new TEVariableCompletion(provider,this);
    }    
}