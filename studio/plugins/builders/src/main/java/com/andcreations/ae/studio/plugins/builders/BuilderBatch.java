package com.andcreations.ae.studio.plugins.builders;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
class BuilderBatch {
    /** */
    private List<Builder> builders;
    
    /** */
    BuilderBatch(Builder builder) {
        builders = new ArrayList<>();
        builders.add(builder);
    }
    
    /** */
    BuilderBatch(List<Builder> builders) {
        this.builders = new ArrayList<>(builders);
    }
    
    /** */
    List<Builder> getBuilders() {
        return builders;
    }
}