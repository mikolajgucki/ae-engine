package com.andcreations.ae.assets;

import java.io.File;
import java.util.List;

import com.andcreations.ae.issue.Issue;

/**
 * @author Mikolaj Gucki
 */
public interface TexFontBuilderListener {
    /** */
    void loadingTexFont(File file);
    
    /** */
    void loadedTexFont(File file);
    
    /** */
    void failedToLoadTexFont(File file,Exception exception);
    
    /** */
    void failedToLoadTexFont(File file,List<Issue> issues);
    
    /** */
    void buildingTexFont(File file);    
    
    /** */
    void texFontUpToDate(File file);
    
    /** */
    void didBuildTexFont(File file);
    
    /** */
    void failedToBuildTexFont(File file,Exception exception);
    
    /** */
    void texFontExtUpToDate(File file);
    
    /** */
    void didExtTexFont(File file);
    
    /** */
    void failedToExtTexFont(File file,Exception exception);
}