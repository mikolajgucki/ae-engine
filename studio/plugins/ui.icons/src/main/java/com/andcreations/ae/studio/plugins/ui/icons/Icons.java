package com.andcreations.ae.studio.plugins.ui.icons;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import com.andcreations.ae.color.Color;
import com.andcreations.ae.image.Image;

/**
 * Provides icons.
 * 
 * @author Mikolaj Gucki
 */
public class Icons {
    /** */
    public static enum DecoLocation {
        /** */
        TOP_LEFT("top_left!") {
            /** */
            Point getLocation(Dimension size,Dimension decoSize) {
                return new Point(0,0);
            }
        },
        
        /** */
        TOP_RIGHT("top_right!") {
            /** */
            Point getLocation(Dimension size,Dimension decoSize) {
                return new Point(size.width - decoSize.width - 1,0);
            }
        },
        
        /** */
        BOTTOM_LEFT("bottom_left!") {
            /** */
            Point getLocation(Dimension size,Dimension decoSize) {
                return new Point(0,size.height - decoSize.height - 1);
            }
        },
        
        /** */
        BOTTOM_RIGHT("bottom_right!") {
            /** */
            Point getLocation(Dimension size,Dimension decoSize) {
                return new Point(size.width - decoSize.width - 1,
                    size.height - decoSize.height - 1);
            }
        },
        
        /** */
        CENTER("center!") {
            /** */
            Point getLocation(Dimension size,Dimension decoSize) {
                return new Point(
                    (size.width - decoSize.width) / 2,
                    (size.height - decoSize.height) / 2);
            }
        };
        
        /** */
        private String prefix;
        
        /** */
        private DecoLocation(String prefix) {
            this.prefix = prefix;
        }
        
        /** */
        String getPrefix() {
            return prefix;
        }
        
        /** */
        abstract Point getLocation(Dimension size,Dimension decoSize);
    }
    
    /** All the known icon sources. */
    private static List<IconSource> iconSources = new ArrayList<>();
    
    /** The icon cache. */
    private static Map<String,ImageIcon> cache = new HashMap<>();
    
    /**
     * Adds an icon source.
     * 
     * @param iconSource The icon source.
     */    
    public static void addIconSource(IconSource iconSource) {
        iconSources.add(iconSource);
    }
    
    /**
     * Gets an icon.
     * 
     * @param name The icon name.
     * @return The icon or <code>null</code> if there is no such icon.
     */
    public static ImageIcon getIcon(String name) {
        if (cache.containsKey(name) == true) {
            return cache.get(name);
        }
        
        for (IconSource iconSource:iconSources) {
            if (iconSource.hasIcon(name)) {
                ImageIcon icon = iconSource.getIcon(name);
                cache.put(name,icon);
                return icon;
            }
        }
        
        throw new RuntimeException("Icon '" + name + "' not found");
    }

    /**
     * Gets an icon name.
     *
     * @param names The names of the icons.
     */
    public static String getIconName(String... names) {
        String flat = "";
        for (String name:names) {
            flat += name + ">";
        }
        
        return flat;
    }
    
    /**
     * Gets a decorated icon.
     *
     * @param names The names of the icons.
     */
    public static ImageIcon getIcon(String... names) {
        String flatName = getIconName(names);
        if (cache.containsKey(flatName) == true) {
            return cache.get(flatName);
        }
        
    // base icon
        ImageIcon base = getIcon(names[0]);
        
    // output image
        BufferedImage image = new BufferedImage(base.getIconWidth(),
            base.getIconHeight(),BufferedImage.TYPE_4BYTE_ABGR);
        Graphics graphics = image.getGraphics();
        
    // base image
        graphics.drawImage(base.getImage(),0,0,
            image.getWidth(),image.getHeight(),null);
        
    // for each decorator
        for (int index = 1; index < names.length; index++) {
            if (names[index] == null) {
                continue;
            }
            
            ImageIcon deco = getIcon(names[index]);
            graphics.drawImage(deco.getImage(),0,0,
                image.getWidth(),image.getHeight(),null);
        }
        
        ImageIcon icon = new ImageIcon(image);
        cache.put(flatName,icon);
        
        return icon;
    }
    
    /**
     * Gets a decorated icon.
     *
     * @param names The names of the icon.
     */    
    public static ImageIcon getIcon(List<String> names) {
        return getIcon(names.toArray(new String[]{}));
    }
    
