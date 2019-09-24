package com.andcreations.ae.studio.plugins.text.editor;

import java.awt.Color;
import java.awt.Font;

import javax.swing.UIManager;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Style;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rsyntaxtextarea.Token;

import com.andcreations.ae.studio.plugins.ui.common.UIColors;

/**
 * Configures a syntex text area.
 *
 * @author Mikolaj Gucki
 */
class RSyntexTextAreaCfg {
    /** */
    private RSyntaxTextArea textArea;
    
    /** */
    private SyntaxScheme syntaxScheme;
    
    /** */
    private Font boldFont;
    
    /** */
    RSyntexTextAreaCfg(RSyntaxTextArea textArea) {
        this.textArea = textArea;
        this.syntaxScheme = textArea.getSyntaxScheme();
        getBoldFont();
    }
    
    /** */
    private void getBoldFont() {
        for (Style style:syntaxScheme.getStyles()) {
            if (style.font != null) {
                boldFont = style.font.deriveFont(Font.BOLD);
                return;
            }
        }
    }
    
    /** */
    private void setStyleFgHex(String hexColor,int... indexList) {
        for (int index:indexList) {
            Style style = syntaxScheme.getStyle(index);
            style.foreground = UIColors.fromHex(hexColor);
        }
    }
    
    /** */
    private void setStyleFgHexBold(String hexColor,int... indexList) {
        for (int index:indexList) {
            Style style = syntaxScheme.getStyle(index);
            if (style == null) {
                style = new Style();
            }
            style.foreground = UIColors.fromHex(hexColor);
            style.font = boldFont;
        }
    }
    
    /** */
    private void configureColors() {
        textArea.setBackground(Colors.getBackgroundColor());       
        textArea.setCurrentLineHighlightColor(UIColors.blend(            
            UIManager.getColor("Panel.background"),
            Color.BLACK,0.6));
        textArea.setMarkOccurrencesColor(UIColors.blend(
            UIColors.fromHex(Colors.BLUE),            
            UIManager.getColor("Panel.background"),0.25));
        textArea.setSelectionColor(
            Colors.getBackgroundColor().brighter().brighter());
        textArea.setMarkAllHighlightColor(
            Colors.getBackgroundColor().brighter());
        textArea.setMarginLineColor(UIColors.blend(
            UIColors.fromHex(Colors.GRAY),            
            UIManager.getColor("Panel.background"),0.25));
        textArea.setMatchedBracketBGColor(Colors.getBackgroundColor());
        textArea.setMatchedBracketBorderColor(UIColors.fromHex(Colors.YELLOW));
        
        setStyleFgHex(Colors.PALE_GREEN,
            Token.COMMENT_DOCUMENTATION,
            Token.COMMENT_EOL,            
            Token.COMMENT_MARKUP,
            Token.COMMENT_MULTILINE);
        setStyleFgHex(Colors.GREEN,Token.COMMENT_KEYWORD);
        setStyleFgHex(Colors.RED,Token.VARIABLE);
        setStyleFgHex(Colors.BROWN,Token.OPERATOR);
        setStyleFgHex(Colors.GREEN,            
            Token.LITERAL_BACKQUOTE,
            Token.LITERAL_BOOLEAN,
            Token.LITERAL_CHAR,
            Token.LITERAL_STRING_DOUBLE_QUOTE);
        setStyleFgHex(Colors.RED,            
            Token.LITERAL_NUMBER_DECIMAL_INT,
            Token.LITERAL_NUMBER_FLOAT,
            Token.LITERAL_NUMBER_HEXADECIMAL);
        setStyleFgHexBold(Colors.BLUE,Token.FUNCTION);
        setStyleFgHex(Colors.LIGHT_GRAY,Token.IDENTIFIER);
        setStyleFgHexBold(Colors.LIGHT_GRAY,Token.SEPARATOR);
        setStyleFgHexBold(Colors.YELLOW,   
            Token.RESERVED_WORD,
            Token.RESERVED_WORD_2);
        
    // mark-up
        setStyleFgHex(Colors.LIGHT_GRAY,Token.MARKUP_CDATA);
        setStyleFgHex(Colors.LIGHT_GRAY,Token.MARKUP_CDATA_DELIMITER);
        setStyleFgHex(Colors.PALE_GREEN,Token.MARKUP_COMMENT);
        setStyleFgHex(Colors.LIGHT_GRAY,Token.MARKUP_DTD);
        setStyleFgHex(Colors.LIGHT_GRAY,Token.MARKUP_ENTITY_REFERENCE);
        setStyleFgHex(Colors.LIGHT_GRAY,Token.MARKUP_PROCESSING_INSTRUCTION);
        setStyleFgHex(Colors.BLUE,Token.MARKUP_TAG_ATTRIBUTE);
        setStyleFgHex(Colors.BROWN,Token.MARKUP_TAG_ATTRIBUTE_VALUE);
        setStyleFgHex(Colors.LIGHT_GRAY,Token.MARKUP_TAG_DELIMITER);
        setStyleFgHex(Colors.GREEN,Token.MARKUP_TAG_NAME);
    }
    
    /** */
    void configure() {
        textArea.setClearWhitespaceLinesEnabled(false);
        textArea.setLineWrap(false);
        textArea.setCodeFoldingEnabled(false);
        
    // colors
        configureColors();
        
    // margin
        textArea.setMarginLineEnabled(true);
        textArea.setMarginLinePosition(80);
        
    // occurrences
        textArea.setMarkOccurrences(true);
        textArea.setPaintMarkOccurrencesBorder(false);
        
    // tabs
        textArea.setTabSize(4);
        textArea.setTabsEmulated(true);
    }
}     

