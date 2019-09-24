package com.andcreations.ae.assets;

import java.io.File;
import java.util.List;

import com.andcreations.ae.issue.Issue;

/**
 * @author Mikolaj Gucki
 */
public interface TexPackBuilderListener {    
    /** */
    void loadingTexPack(File file);
    
    /** */
    void loadedTexPack(File file);
    
    /** */
    void failedToLoadTexPack(File file,Exception exception);
    
    /** */
    void failedToLoadTexPack(File file,List<Issue> issues);
    
    /** */
    void buildingTexPack(File file);
    
    /** */
    void texPackUpToDate(File file);
    
    /** */
    void didBuildTexPack(File file);
    
    /** */
    void failedToBuildTexPack(File file,Exception exception);
}