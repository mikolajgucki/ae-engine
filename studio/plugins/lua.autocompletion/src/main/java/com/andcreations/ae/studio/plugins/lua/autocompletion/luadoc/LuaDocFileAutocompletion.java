package com.andcreations.ae.studio.plugins.lua.autocompletion.luadoc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.lua.doc.LuaDocFileData;
import com.andcreations.ae.lua.doc.LuaDocFunc;
import com.andcreations.ae.lua.doc.LuaDocVar;
import com.andcreations.ae.studio.plugins.lua.parser.LuaParsedFile;
import com.andcreations.ae.studio.plugins.text.editor.autocomplete.FuncAutocompl;
import com.andcreations.ae.studio.plugins.text.editor.autocomplete.VarAutocompl;

/**
 * @author Mikolaj Gucki
 */
class LuaDocFileAutocompletion {
    /** */
    private File editedFile;
    
    /** */
    private List<FuncAutocompl> funcs = new ArrayList<>();
    
    /** */
    private List<VarAutocompl> vars = new ArrayList<>();
    
    /** */
    LuaDocFileAutocompletion(File editedFile,LuaParsedFile parsedFile) {
        this.editedFile = editedFile;
        create(parsedFile);
    }
    
    /** */
    private void create(LuaParsedFile parsedFile) {
        createFuncs(parsedFile);
        createVars(parsedFile);
    }
    
    /** */
    private void createFuncs(LuaParsedFile parsedFile) {
        LuaDocFileData luaDocData = parsedFile.getLuaDocInfo();
    // for each function
        for (LuaDocFunc luaDocFunc:luaDocData.getFuncs()) {
            if (luaDocFunc.isLocal() == true &&
                editedFile.equals(parsedFile.getFile()) == false) {
            //
                continue;
            }
            
            funcs.addAll(LuaDocFuncConverter.convert(luaDocFunc));
        }        
    }
    
    /** */
    private void createVars(LuaParsedFile parsedFile) {
        LuaDocFileData luaDocData = parsedFile.getLuaDocInfo();
    // for each variable
        for (LuaDocVar luaDocVar:luaDocData.getVars()) {
            if (luaDocVar.isLocal() == true &&
                editedFile.equals(parsedFile.getFile()) == false) {
            //
                continue;
            }
            
            vars.add(LuaDocVarConverter.convert(luaDocVar));
        }
    }
    
    /** */
    List<FuncAutocompl> getFuncs() {
        return funcs;
    }
    
    /** */
    List<VarAutocompl> getVars() {
        return vars;
    }
}