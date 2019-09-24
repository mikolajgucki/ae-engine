package com.andcreations.ae.studio.plugins.assets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.andcreations.ae.studio.plugins.builders.Builder;
import com.andcreations.ae.studio.plugins.builders.Builders;

/**
 * @author Mikolaj Gucki
 */
public class AssetsBuilderPipeline {
    /** */
    private static AssetsBuilderPipeline instance;
    
    /** */
    private List<AssetsBuilder> builders = new ArrayList<>();
    
    /** */
    public void appendBuilder(AssetsBuilder builder) {
        builders.add(builder);
    }
    
    /** */
    List<AssetsBuilder> getBuilders() {
        return Collections.unmodifiableList(builders);
    }
    
    /** */
    void runPipeline() {        
        Builders.get().postBuild(new ArrayList<Builder>(builders));
    }
    
    /** */
    void init() {
    }
    
    /** */
    public static AssetsBuilderPipeline get() {
        if (instance == null) {
            instance = new AssetsBuilderPipeline();
        }
        
        return instance;
    }
}