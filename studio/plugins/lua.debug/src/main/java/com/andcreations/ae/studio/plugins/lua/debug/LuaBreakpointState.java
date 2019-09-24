package com.andcreations.ae.studio.plugins.lua.debug;

/**
 * @author Mikolaj Gucki
 */
public class LuaBreakpointState {
    /** */
    private String source;
    
    /** */
    private int line;
    
    /** */
    private boolean enabled;
    
    /** */
    public LuaBreakpointState() {
    }
    
    /** */
    LuaBreakpointState(String source,int line,boolean enabled) {
        this.source = source;
        this.line = line;
        this.enabled = enabled;
    }
    
    /** */
    public void setSource(String source) {
        this.source = source;
    }
    
    /** */
    public String getSource() {
        return source;
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
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /** */
    public boolean getEnabled() {
        return enabled;
    }
}