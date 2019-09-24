package com.andcreations.ae.lua.doc;

/**
 * @author Mikolaj Gucki
 */
public class LuaDocVar implements LuaDocNamedElement {
    /** */
    public static final String ERROR_NOT_FOLLOWED_BY_VAR =
        "not.followed.by.var";
    
    /** */
    public static final String ERROR_INVALID_EXPRESSION = "var.invalid.exp";    
    
    /** */
    private String definedIn;
    
    /** */
    private String name;
    
    /** */
    private String localName;
    
    /** */
    private String globalName;    
    
    /** */
    private String brief;
    
    /** */
    private String full;        
    
    /** */
    private boolean local;

    /** */
    private int line;    
    
    /** */
    LuaDocVar() {
    }
    
    /** */
    public void setDefinedIn(String definedIn) {
        this.definedIn = definedIn;
    }
    
    /** */
    public String getDefinedIn() {
        return definedIn;
    }
    
    /** */
    public void setName(String name) {
        this.name = name;
    }
    
    /** */
    @Override    
    public String getName() {
        return name;
    }
    
    /** */
    public void setLocalName(String localName) {
        this.localName = localName;
    }
    
    /** */
    @Override    
    public String getLocalName() {
        return localName;
    }        
    
    /** */
    @Override
    public String getGlobalName() {
        return globalName;
    }    
    
    /** */
    public void setBrief(String brief) {
        this.brief = brief;
    }
    
    /** */
    public String getBrief() {
        return brief;
    }
    
    /** */
    public void setFull(String full) {
        this.full = full;
    }
    
    /** */
    public String getFull() {
        return full;
    }    

    /** */
    public void setLocal(boolean local) {
        this.local = local;
    }
    
    /** */
    public boolean isLocal() {
        return local;
    }    
    
    /** */
    public void setLine(int line) {
        this.line = line;
    }
    
    /** */
    public int getLine() {
        return line;
    }     
    
    /** */
    void updateGlobalName(String moduleName) {
        if (local == true) {
            return;
        }
        
        if (localName == null) {
            globalName = name;
            return;
        }
        
        globalName = moduleName + "." + localName;
    }    
}