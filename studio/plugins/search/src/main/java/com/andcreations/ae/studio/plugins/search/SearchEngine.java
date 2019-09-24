package com.andcreations.ae.studio.plugins.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.studio.plugins.search.common.EqualsSearchMatcher;
import com.andcreations.ae.studio.plugins.search.common.RegexSearchMatcher;
import com.andcreations.ae.studio.plugins.search.common.SearchException;
import com.andcreations.ae.studio.plugins.search.common.SearchMatcher;
import com.andcreations.ae.studio.plugins.search.common.SearchOccurence;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class SearchEngine {
    /** */
    private BundleResources res = new BundleResources(SearchEngine.class);     
    
    /** */
    private SearchOccurenceGroup search(SearchSource source,
        SearchSourceDocument document,SearchMatcher matcher)
        throws IOException {
    //
        List<SearchOccurence> occurences = new ArrayList<>();
        BufferedReader reader = new BufferedReader(
            new StringReader(document.getText()));
        
        int lineNo = 0;
    // for each line
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            lineNo++;
            
            occurences.addAll(matcher.match(line,lineNo));
        }
        
        if (occurences.isEmpty() == true) {
            return null;
        }
        return new SearchOccurenceGroup(source,document,occurences);
    }    
    
    /** */
    private List<SearchOccurenceGroup> search(SearchSource source,
        SearchMatcher matcher) throws IOException {
    //
        List<SearchOccurenceGroup> groups = new ArrayList<>();        
    // for each document
        for (SearchSourceDocument document:source.getDocuments()) {
            SearchOccurenceGroup group = search(
                source,document,matcher);
            if (group != null) {
                groups.add(group);
            }
        }
        
        return groups;
    }    
    
    /** */
    List<SearchOccurenceGroup> search(List<SearchSource> searchSourceList,
        String searchText,boolean matchCase,boolean regex) throws IOException {
    // matcher
        SearchMatcher matcher;
        if (regex == false) {
            matcher = new EqualsSearchMatcher();
        }
        else {
            matcher = new RegexSearchMatcher();
        }
        
        try {
            matcher.init(matchCase,searchText);
        } catch (SearchException exception) {
            CommonDialogs.error(res.getStr("error.dialog.title"),
                exception.getMessage());
            return null;
        }
    
        List<SearchOccurenceGroup> groups = new ArrayList<>();
    // for each source
        for (SearchSource source:searchSourceList) {
            groups.addAll(search(source,matcher));
        }
        
        return groups;
    }
}