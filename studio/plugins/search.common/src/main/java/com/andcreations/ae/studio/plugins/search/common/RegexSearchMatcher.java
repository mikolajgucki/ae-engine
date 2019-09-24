package com.andcreations.ae.studio.plugins.search.common;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class RegexSearchMatcher implements SearchMatcher {
    /** */
    private BundleResources res =
        new BundleResources(RegexSearchMatcher.class); 
    
    /** */
    private Pattern pattern;
    
    /** */
    public RegexSearchMatcher() {
    }
    
    /** */
    @Override
    public void init(boolean matchCase,String searchText)
        throws SearchException {
    // flags
        int flags = 0;
        if (matchCase == false) {
            flags = Pattern.CASE_INSENSITIVE;
        }
        
    // pattern
        try {
            pattern = Pattern.compile(String.format("(%s)",searchText),flags);
        } catch (PatternSyntaxException exception) {
            throw new SearchException(res.getStr("invalid.pattern"));
        }        
    }
    
    /** */
    public List<SearchOccurence> match(String line,int lineNo) {
        List<SearchOccurence> occurences = new ArrayList<>();
        
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
        // TODO Finish regex matcher.
        // the first group contains the matched string
        /*
            String match = matcher.group(1);
            int start = matcher.start(1);
            int end = matcher.end(1);
            
            occurences.add(new SearchOccurence(lineNo,line,start,end - start));
        */
        }
        
        return occurences;
    }
}