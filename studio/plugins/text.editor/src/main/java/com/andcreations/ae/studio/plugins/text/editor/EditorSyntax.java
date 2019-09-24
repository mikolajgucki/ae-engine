package com.andcreations.ae.studio.plugins.text.editor;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

/**
 * @author Mikolaj Gucki
 */
public enum EditorSyntax {
    /** */
    LUA(AELuaTokenMaker.SYNTAX_STYLE),
    
    /** */
    JSON(SyntaxConstants.SYNTAX_STYLE_JSON),
    
    /** */
    JAVA(SyntaxConstants.SYNTAX_STYLE_JAVA),
    
    /** */
    XML(SyntaxConstants.SYNTAX_STYLE_XML),
    
    /** */
    PROPERTIES(SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE),
    
    /** */
    NONE(SyntaxConstants.SYNTAX_STYLE_NONE);
    
    /** */
    private String textAreaEditingStyle;
    
    /** */
    private EditorSyntax(String textAreaEditingStyle) {
        this.textAreaEditingStyle = textAreaEditingStyle;
    }
    
    /** */
    public String getTextAreaEditingStyle() {
        return textAreaEditingStyle;
    }
}