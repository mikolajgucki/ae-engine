package com.andcreations.ae.lua.doc;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class LuaDocFileData {
    /** */    
    private transient File file;
    
    /** */
    private LuaDocModule module;
    
    /** */
    private List<LuaDocVar> vars = new ArrayList<>();
    
    /** */
    private List<LuaDocFunc> funcs = new ArrayList<>();
    
    /** */
    private List<LuaDocWarning> warnings = new ArrayList<>();
    
    /** */
    private List<LuaDocError> errors = new ArrayList<>();
    
    /** */
    public LuaDocFileData() {
    }
    
    /** */    
    public void setFile(File file) {
        this.file = file;
    }
    
    /** */
    public File getFile() {
        return file;
    }
    
    /** */
    public void setModule(LuaDocModule module) {
        this.module = module;
    }
    
    /** */
    public LuaDocModule getModule() {
        if (module == null) {
            module = new LuaDocModule();
        }
        return module;
    }
    
    /** */
    public void addVar(LuaDocVar var) {
        vars.add(var);
    }
    
    /** */
    public List<LuaDocVar> getVars() {
        return Collections.unmodifiableList(vars);
    }    
    
    /** */
    public void addFunc(LuaDocFunc func) {
        funcs.add(func);
    }
    
    /** */
    public List<LuaDocFunc> getFuncs() {
        return Collections.unmodifiableList(funcs);
    }
    
    /** */
    public void addWarning(LuaDocWarning warning) {
        warnings.add(warning);
    }
    
    /** */
    public List<LuaDocWarning> getWarnings() {
        return Collections.unmodifiableList(warnings);
    }
    
    /** */
    public void addError(LuaDocError error) {
        errors.add(error);
    }
    
    /** */
    public List<LuaDocError> getErrors() {
        return Collections.unmodifiableList(errors);
    }    
    
    /** */
    boolean isEmpty() {
        return funcs.isEmpty() && vars.isEmpty() && warnings.isEmpty() &&
            errors.isEmpty();
    }
}