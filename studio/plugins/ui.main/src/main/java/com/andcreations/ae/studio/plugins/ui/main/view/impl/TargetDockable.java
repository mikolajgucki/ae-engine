package com.andcreations.ae.studio.plugins.ui.main.view.impl;

import java.util.HashMap;
import java.util.Map;

import bibliothek.gui.Dockable;

import com.andcreations.ae.studio.plugins.ui.main.view.ViewCategory;

/**
 * @author Mikolaj Gucki
 */
class TargetDockable {
    /** The dockable. */
    private Dockable dockable;
    
    /** The count map. */
    private Map<ViewCategory,Integer> countMap = new HashMap<>();
    
    /** */
    TargetDockable(Dockable dockable) {
        this.dockable = dockable;
    }
    
    /** */
    Dockable getDockable() {
        return dockable;
    }
    
    /** */
    void inc(ViewCategory category) {
        Integer count = countMap.get(category);
        if (count == null) {
            count = Integer.valueOf(0);
        }
        
        countMap.put(category,Integer.valueOf(count.intValue() + 1));
    }
    
    /** */
    int getCount(ViewCategory category) {
        Integer count = countMap.get(category);
        return count != null ? count.intValue() : 0;
    }
}   
