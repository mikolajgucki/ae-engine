package com.andcreations.ae.studio.plugins.ui.common.validation;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class IconBorder extends AbstractBorder {
    /** */
    private Border border;
    
    /** */
    private ImageIcon icon;
    
    /** */
    public IconBorder(Border border,ImageIcon icon) {
        this.border = border;
        this.icon = icon;
    }
    
    /** */
    @Override
    public void paintBorder(Component component,Graphics graphics,
        int x,int y,int width,int height) {
    //
        border.paintBorder(component,graphics,x,y,width,height);
    
    // bottom-right corner
        final int gap = 4;
        int ix = x + width - icon.getIconWidth() - gap;
        int iy = y + height - icon.getIconHeight() - gap;
        
    // draw
        graphics.drawImage(icon.getImage(),ix,iy,icon.getImageObserver());
    }
    
    /** */
    @Override
    public Insets getBorderInsets(Component component) {
        return border.getBorderInsets(component);
    }
    
    /** */
    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}