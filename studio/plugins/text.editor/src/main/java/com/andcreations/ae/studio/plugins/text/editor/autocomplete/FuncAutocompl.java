package com.andcreations.ae.studio.plugins.text.editor.autocomplete;

import java.util.ArrayList;
import java.util.List;

import org.fife.ui.autocomplete.AbstractCompletion;
import org.fife.ui.autocomplete.CompletionProvider;

/**
 * @author Mikolaj Gucki
 */
public class FuncAutocompl extends AbstractAutocompl {
    /** */
    static class Param {
        /** */
        private String name;
        
        /** */
        private String description;
        
        /** */
        public Param(String name,String description) {
            this.name = name;
            this.description = description;            
        }
        
        /** */
        String getName() {
            return name;
        }
        
        /** */
        String getDescription() {
            return description;
        }            
    }
    
    /** */
    private List<Param> params = new ArrayList<>();
    
    /** */
    public FuncAutocompl(String name,String prefix) {
        super(name,prefix);
    }
    
    /** */
    public void addParam(String name,String description) {
        params.add(new Param(name,description));
    }       
    
    /** */
    List<Param> getParams() {
        return params;
    }
    
    /** */
    @Override
    AbstractCompletion createCompletion(CompletionProvider provider) {
        return new TEFunctionCompletion(provider,this);
    }
}