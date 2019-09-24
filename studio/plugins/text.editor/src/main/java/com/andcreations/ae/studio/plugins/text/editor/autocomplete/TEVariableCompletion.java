package com.andcreations.ae.studio.plugins.text.editor.autocomplete;

import javax.swing.Icon;

import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.VariableCompletion;

/**
 * @author Mikolaj Gucki
 */
public class TEVariableCompletion extends VariableCompletion
    implements Autocompl {
    /** */
    private VarAutocompl var;

    /** */
    TEVariableCompletion(CompletionProvider provider,VarAutocompl var) {
        super(provider,var.getName(),"");
        this.var = var;
        create();
    }
    
    /** */
    private void create() {
    // description, defined in
        setShortDescription(var.getDescription());
        setDefinedIn(var.getDefinedIn());        
    }
    
    /** */
    @Override
    public String getDefinitionString() {
        return var.getDefinitionString();            
    }   
    
    /** */
    @Override
    public String getReplacementText() {
        return var.getReplacementText();
    } 

    /** */
    @Override
    public String getDisplayText() {
        return var.getDisplayText();
    }
    
    /** */
    @Override
    public String getDisplayHTML() {
        return var.getDisplayHTML();
    }    
    
    /** */
    @Override
    public Icon getIcon() {
        return var.getIcon();
    }
    
    /** */
    @Override
    public String getLowerCaseName() {
        return var.getLowerCaseName();
    }   
    
    /** */
    @Override
    public String getLowerCasePrefix() {
        return var.getLowerCasePrefix();
    }
    
    /** */
    @Override
    public String getLowerCaseFullName() {
        return var.getLowerCaseFullName();
    }      
    
    /** */
    @Override
    public String toString() {
        return var.getDisplayText();
    }      
}