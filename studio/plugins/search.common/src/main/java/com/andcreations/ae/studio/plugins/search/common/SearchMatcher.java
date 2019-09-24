package com.andcreations.ae.studio.plugins.search.common;

import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public interface SearchMatcher {
    /** */
    void init(boolean matchCase,String searchText) throws SearchException;
    
    /** */
    List<SearchOccurence> match(String line,int lineNo);
}