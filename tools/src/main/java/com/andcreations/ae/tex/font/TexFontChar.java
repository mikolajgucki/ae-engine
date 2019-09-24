package com.andcreations.ae.tex.font;

/**
 * @author Mikolaj Gucki
 */
public class TexFontChar {
    /** The character in unicode. */
    private int character;
    
    /** The identifier of the image with the character. */
    private String image;
    
    /**
     * Constructs a {@link TexFontChar}.
     *
     * @param character The character.
     * @param image The identifier of the image with the character.
     */
    public TexFontChar(int character,String image) {
        this.character = character;
        this.image = image;
    }
    
    /**
     * Gets the character in unicode.
     *
     * @return The character.
     */
    public int getCharacter() {
        return character;
    }    
    
    /**
     * Gets the identifier of the image with the character.
     *
     * @return The image identifier.
     */
    public String getImage() {
        return image;
    }
}