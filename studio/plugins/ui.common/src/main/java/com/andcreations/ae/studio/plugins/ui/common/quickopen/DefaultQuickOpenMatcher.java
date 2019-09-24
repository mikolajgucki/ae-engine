package com.andcreations.ae.studio.plugins.ui.common.quickopen;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The interface for matcher which match item values and pattern.
 *
 * @author Mikolaj Gucki
 */
public class DefaultQuickOpenMatcher implements QuickOpenMatcher {
    /** */
    @Override
    public boolean match(String value,String pattern) {
    // match all if no pattern is given
        if (pattern == null || pattern.length() == 0) {
            return true;
        }
        
        pattern = pattern
            .replaceAll("\\.","\\\\.")
            .replaceAll("\\*",".*")
            .replaceAll("\\?",".");        
        Pattern regex = Pattern.compile(pattern,Pattern.CASE_INSENSITIVE);
        
        Matcher matcher = regex.matcher(value);
        return matcher.lookingAt();        
    }
}