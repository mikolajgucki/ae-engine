package com.andcreations.ae.io;

/**
 * Provides Base16-related methods.
 *
 * @author Mikolaj Gucki
 */
public class Base16 {
    /**
     * Gets the digit value.
     *
     * @param digit The digit.
     * @return The digit value or -1 if the digit is invalid.
     */
    public static int getDigitValue(int digit) {
        if (digit >= 'a' && digit <= 'f') {
            return digit - 'a' + 10;
        }
        if (digit >= 'A' && digit <= 'F') {
            return digit - 'A' + 10;
        }
        if (digit >= '0' && digit <= '9') {
            return digit - '0';
        }
        
        return -1;
    }
    
    /**
     * Gets a digit character.
     *
     * @param value The value.
     * @return The digit character or -1 if the value is invalid.
     */
    public static int getDigit(int value) {
        if (value < 10) {
            return '0' + value;
        }
        if (value < 16) {
            return 'a' + value - 10;
        }
        
        return -1;
    }
}