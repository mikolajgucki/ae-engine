package com.andcreations.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides string-related utility methods.
 *
 * @author Mikolaj Gucki
 */
public class Strings {
    /**
     * Removes all the leading characters <= 32.
     *
     * @param str The input string.
     * @return The trimmed string.
     */
    public static String ltrim(String str) {
        StringBuilder result = new StringBuilder(str);
        while (result.length() > 0 && result.charAt(0) <= 32) {
            result.deleteCharAt(0);
        }
        
        return result.toString();
    }
    
    /**
     * Removes all the trailing characters <= 32.
     *
     * @param str The input string.
     * @return The trimmed string.
     */
    public static String rtrim(String str) {
        StringBuilder result = new StringBuilder(str);
        
        while (true) {
            int length = result.length();
            if (length == 0) {
                break;
            }
            if (result.charAt(length - 1) <= 32) {
                result.deleteCharAt(length - 1);
            }
            else {
                break;
            }
        }
        
        return result.toString();
    }    
    
    /** */
    public static List<String> splitByNewline(String str) {
        BufferedReader reader = new BufferedReader(new StringReader(str));
        List<String> lines = new ArrayList<String>();        
    // read lines
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();        
        } catch (IOException exception) {
            // should never happen
        }
        
        return lines;
    }
}