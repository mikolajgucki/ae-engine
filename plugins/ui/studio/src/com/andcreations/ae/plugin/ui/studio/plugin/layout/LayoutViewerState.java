package com.andcreations.ae.plugin.ui.studio.plugin.layout;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Mikolaj Gucki
 */
public class LayoutViewerState {
    /** */
    private List<LayoutSize> sizes = new ArrayList<>();
    
    /** */
    private List<String> userModulesToLoad = new ArrayList<>();
    
    /** */
    public LayoutViewerState() {
    }
    
    /** */
    public void setSizes(List<LayoutSize> sizes) {
        this.sizes = sizes;
    }
    
    /** */
    public List<LayoutSize> getSizes() {
        return sizes;
    }
    
    /** */
    public void addSize(LayoutSize size) {
        sizes.add(size);
    }
    
    /** */
    public void removeSize(String module) {
        LayoutSize size = findSize(module);
        sizes.remove(size);
    }
    
    /** */
    public LayoutSize findSize(String module) {
        for (LayoutSize size:sizes) {
            if (size.getModule().equals(module) == true) {
                return size;
            }
        }
        
        return null;
    }
    
    /** */
    public void setUserModulesToLoad(List<String> userModulesToLoad) {
        this.userModulesToLoad = userModulesToLoad;
    }
    
    /** */
    public List<String> getUserModulesToLoad() {
        return userModulesToLoad;
    }
}