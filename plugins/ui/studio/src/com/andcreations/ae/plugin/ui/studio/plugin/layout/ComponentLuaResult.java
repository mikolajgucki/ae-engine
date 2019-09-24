package com.andcreations.ae.plugin.ui.studio.plugin.layout;

import java.util.List;

/**
 * @author Mikolaj Gucki
 */
class ComponentLuaResult {
    /** */
    private String id;
    
    /** */
    private BoundsLuaResult bounds;
    
    /** */
    private List<ComponentLuaResult> children;
    
    /** */
    public void setId(String id) {
        this.id = id;
    }
    
    /** */
    public String getId() {
        return id;
    }
    
    /** */
    public void setBounds(BoundsLuaResult bounds) {
        this.bounds = bounds;
    }
    
    /** */
    public BoundsLuaResult getBounds() {
        return bounds;
    }
    
    /** */
    public void setChildren(List<ComponentLuaResult> children) {
        this.children = children;
    }
    
    /** */
    public List<ComponentLuaResult> getChildren() {
        return children;
    }
    
    /** */
    boolean hasChildren() {
        return children != null && children.isEmpty() == false;
    }
}
