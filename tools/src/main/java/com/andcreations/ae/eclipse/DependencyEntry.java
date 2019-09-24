package com.andcreations.ae.eclipse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
class DependencyEntry {
    /** */
    private String id;
    
    /** */
    private List<String> dependencies = new ArrayList<>();
    
    /** */
    DependencyEntry(String id,List<String> dependencies) {
        this.id = id;
        this.dependencies = dependencies;
    }
    
    /** */
    String getId() {
        return id;
    }
    
    /** */
    List<String> getDependencies() {
        return dependencies;
    }
    
    /** */
    boolean hasDependencies() {
        return dependencies != null && dependencies.isEmpty() == false;
    }
}
