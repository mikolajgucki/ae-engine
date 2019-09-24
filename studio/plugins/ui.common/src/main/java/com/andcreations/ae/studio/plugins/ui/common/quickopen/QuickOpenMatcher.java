package com.andcreations.ae.studio.plugins.ui.common.quickopen;

/**
 * The interface for matcher which match item values and pattern.
 *
 * @author Mikolaj Gucki
 */
public interface QuickOpenMatcher {
    /**
     * Matches a value against a pattern.
     *
     * @param value The value.
     * @param pattern The pattern.
     * @return <code>true</code> if matches, <code>false</code> otherwise.
     */
    boolean match(String value,String pattern);
}