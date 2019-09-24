package com.andcreations.ae.studio.plugins.text.editor;

import java.awt.Color;

import javax.swing.UIManager;

import com.andcreations.ae.studio.plugins.ui.common.UIColors;

/**
 * The colors for the text area.
 *
 * @author Mikolaj Gucki
 */
class Colors {
    /** */
    static final String GRAY = "888888";
    
    /** */
    static final String LIGHT_GRAY = "b0b0b0";
    
    /** */
    static final String RED = "df5f5a";
    
    /** */
    static final String DARK_RED = "662b29";
    
    /** */
    static final String GREEN = "a1b56c";
    
    /** */
    static final String PALE_GREEN = "abb591";
    
    /** */
    static final String BLUE = "7cafc2";
    
    /** */
    static final String VIOLET = "ba8baf";
    
    /** */
    static final String YELLOW = "f7ca88";
    
    /** */
    static final String BROWN = "d78c5e";
    
    /** */
    static Color getBackgroundColor() {
        return UIColors.blend(            
            UIManager.getColor("Panel.background"),Color.BLACK,0.5);        
    }
}