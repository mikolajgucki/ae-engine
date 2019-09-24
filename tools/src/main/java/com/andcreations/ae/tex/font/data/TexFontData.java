package com.andcreations.ae.tex.font.data;


/**
 * @author Mikolaj Gucki
 */
public class TexFontData {
    /** The path to the font file. */
    private String fontFile;
    
    /** The font size. */
    private float size;

    /** The space between the characters in the output image. */
    private TexFontSpacing spacing;
    
    /** The padding of each character. */
    private TexFontPadding padding;
    
    /** The characters to put in the texture. */
    private String characters;
    
    /** */
    public String getFontFile() {
        return fontFile;
    }
    
    /** */
    public float getSize() {
        return size;
    }
    
    /** */
    public TexFontSpacing getSpacing() {
        return spacing;
    }
    
    /** */
    public TexFontPadding getPadding() {
        return padding;
    }
    
    /** */
    public String getCharacters() {
        return characters;
    }
}