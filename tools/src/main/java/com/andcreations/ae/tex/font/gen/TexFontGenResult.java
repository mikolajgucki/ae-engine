package com.andcreations.ae.tex.font.gen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class TexFontGenResult {
    /** */
    private List<TexFontGenChar> chars;
    
    /** */
    void addChar(TexFontGenChar ch) {
        if (chars == null) {
            chars = new ArrayList<>();
        }
        chars.add(ch);
    }
    
    /** */
    public List<TexFontGenChar> getChars() {
        return Collections.unmodifiableList(chars);
    }
}