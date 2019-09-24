package com.andcreations.ae.studio.plugins.ui.common.quickopen;

/**
 * Splits value and pattern using supplied regex and matches the tokens using
 * supplied matcher.
 *
 * @author Mikolaj Gucki
 */
public class SeparatorQuickOpenMatcher implements QuickOpenMatcher {
    /** */
    private String regex;
    
    /** */
    private QuickOpenMatcher matcher;
    
    /** */
    private boolean camelCaseSplit;
    
    /** */
    public SeparatorQuickOpenMatcher(String regex,QuickOpenMatcher matcher,
        boolean camelCaseSplit) {
    //
        this.regex = regex;
        this.matcher = matcher;
        this.camelCaseSplit = camelCaseSplit;
    }
    
    /** */
    public SeparatorQuickOpenMatcher(String regex,QuickOpenMatcher matcher) {
        this(regex,matcher,false);
    }
    
    /** */
    private String doCamelCaseSplit(String input) {
        StringBuilder output = new StringBuilder();
        
        for (int index = 0; index < input.length(); index++) {
            char ch = input.charAt(index);
            if (ch >= 'A' && ch <= 'Z' && index > 0) {
                output.append(" ");
            }
            output.append(ch);
        }
        
        return output.toString();
    }
    
    /** */
    @Override
    public boolean match(String value,String pattern) {
        if (camelCaseSplit == true) {
            pattern = doCamelCaseSplit(pattern);
        }
        
        String[] values = value.split(regex);
        String[] patterns = pattern.split(regex);
        
        int index = 0;
        while (index < values.length && index < patterns.length) {
            if (matcher.match(values[index],patterns[index]) == false) {
                return false;
            }
            
            index++;
        }
        
        return true;
    }
}