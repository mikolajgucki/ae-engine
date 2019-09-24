package com.andcreations.ae.tex.font.gen;

import java.awt.Font;

import com.andcreations.ae.tex.font.data.TexFontPadding;
import com.andcreations.ae.tex.font.data.TexFontSpacing;


/**
 * @author Mikolaj Gucki
 */
public class TexFontGenCfg {
    /** */
    private String id;    
    
    /** */
    private Font font;
    
    /** */
    private float size;
    
    /** */
    private TexFontSpacing spacing;
    
    /** */
    private TexFontPadding padding;
    
    /** */
    private String characters;
    
    /** */
    public TexFontGenCfg(String id,Font font,float size,String characters) {
        this.id = id;
        this.font = font;
        this.size = size;
        this.characters = characters;
    }
    
    /** */
    String getId() {
        return id;
    }    
    
    /** */
    Font getFont() {
        return font;
    }
    
    /** */
    float getSize() {
        return size;
    }
    
    /** */
    String getCharacters() {
        return characters;
    }
    
    /** */
    public void setSpacing(TexFontSpacing spacing) {
        this.spacing = spacing;
    }
    
    /** */
    TexFontSpacing getSpacing() {
        return spacing;
    }
    
    /** */
    public void setPadding(TexFontPadding padding) {
        this.padding = padding;
    }
    
    /** */
    TexFontPadding getPadding() {
        return padding;
    }
}