package com.andcreations.ae.desktop;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mikolaj Gucki
 */
public class LuaValueUtil {
    /**
     * Removes the fraction part if it contails only zeros.
     *
     * @param str The number.
     * @return The formatted number.
     */
    public static String formatNumber(String str) {
        Pattern pattern = Pattern.compile("([0-9]+)\\.([0-9]*)");
        Matcher matcher = pattern.matcher(str);
    // match pattern xxx.yyy
        if (matcher.matches() == true) {
        // check if the fraction part contains only zeros
            boolean zeros = true;
            String fraction = matcher.group(2);
            for (int index = 0; index < fraction.length(); index++) {
                if (fraction.charAt(index) != '0') {
                    zeros = false;
                    break;
                }
            }
            
        // ...and if so pick it as an integer
            if (zeros == true) {
                int indexOf = str.indexOf('.');
                return str.substring(0,indexOf);
            }
        }
        
        return str;
    }    
}