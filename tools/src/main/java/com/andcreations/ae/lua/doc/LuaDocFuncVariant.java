package com.andcreations.ae.lua.doc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class LuaDocFuncVariant {
    /** */
    private String brief;
    
    /** */
    private String full;    
    
    /** */
    private List<LuaDocFuncParam> params = new ArrayList<>();
    
    /** */
    private String rtrn;
    
    /** */
    private int line;
   
    /** */
    LuaDocFuncVariant() {
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
    public void addParam(LuaDocFuncParam param) {
        params.add(param);
    }
    
    /** */
    public List<LuaDocFuncParam> getParams() {
        return Collections.unmodifiableList(params);
    }
    
    /** */
    public LuaDocFuncParam getParam(String name) {
        for (LuaDocFuncParam param:params) {
            if (param.getName().equals(name) == true) {
                return param;
            }
        }
        
        return null;
    }
    
    /** */
    public void setReturn(String rtrn) {
        this.rtrn = rtrn;
    }
    
    /** */
    public String getReturn() {
        return rtrn;
    }
    
    /** */
    public void setLine(int line) {
        this.line = line;
    }
    
    /** */
    public int getLine() {
        return line;
    }         
}