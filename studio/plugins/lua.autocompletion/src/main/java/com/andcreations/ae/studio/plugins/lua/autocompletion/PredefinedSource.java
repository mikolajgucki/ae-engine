package com.andcreations.ae.studio.plugins.lua.autocompletion;

import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.studio.plugins.text.editor.autocomplete.AutocomplSource;
import com.andcreations.ae.studio.plugins.text.editor.autocomplete.FuncAutocompl;

/**
 * @author Mikolaj Gucki
 */
class PredefinedSource {
    /** */
    private List<PredefinedVar> vars = new ArrayList<>();
    
    /** */
    private List<PredefinedFunc> funcs = new ArrayList<>();
    
    /** */
    public List<PredefinedVar> getVars() {
        return vars;
    }
    
    /** */
    public void setVars(List<PredefinedVar> vars) {
        this.vars = vars;
    }
    
    /** */
    public List<PredefinedFunc> getFuncs() {
        return funcs;
    }
        
    /** */
    public void setFuncs(List<PredefinedFunc> funcs) {
        this.funcs = funcs;
    }
    
    /** */
    AutocomplSource convert() {
        AutocomplSource autocomplSource = new AutocomplSource();
        
    // for each variable
        for (PredefinedVar var:getVars()) {
            autocomplSource.addVar(var.convert());
        }
        
    // for each function
        for (PredefinedFunc func:getFuncs()) {
            List<FuncAutocompl> funcAutocomplList = func.convert();
            for (FuncAutocompl funcAutocompl:funcAutocomplList) {
                autocomplSource.addFunc(funcAutocompl);
            }                
        }
        
        return autocomplSource;
    }
}