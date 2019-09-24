package com.andcreations.ae.studio.plugins.text.editor.autocomplete;

import org.fife.ui.autocomplete.AbstractCompletion;
import org.fife.ui.autocomplete.CompletionProvider;

/**
 * @author Mikolaj Gucki
 */
public interface TECompletion {
    /** */
    AbstractCompletion getCompletion(CompletionProvider provider);
}