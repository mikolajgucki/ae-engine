package com.andcreations.ae.studio.plugins.text.editor.autocomplete;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.FunctionCompletion;

/**
 * @author Mikolaj Gucki
 */
public class TEFunctionCompletion extends FunctionCompletion
    implements Autocompl {
    /** */                                        
    private class TEParameter extends Parameter {
        /** */
        private FuncAutocompl.Param param;
        
        /** */
        private TEParameter(FuncAutocompl.Param param,boolean endParam) {
            super(null,param.getName(),endParam);            
            this.param = param;
        }
        
        /** */
        @Override
        public String getDescription() {
            return param.getDescription();
        }
    }
        
    /** */
    private FuncAutocompl func;
    
    /** */
    TEFunctionCompletion(CompletionProvider provider,FuncAutocompl func) {
        super(provider,func.getName(),"");
        this.func = func;
        create();
    }
    
    /** */
    private void create() {
    // description, defined in
        setShortDescription(func.getDescription());
        setDefinedIn(func.getDefinedIn());
        
    // params
        List<Parameter> parameters = new ArrayList<>();
        for (int index = 0; index < func.getParams().size(); index++) {
            FuncAutocompl.Param param = func.getParams().get(index);
            boolean endParam = index == func.getParams().size() - 1;
            parameters.add(new TEParameter(param,endParam));
        }
        setParams(parameters);
    }
    
    /** */
    @Override
    public String getDefinitionString() {
        return func.getDefinitionString();            
    }   
    
    /** */
    @Override
    public String getReplacementText() {
        return func.getReplacementText();
    } 

    /** */
    @Override
    public String getDisplayText() {
        return func.getDisplayText();
    }
    
    /** */
    @Override
    public String getDisplayHTML() {
        return func.getDisplayHTML();
    }    
    
    /** */
    @Override
    public Icon getIcon() {
        return func.getIcon();
    }
    
    /** */
    @Override
    public String getLowerCaseName() {
        return func.getLowerCaseName();
    }   
    
    /** */
    @Override
    public String getLowerCasePrefix() {
        return func.getLowerCasePrefix();
    }
    
    /** */
    @Override
    public String getLowerCaseFullName() {
        return func.getLowerCaseFullName();
    }
    
    /** */
    @Override
    public String toString() {
        return func.getDisplayText();
    }    
}