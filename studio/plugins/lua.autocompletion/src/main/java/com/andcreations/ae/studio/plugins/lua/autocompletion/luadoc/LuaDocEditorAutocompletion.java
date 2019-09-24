package com.andcreations.ae.studio.plugins.lua.autocompletion.luadoc;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.andcreations.ae.studio.plugins.lua.parser.LuaParsedFile;
import com.andcreations.ae.studio.plugins.lua.parser.LuaParser;
import com.andcreations.ae.studio.plugins.text.editor.autocomplete.AutocomplSource;
import com.andcreations.ae.studio.plugins.text.editor.autocomplete.FuncAutocompl;
import com.andcreations.ae.studio.plugins.text.editor.autocomplete.VarAutocompl;

/**
 * @author Mikolaj Gucki
 */
class LuaDocEditorAutocompletion {
    /** */
    private File editedFile;
    
    /** */
    private Map<File,LuaDocFileAutocompletion> fileAutocompletions =
        new HashMap<File,LuaDocFileAutocompletion>();
        
    /** */    
    private AutocomplSource source = new AutocomplSource();
    
    /** */
    LuaDocEditorAutocompletion(File editedFile) {
        this.editedFile = editedFile;
        create();
    }
    
    /** */
    void create() {
        for (LuaParsedFile parsedFile:LuaParser.get().getParsedFiles()) {
            addCompletions(parsedFile);
        }
    }
    
    /** */
    void addCompletions(LuaParsedFile parsedFile) {
        LuaDocFileAutocompletion fileAutocompletion =
            new LuaDocFileAutocompletion(editedFile,parsedFile);
        fileAutocompletions.put(parsedFile.getFile(),fileAutocompletion);
        
    // add to the source
        for (FuncAutocompl func:fileAutocompletion.getFuncs()) {
            source.addFunc(func);
        }
        for (VarAutocompl var:fileAutocompletion.getVars()) {
            source.addVar(var);
        }
    }
    
    /** */
    void removeCompletions(File file) {
        LuaDocFileAutocompletion fileAutocompletion =
            fileAutocompletions.get(file);
        if (fileAutocompletion != null) {
       // remove from the source
           for (FuncAutocompl func:fileAutocompletion.getFuncs()) {
               source.removeFunc(func);
           }
           for (VarAutocompl var:fileAutocompletion.getVars()) {
               source.removeVar(var);
           }
        }            
    }
    
    /** */
    AutocomplSource getSource() {
        return source;
    }
}