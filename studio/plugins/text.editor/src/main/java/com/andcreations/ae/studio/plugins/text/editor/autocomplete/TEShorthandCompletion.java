package com.andcreations.ae.studio.plugins.text.editor.autocomplete;

import javax.swing.Icon;

import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.ShorthandCompletion;

/**
 * @author Mikolaj Gucki
 */
public class TEShorthandCompletion extends ShorthandCompletion
    implements Autocompl {
    /** */
    private ShorthandAutocompl shorthand;
        
    /** */
    TEShorthandCompletion(CompletionProvider provider,
        ShorthandAutocompl shorthand) {
    //
        super(provider,shorthand.getText(),"");
        this.shorthand = shorthand;
    }
    
    /** */
    /*
    @Override
    public String getDefinitionString() {
        return shorthand.getText();      
    } 
    */
    
    /** */
    @Override
    public String getReplacementText() {
        return shorthand.getReplacementText();
    } 

    /** */
    @Override
    public String getDisplayText() {
        return shorthand.getText();
    }
    
    /** */
    @Override
    public String getDisplayHTML() {
        return shorthand.getText();
    }    
    
    /** */
    @Override
    public Icon getIcon() {
        return null;
    }
    
    /** */
    @Override
    public String getLowerCaseName() {
        return shorthand.getText().toLowerCase();
    }   
    
    /** */
    @Override
    public String getLowerCasePrefix() {
        return "";
    }
    
    /** */
    @Override
    public String getLowerCaseFullName() {
        return getLowerCaseName();
    }      
    
    /** */
    @Override
    public String toString() {
        return shorthand.getText();
    }    
}