package com.andcreations.ae.studio.plugins.lua.explorer;

import com.andcreations.ae.studio.plugins.ui.common.quickopen.DefaultQuickOpenMatcher;
import com.andcreations.ae.studio.plugins.ui.common.quickopen.QuickOpenMatcher;

/** 
 * @author Mikolaj Gucki
 */
class GoToLuaItemMatcher implements QuickOpenMatcher {
    /** */
    private DefaultQuickOpenMatcher defaultMatcher =
        new DefaultQuickOpenMatcher();
        
    /** */
    @Override
    public boolean match(String value,String pattern) {
    // match all if no pattern is given
        if (pattern == null || pattern.length() == 0) {
            return true;
        }        
        
        if (defaultMatcher.match(value,pattern) == true) {
            return true;
        }
        
        String[] tokens = value.split("[\\.\\:,]");
        for (String token:tokens) {
            if (defaultMatcher.match(token,pattern) == true) {
                return true;
            }
        }
        
        return false;
    }
}