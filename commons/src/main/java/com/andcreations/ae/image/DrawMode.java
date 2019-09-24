package com.andcreations.ae.image;

import com.andcreations.ae.color.Color;

/**
 * @author Mikolaj Gucki
 * http://www.java-gaming.org/index.php?topic=24529.0
 */
public interface DrawMode {
    /** */
    public static DrawMode NORMAL = new DrawMode() {
        /** */
        @Override
        public Color getColor(Color lowerColor,Color upperColor) {
            int pixel[] = {lowerColor.toRGBA()};
            Compositor.blendPixel_SrcOver(
                pixel,0,upperColor.toRGBA(),upperColor.a);
            return new Color(pixel[0]);
        }
    };
    
    /**
     * Gets the result color from drawing a pixel on an underlying pixel.
     *
     * @param lowerColor The color of the lower pixel.
     * @param upperColor The color of the upper pixel.
     * @return The result color.
     */
    Color getColor(Color lowerColor,Color upperColor);
}