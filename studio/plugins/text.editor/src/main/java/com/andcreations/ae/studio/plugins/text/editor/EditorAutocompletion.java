package com.andcreations.ae.studio.plugins.text.editor;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.LanguageAwareCompletionProvider;

import com.andcreations.ae.studio.plugins.text.editor.autocomplete.AutocomplSource;
import com.andcreations.ae.studio.plugins.text.editor.autocomplete.AutocomplSourceListener;
import com.andcreations.ae.studio.plugins.text.editor.autocomplete.TECompletion;
import com.andcreations.ae.studio.plugins.text.editor.autocomplete.TECompletionCellRenderer;
import com.andcreations.ae.studio.plugins.text.editor.autocomplete.TECompletionProvider;

/**
 * @author Mikolaj Gucki
 */
public class EditorAutocompletion {
    /** */
    private EditorMediator editor;
    
    /** */
    private TECompletionProvider defaultProvider;
    
    /** */
    private TECompletionProvider commentProvider;
    
    /** */
    private LanguageAwareCompletionProvider topProvider;
    
    
    /** */
    EditorAutocompletion(EditorMediator editor) {
        this.editor = editor;
        create();
    }
    
    /** */
    private void create() {
    // default provider
        defaultProvider = new TECompletionProvider();
        defaultProvider.setListCellRenderer(new TECompletionCellRenderer());
        defaultProvider.setParameterizedCompletionParams('(',",",')');

    // comment provider
        commentProvider = new TECompletionProvider();
        
    // top provider
        topProvider = new LanguageAwareCompletionProvider(defaultProvider);
        topProvider.setCommentCompletionProvider(commentProvider);
        
    // install
        AutoCompletion completion = new AutoCompletion(topProvider);        
        completion.install(editor.getComponent().getTextArea());
        completion.setShowDescWindow(true);
        completion.setParameterAssistanceEnabled(true);
    }
    
    /** */
    private void setAutocomplSourceListener(AutocomplSource source,
        final TECompletionProvider provider) {
    //
        source.setAutocomplSourceListener(new AutocomplSourceListener() {
            /** */
            @Override
            public void completionAdded(TECompletion completion) {
                provider.addCompletion(
                    completion.getCompletion(defaultProvider));
            }
    
            /** */
            @Override
            public void completionRemoved(TECompletion completion) {
                provider.removeCompletion(
                    completion.getCompletion(defaultProvider));
            }
        });
    }
    
    /** */
    public void addCommentSource(AutocomplSource source) {
        for (TECompletion completion:source.getTECompletions()) {
            commentProvider.addCompletion(
                completion.getCompletion(commentProvider));
        }
        
    // listener
        setAutocomplSourceListener(source,commentProvider);
    }
    
    /** */
    public void addSource(AutocomplSource source) {        
        for (TECompletion completion:source.getTECompletions()) {
            defaultProvider.addCompletion(
                completion.getCompletion(defaultProvider));
        }
        
    // listener
        setAutocomplSourceListener(source,defaultProvider);
    }
}