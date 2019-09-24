package com.andcreations.ae.tex.font.gen;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.andcreations.ae.tex.font.data.TexFontPadding;
import com.andcreations.ae.tex.font.data.TexFontSpacing;

/**
 * @author Mikolaj Gucki
 */
public class TexFontGen {
    /** The default texture width. */
    private static final int DEFAULT_TEXTURE_WIDTH = 1024;
    
    /** The default texture height. */
    private static final int DEFAULT_TEXTURE_HEIGHT = 1024;
    
    /** The configuration. */
    private TexFontGenCfg cfg;
    
    /** The font. */
    private Font font;
    
    /** The font metrics. */
    private FontMetrics metrics;
    
    /** The font image. */
    private BufferedImage image;
    
    /** The font image graphics. */
    private Graphics2D graphics;
    
    /** The mask image. */
    private BufferedImage maskImage;
    
    /** The mask image graphics. */
    private Graphics2D maskGraphics;

    /** The result data. */
    private TexFontGenResult result;
    
    /**
     * Constructs a {@link TexFontGen}.
     *
     * @param cfg The generator configuration.
     */
    public TexFontGen(TexFontGenCfg cfg) {
        this.cfg = cfg;
        this.font = cfg.getFont().deriveFont(Font.PLAIN,cfg.getSize());
    }
    
    /**
     * Gets the font metrics.
     */
    private void getFontMetrics() {
        BufferedImage image = new BufferedImage(
            1,1,BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = image.createGraphics();
        metrics = graphics.getFontMetrics(font);  
    }
    
    /**
     * Gets the width of a character.
     *
     * @param ch The character.
     * @return The character width.
     */
    private int getCharacterWidth(char ch) {
        String chStr = new String(new char[]{ch});
        return metrics.stringWidth(chStr);         
    }
    
    /**
     * Creates the texture image.
     *
     * @param width The image width.
     * @param height The image height.
     */
    private void createImage(int width,int height) {
        image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        
    // graphics
        graphics = image.createGraphics();
        graphics.setFont(font);
        graphics.setColor(Color.WHITE);
        
    // anti-aliasing
        graphics.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);        
    }
    
    /**
     * Creates the mask image.
     *
     * @param width The image width.
     * @param height The image height.     
     */
    private void createMask(int width,int height) {
        maskImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        maskGraphics = maskImage.createGraphics();
    }
    
    /**    
     * Runs the generator.
     */
    public void run() {
        getFontMetrics();
        createImage(DEFAULT_TEXTURE_WIDTH,DEFAULT_TEXTURE_HEIGHT);
        createMask(DEFAULT_TEXTURE_WIDTH,DEFAULT_TEXTURE_HEIGHT);
        
    // result
        result = new TexFontGenResult();
        
    // spacing
        TexFontSpacing spacing = cfg.getSpacing();
        if (spacing == null) {
            spacing = new TexFontSpacing();
        }
        
    // padding
        TexFontPadding padding = cfg.getPadding();
        if (padding == null) {
            padding = new TexFontPadding();
        }
        
    // location
        int x = spacing.getHorizontal();
        int y = spacing.getVertical();
        
    // heights
        int characterHeight = metrics.getAscent() + metrics.getDescent() +
            padding.getTop() + padding.getBottom();
        int cellHeight = characterHeight + spacing.getVertical();
        
    // for each character
        for (int index = 0; index < cfg.getCharacters().length(); index++) {
            char ch = cfg.getCharacters().charAt(index);
            
        // widths
            int characterWidth = getCharacterWidth(ch) + 
                padding.getLeft() + padding.getRight();
            int cellWidth = characterWidth + spacing.getHorizontal();
            
        // go to next line?
            if (x + cellWidth >= image.getWidth()) {
                x = spacing.getHorizontal();
                y += cellHeight;
                
                if (y + cellHeight > image.getHeight()) {
                    // TODO The image is too small for the characters.
                }
            }
            
        // draw
            String chStr = new String(new char[]{ch});
            graphics.drawString(chStr,
                x + padding.getLeft(),
                y + padding.getTop() + metrics.getAscent());
            
        // draw (mask)
            maskGraphics.setColor(new Color(0x80779dbe,true));
            maskGraphics.fillRect(x,y,characterWidth,characterHeight);
            
        // character image
            String subtexture = String.format("%s%d",cfg.getId(),(int)ch);
            result.addChar(new TexFontGenChar(
                ch,subtexture,x,y,characterWidth,characterHeight));
            
            x += cellWidth;
        }
    }
    
    /**
     * Gets the raw image.
     *
     * @return The raw image.
     */
    public BufferedImage getRawImage() {
        return image;
    }
    
    /**
     * Gets the mask image.
     *
     * @return The mask image.
     */
    public BufferedImage getMaskImage() {
        return maskImage;
    }
    
    /**
     * Gets the reuslt data.
     *
     * @return The result data.
     */
    public TexFontGenResult getResultData() {
        return result;
    }
}