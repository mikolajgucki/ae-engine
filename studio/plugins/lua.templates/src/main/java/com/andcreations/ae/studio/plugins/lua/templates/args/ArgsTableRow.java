package com.andcreations.ae.studio.plugins.lua.templates.args;

/**
 * @author Mikolaj Gucki
 */
public class ArgsTableRow {
    /** */
    private String name;
    
    /** */
    private String desc;
    
    /** */
    ArgsTableRow(String name) {
        this.name = name;
        this.desc = "";
    }
    
    /** */
    public String getName() {
        return name;
    }
    
    /** */
    public void setName(String name) {
        this.name = name;
    }
    
    /** */
    public String getDesc() {
        return desc;
    }
    
    /** */
    public void setDesc(String desc) {
        this.desc = desc;
    }
}