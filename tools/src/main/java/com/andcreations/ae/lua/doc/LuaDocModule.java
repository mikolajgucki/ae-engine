package com.andcreations.ae.lua.doc;

/**
 * @author Mikolaj Gucki
 */
public class LuaDocModule {
    /** */
    private String name;
    
    /** */
    private String brief;
    
    /** */
    private String full;
    
    /** */
    private String group;
   
    /** */
    public LuaDocModule() {
    }
    
    /** */
    public void setName(String name) {
        this.name = name;
    }
    
    /** */
    public String getName() {
        return name;
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
    public void setGroup(String group) {
        this.group = group;
    }
    
    /** */
    public String getGroup() {
        return group;
    }    
    
    /** */
    public String getElementLocalName(String elementName) {
        if (name == null) {
            return null;
        }
        
    // last component of the module name    
        String[] components = name.split("\\.");
        String last = components[components.length - 1];
        
    // first component of element name
        int indexOf = elementName.indexOf('.');
        if (indexOf == -1) {
            indexOf = elementName.indexOf(':');
        }
        if (indexOf == -1) {
            return null;
        }
        String first = elementName.substring(0,indexOf);
        
    // if the first component of the element name is the same as the
    // last component of the module name, then element name starts with
    // the module name
        if (last.equals(first)) {
            return elementName.substring(indexOf + 1,elementName.length());
        }
        
        return null;
    }
}