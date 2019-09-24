package com.andcreations.ae.studio.plugins.text.editor.autocomplete;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class AutocomplSource {
    /** */
    private List<BasicAutocompl> basics = new ArrayList<>();
    
    /** */
    private List<ShorthandAutocompl> shorthands = new ArrayList<>();
    
    /** */
    private List<VarAutocompl> vars = new ArrayList<>();
    
    /** */
    private List<FuncAutocompl> funcs = new ArrayList<>();
    
    /** */
    private AutocomplSourceListener listener;

    /** */
    public void setAutocomplSourceListener(AutocomplSourceListener listener) {
        this.listener = listener;
    }
    
    /** */
    public void addBasic(BasicAutocompl basic) {
        basics.add(basic);
        if (listener != null) {
            listener.completionAdded(basic);
        }
    }
    
    public void removeBasic(BasicAutocompl basic) {
        basics.remove(basic);
        if (listener != null) {
            listener.completionRemoved(basic);
        }
    }
    
    /** */
    public void addShorthand(ShorthandAutocompl shorthand) {
        shorthands.add(shorthand);
        if (listener != null) {
            listener.completionAdded(shorthand);
        }
    }
    
    /** */
    public void removeShorthand(ShorthandAutocompl shorthand) {
        shorthands.remove(shorthand);
        if (listener != null) {
            listener.completionRemoved(shorthand);
        }        
    }
    
    /** */
    public void addVar(VarAutocompl var) {
        vars.add(var);
        if (listener != null) {
            listener.completionAdded(var);
        }        
    }
    
    /** */
    public void removeVar(VarAutocompl var) {
        vars.remove(var);
        if (listener != null) {
            listener.completionRemoved(var);
        }        
    }
    
    /** */
    public void addFunc(FuncAutocompl func) {
        funcs.add(func);
        if (listener != null) {
            listener.completionAdded(func);
        }
    }
    
    /** */
    public void removeFunc(FuncAutocompl func) {
        funcs.remove(func);
        if (listener != null) {
            listener.completionRemoved(func);
        }
    }
    
    /** */
    public List<TECompletion> getTECompletions() {
        List<TECompletion> completions = new ArrayList<>();
        completions.addAll(basics);
        completions.addAll(shorthands);
        completions.addAll(vars);
        completions.addAll(funcs);
        
        return completions;
    }
}