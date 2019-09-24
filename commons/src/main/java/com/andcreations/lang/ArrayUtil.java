package com.andcreations.lang;

/**
 * Provides array-related utility methods.
 *
 * @author Mikolaj Gucki
 */
public class ArrayUtil {
    /** */
    public static String toListString(Object[] objects,String separator) {
        StringBuilder str = new StringBuilder();
        
        for (Object object:objects) {
            if (str.length() > 0) {
                str.append(separator);
            }
            str.append(object.toString());
        }
        
        return str.toString();
    }
}