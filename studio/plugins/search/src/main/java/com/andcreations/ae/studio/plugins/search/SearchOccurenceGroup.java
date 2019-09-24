package com.andcreations.ae.studio.plugins.search;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.search.common.SearchOccurence;

/**
 * Groups a number of occurences.
 *
 * @author Mikolaj Gucki
 */
class SearchOccurenceGroup {
    /** */
    private SearchSource source;
    
    /** */
    private SearchSourceDocument document;
    
    /** */
    private List<SearchOccurence> occurences = new ArrayList<>();

    /** */
    SearchOccurenceGroup(SearchSource source,SearchSourceDocument document,
        List<SearchOccurence> occurences) {
    //
        this.source = source;
        this.document = document;
        this.occurences = occurences;
    }
    
    /** */
    SearchSource getSource() {
        return source;
    }
    
    /** */
    String getDocumentId() {
        return document.getId();
    }
    
    /** */
    String getName() {
        return document.getName();
    }
    
    /** */
    ImageIcon getIcon() {
        return document.getIcon();
    }
    
    /** */
    List<SearchOccurence> getOccurences() {
        return occurences;
    }
}