    /**
     * Colorizes an icon and puts it in the cache.
     *
     * @param name The icon name.
     * @param iconColor The icon color.
     */
    public static void colorize(String name,Color iconColor) {
        ImageIcon base = getIcon(name);
        int width = base.getIconWidth();
        int height = base.getIconHeight();       
        
    // output image
        Image image = new Image(width,height);
        image.getBufferedImage().getGraphics().drawImage(
            base.getImage(),0,0,width,height,null);
        
    // for each pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = image.getColor(x,y).avg();
                color.mul(iconColor);
                
                int a = image.getAlpha(x,y);
                image.setRGBA(x,y,color.r,color.g,color.b,a);
            }
        }
        
    // result icon
        ImageIcon icon = new ImageIcon(image.getBufferedImage());
        cache.put(name,icon);
    }    
    
    /**
     * Scales down an icon and puts in the cache.
     *
     * @param name The icon name.
     */
    public static void shrink(String name) {
    // get icon size
        ImageIcon base = getIcon(name);
        Dimension size = new Dimension(
            base.getIconWidth(),base.getIconHeight());
        
    // scaled size
        Dimension decoSize = new Dimension(size.width * 3 / 4,
            size.height * 3 / 4);
        
    // scale (by decoration)
        ImageIcon icon = getDecoIcon(DecoLocation.CENTER,decoSize,
            new String[]{name});
        
    // put in the cache
        cache.put(name,icon);
    }
    
    /**
     * Gets the name of a disabled icon.
     *
     * @param names The names of the icons.
     * @return The disable icon name.
     */
    public static String getDisabledIconName(String... names) {
        return "disabled!" + getIconName(names);
    }    
    
    /**
     * Gets a disabled icon.
     *
     * @param names The names of the icons.   
     * @return The disabled icon.
     */
    public static ImageIcon getDisabledIcon(String... names) {
        String flatName = getDisabledIconName(names);
        if (cache.containsKey(flatName) == true) {
            return cache.get(flatName);
        }        
        
    // background color
        java.awt.Color uiBgColor = UIManager.getColor("Panel.background");
        Color bgColor = new Color(
            uiBgColor.getRed(),uiBgColor.getGreen(),uiBgColor.getBlue());
        
        ImageIcon base = getIcon(names);
    // size
        int width = base.getIconWidth();
        int height = base.getIconHeight();        
        
    // output image
        Image image = new Image(width,height);
        image.getBufferedImage().getGraphics().drawImage(
            base.getImage(),0,0,width,height,null);
        
    // for each pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = Color.blend(
                    image.getColor(x,y).avg(),bgColor,0.6);
                
                int a = image.getAlpha(x,y);
                image.setRGBA(x,y,color.r,color.g,color.b,a);
            }
        }        
        
    // result icon
        ImageIcon icon = new ImageIcon(image.getBufferedImage());
        cache.put(flatName,icon);
        
        return icon;
    }   
    
    /** */
    private static Dimension getDefaultDecoSize(Dimension size) {
        return new Dimension(size.width / 2,size.height / 2);
    }    
    
    /** */
    private static String flattenDecoName(DecoLocation decoLocation,
        String... names) {
    //
        return decoLocation.getPrefix() + getIconName(names);
    }
    
    /** */
    public static String getDecoIconName(DecoLocation decoLocation,
        String... names) {
    //
        getDecoIcon(decoLocation,names);
        return flattenDecoName(decoLocation,names);
    } 
    
    /** */
    public static String getDecoIconName(String... names) {
        return getDecoIconName(DecoLocation.BOTTOM_RIGHT,names);
    }
    
    /** */
    public static ImageIcon getDecoIcon(DecoLocation decoLocation,
        String... names) {
    // get icon size
        ImageIcon base = getIcon(names);
        Dimension size = new Dimension(
            base.getIconWidth(),base.getIconHeight());
        
    // decorate
        return getDecoIcon(decoLocation,getDefaultDecoSize(size),names);
    }    
    
    /** */
    private static ImageIcon getDecoIcon(DecoLocation decoLocation,
        Dimension decoSize,String... names) {
    //
        String flatName = flattenDecoName(decoLocation,names);
        if (cache.containsKey(flatName) == true) {
            return cache.get(flatName);
        }        
        
    // base icon
        ImageIcon base = getIcon(names);
        int width = base.getIconWidth();
        int height = base.getIconHeight();
        Image baseImage = new Image(width,height);
        baseImage.getBufferedImage().getGraphics().drawImage(
            base.getImage(),0,0,width,height,null);    
        
    // size, location
        Dimension size = new Dimension(
            base.getIconWidth(),base.getIconHeight());
        Point location = decoLocation.getLocation(size,decoSize);
        
    // scale, draw
        Image baseScaled = baseImage.scale(decoSize.width,decoSize.height);
        Image image = new Image(width,height);
        image.drawImage(baseScaled,location.x,location.y);
        
    // result icon
        ImageIcon icon = new ImageIcon(image.getBufferedImage());
        cache.put(flatName,icon);
        
        return icon;        
    }
    
    /** */
    public static ImageIcon getDecoIcon(String... names) {
        return getDecoIcon(DecoLocation.BOTTOM_RIGHT,names);
    }
    
    /** */
    public static Color red() {
        return Color.fromHex("c14832");
    }
    
    /** */
    public static Color vividRed() {
        return Color.fromHex("db5239");
    }
    
    /** */
    public static Color green() {
        return Color.fromHex("8d9813");
    }
    
    /** */
    public static Color darkGreen() {
        return Color.fromHex("5f660d");
    }
    
    /** */
    public static Color blue() {
        return Color.fromHex("72b3eb");
    }
    
    /** */
    public static Color orange() {
        return Color.fromHex("e2a74a");
    }
    
    /** */
    public static Color violet() {
        return Color.fromHex("7978a2");
    }
    
    /** */
    public static Color gray() {
        return mixWithPanelBackground(Color.fromHex("a0a0a0"));
    }
    
    /** */
    public static Color lightGray() {
        return mixWithPanelBackground(Color.fromHex("e0e0e0"));
    }
    
    /** */
    public static Color darkGray() {
        return mixWithPanelBackground(Color.fromHex("808080"));
    }
    
    /** */
    private static Color mixWithPanelBackground(Color color) {
        java.awt.Color uiBgColor = UIManager.getColor("Panel.background");
        Color bgColor = new Color(
            uiBgColor.getRed(),uiBgColor.getGreen(),uiBgColor.getBlue());                
        return Color.blend(color,bgColor,0.6);        
    }
}
