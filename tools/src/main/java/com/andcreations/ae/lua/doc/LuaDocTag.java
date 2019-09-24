package com.andcreations.ae.lua.doc;


/**
 * Represents a LuaDoc comment tag along with its value.
 *
 * @author Mikolaj Gucki
 */
public class LuaDocTag {
    /** */
    static final String MODULE = "module";    
    
    /** */
    static final String GROUP = "group";    
    
    /** */
    static final String VAR = "var";
    
    /** */
    static final String FUNC = "func";
    
    /** */
    static final String NAME = "name";
    
    /** */
    static final String BRIEF = "brief";
    
    /** */
    static final String FULL = "full";
    
    /** */
    static final String PARAM = "param";
    
    /** */
    static final String RETURN = "return";
    
    /** The tag names. */
    public static final String[] NAMES = {
        MODULE, GROUP, VAR, FUNC, NAME, BRIEF, FULL, PARAM, RETURN
    };
    
    /** The tag name. */
    private String name;
    
    /** The tag value. */
    private String value;
    
    /** The line number in the file. */
    private int line;
    
    /** */
    LuaDocTag(String name,String value,int line) {
        this.name = name;
        this.line = line;
        setValue(value);
    }
    
    private void setValue(String value) {
        if (value.trim().length() == 0) {
            value = null;
        }
        this.value = value;
    }
    
    /** */
    public String getName() {
        return name;
    }
    
    /** */
    public boolean matchName(String name) {
        return this.name.equals(name);
    }
    
    /** */
    public String getValue() {
        return value;
    }
    
    /** */
    public int getLine() {
        return line;
    }
    
    /** */
    @Override
    public String toString() {
        return String.format("[%d]%s %s",line,name,value);
    }
}