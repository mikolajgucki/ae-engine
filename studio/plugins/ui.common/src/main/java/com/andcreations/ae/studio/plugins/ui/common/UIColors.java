package com.andcreations.ae.studio.plugins.ui.common;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.UIManager;

/**
 * The common UI colors.
 *
 * @author Mikolaj Gucki
 */
public class UIColors {
    /** */
    public static final String WARNING = "warning";
    
    /** */
    public static final String ERROR = "error";
    
    /** */
    public static final String DARK = "dark";
    
    /** */
    public static final String GREEN = "green";
    
    /** */
    private static final Map<String,Color> colors = new HashMap<>();
    
    /** */
    private static String htmlDark;
    
    /**
     * Converts a color from a hex.
     *
     * @param hex The hex.
     * @return The color.
     */
    public static Color fromHex(String hex) {
        if (hex.length() != 6) {
            throw new IllegalArgumentException("Invalid hex value");
        }
        
        int r = Integer.parseInt(hex.substring(0,2),16);
        int g = Integer.parseInt(hex.substring(2,4),16);
        int b = Integer.parseInt(hex.substring(4,6),16);
        
        return new Color(r,g,b);
    }
    
    /**
     * Converts a color to its hex representation (rrggbb).
     *
     * @param color The color.
     * @return The color in hex.
     */
    public static String toHex(Color color) {
        return String.format("%02X%02X%02X",color.getRed(),color.getGreen(),
            color.getBlue());
    }
    
    /** */
    public static Color blend(Color a,Color b,double t) {
        return new Color(
            (int)(a.getRed() * t + b.getRed() * (1 - t)),
            (int)(a.getGreen() * t + b.getGreen() * (1 - t)),
            (int)(a.getBlue() * t + b.getBlue() * (1 - t)));
    }
    
    /** */
    private static void put(String name,Color color) {
        colors.put(name,color);
    }
    
    /** */
    static void init() {
        put(WARNING,fromHex("e2a74a"));
        put(ERROR,fromHex("c24f4b"));
        put(DARK,UIColors.blend(
            UIManager.getColor("Panel.background"),
            UIManager.getColor("Label.disabledForeground"),0.5));
        put(GREEN,UIColors.blend(
            UIManager.getColor("Panel.background"),fromHex("c4c200"),0.5));
        htmlDark = toHex(dark());
    }
    
    /** */
    public static Color getColor(String name) {
        return colors.get(name);
    }
    
    /** */
    public static Color warning() {
        return getColor(WARNING);
    }
    
    /** */
    public static Color error() {
        return getColor(ERROR);
    }
    
    /** */
    public static Color dark() {
        return getColor(DARK);
    }
    
    /** */
    public static Color green() {
        return getColor(GREEN);
    }
    
    /** */
    public static String htmlDark() {
        return htmlDark;
    }
}