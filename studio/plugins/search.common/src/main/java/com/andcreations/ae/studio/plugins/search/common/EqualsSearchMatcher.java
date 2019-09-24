package com.andcreations.ae.studio.plugins.search.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class EqualsSearchMatcher implements SearchMatcher {
    /** */
    private boolean matchCase;
    
    /** */
    private String searchText;    
    
    /** */
    public EqualsSearchMatcher() {
    }
    
    /** */
    @Override
    public void init(boolean matchCase,String searchText)
        throws SearchException {
    //
        this.matchCase = matchCase;
        this.searchText = searchText;
    }
    
    /** */
    @Override
    public List<SearchOccurence> match(String line,int lineNo) {
        String originalLine = line;
        if (matchCase == false) {
            searchText = searchText.toLowerCase();
            line = line.toLowerCase();
        }
        
        List<SearchOccurence> occurences = new ArrayList<>();
        int length = searchText.length();
        int fromIndex = 0;
        while (true) {
            int indexOf = line.indexOf(searchText,fromIndex);
            if (indexOf == -1) {
                break;
            }
            occurences.add(new SearchOccurence(
                lineNo,originalLine,indexOf,length));
            fromIndex = indexOf + length;
        }
        
        return occurences;
    }
}