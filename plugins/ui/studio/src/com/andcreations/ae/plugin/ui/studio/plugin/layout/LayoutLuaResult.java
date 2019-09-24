package com.andcreations.ae.plugin.ui.studio.plugin.layout;

/**
 * @author Mikolaj Gucki
 */
class LayoutLuaResult {
    /** */
    private String error;
    
    /** */
    private ComponentLuaResult root;
    
    /** */
    public void setError(String error) {
        this.error = error;
    }
    
    /** */
    public String getError() {
        return error;
    }
    
    /** */
    public void setRoot(ComponentLuaResult root) {
        this.root = root;
    }
    
    /** */
    public ComponentLuaResult getRoot() {
        return root;
    }
}