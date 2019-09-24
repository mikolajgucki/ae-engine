package com.andcreations.lua;

import java.util.Locale;

/**
 * @author Mikolaj Gucki
 */
public class LuaTypes {
    /**
     * Converts an integer to a Lua number literal.
     *
     * @param value The integer.
     * @return The Lua number literal.
     */
    public static String intToStr(int value) {
        return String.format(Locale.US,"%d",value);
    }
    
    /**
     * Converts a number to a Lua number literal.
     *
     * @param value The number.
     * @return The Lua number literal.
     */
    public static String numberToStr(double value) {
        return String.format(Locale.US,"%.4f",value);
    }
}