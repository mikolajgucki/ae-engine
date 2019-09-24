package com.andcreations.ae.studio.plugins.ui.common;

import java.awt.Font;

/**
 * @author Mikolaj Gucki
 */
public class UIFonts {
    /** */
    public static Font getSmallFont(Font font) {
        int size = font.getSize();
        return font.deriveFont((float)(size * 0.85));
    }
}