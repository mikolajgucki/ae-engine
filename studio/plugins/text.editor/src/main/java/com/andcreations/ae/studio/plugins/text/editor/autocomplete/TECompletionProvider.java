package com.andcreations.ae.studio.plugins.text.editor.autocomplete;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.JTextComponent;

import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;

/**
 * @author Mikolaj Gucki
 */
public class TECompletionProvider extends DefaultCompletionProvider {
    /** */
    @Override
    public boolean isValidChar(char ch) {
        return super.isValidChar(ch) || ch == '.' || ch == '?' || ch == '@';
    }    
    
    /** */
    private boolean matchName(Autocompl autocompl,String text) {
        return autocompl.getLowerCaseName().startsWith(text);
    }
    
    /** */
    private boolean matchFullName(Autocompl autocompl,String text) {
        return autocompl.getLowerCaseFullName().startsWith(text);
    }    
    
    /** */
    private boolean matchPrefix(Autocompl autocompl,String text,
        String dotText) {
    //
        return autocompl.getLowerCasePrefix().startsWith(text) ||
            autocompl.getLowerCasePrefix().contains(dotText);
    }
    
    /** */
    @Override
    protected List<Completion> getCompletionsImpl(JTextComponent comp) {
        String text = getAlreadyEnteredText(comp).toLowerCase();
        if (text == null || text.length() == 0) {
            return completions;
        }
        
        String name = null;
        String prefix = null;
        
    // check if to match name, prefix or both
        int indexOf = text.indexOf('?');
        if (indexOf < 0) {
            name = text;
        }
        else {
            name = text.substring(indexOf + 1,text.length());
            prefix = text.substring(0,indexOf);
            if (name.length() == 0) {
                name = null;
            }
            if (prefix.length() == 0) {
                prefix = null;
            }                
        }
        
        String dotName = null;
        if (name != null) {
            dotName = "." + name;
        }
        String dotPrefix = null;
        if (prefix != null) {
            dotPrefix = "." + prefix;
        }
        
    // matched completions
        List<Completion> matched = new ArrayList<>();
                
    // ? -> match all
        if (name == null && prefix == null) {
            return completions;
        }
        
    // ... or ?...
        if (name != null && prefix == null) {
            if (indexOf < 0) {            
            // ... -> match by name, prefix or full name
                for (Completion compl:completions) {
                    Autocompl autocompl = (Autocompl)compl;
                    if (matchName(autocompl,name) == true ||
                        matchPrefix(autocompl,name,dotName) == true ||
                        matchFullName(autocompl,name) == true) {
                    //
                        matched.add(compl);
                    }
                }
            }
            else {
            // ?... -> match by name
                for (Completion compl:completions) {
                    Autocompl autocompl = (Autocompl)compl;
                    if (matchName(autocompl,name) == true) {
                        matched.add(compl);
                    }
                }                
            }
        }        
        
    // ...? -> match by prefix
        if (name == null && prefix != null) {
            for (Completion compl:completions) {
                Autocompl autocompl = (Autocompl)compl;
                if (matchPrefix(autocompl,prefix,dotPrefix) == true) {
                    matched.add(compl);
                }
            }            
        }
        
    // ...?... -> match by name and prefix
        if (name != null && prefix != null) {            
            for (Completion compl:completions) {
                Autocompl autocompl = (Autocompl)compl;
                if (matchName(autocompl,name) == true &&
                    matchPrefix(autocompl,prefix,dotPrefix) == true) {
                //
                    matched.add(compl);
                }
            }
        }              
        
        return matched;
    }
}