package com.andcreations.ae.launch.image;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import com.andcreations.ae.color.Color;
import com.andcreations.ae.image.Image;

/**
 * The layout with draws an image in the center of the launch image.
 *
 * @author Mikolaj Gucki
 */
public class CenterLaunchImageLayout implements LaunchImageLayout {
    /** */
    private Color color;
    
    /** */
    private Image image;
    
    /** */
    private double factor;
    
    /** */
    public CenterLaunchImageLayout(Color color,Image image,double factor) {
        this.color = color;
        this.image = image;
        this.factor = factor;
    }
    
    /** */
    public CenterLaunchImageLayout(Color color,File imageFile,double factor)
        throws IOException {
    //
        this(color,Image.load(imageFile),factor);
    }
    
    /** */
    private Dimension getSize(LaunchImageEntry entry) {
    // portrait
        if (entry.getOrientation() == LaunchImageOrientation.PORTRAIT) {
            int width = (int)(entry.getWidth() * factor);
            double ratio = (double)width / image.getWidth();
            int height = (int)(image.getHeight() * ratio);
            
            return new Dimension(width,height);
        }
        
    // landscape
        if (entry.getOrientation() == LaunchImageOrientation.LANDSCAPE) {
            int height = (int)(entry.getHeight() * factor);
            double ratio = (double)height / image.getHeight();
            int width = (int)(image.getWidth() * ratio);
            
            return new Dimension(width,height);
        }
    
        throw new IllegalArgumentException("Unknown orientation " +
            entry.getOrientation());
    }
    
    /** */
    @Override
    public Image layout(LaunchImageEntry entry) {
        Image launchImage = new Image(entry.getWidth(),entry.getHeight());
        
    // background color
        launchImage.fillRect(0,0,entry.getWidth(),entry.getHeight(),color);
        
    // scale
        Dimension size = getSize(entry);
        Image scaledImage = image.scale(size.width,size.height);    
        
    // draw in the center
        int x = (launchImage.getWidth() - scaledImage.getWidth()) / 2;
        int y = (launchImage.getHeight() - scaledImage.getHeight()) / 2;
        launchImage.drawImage(scaledImage,x,y);
        
        return launchImage;
    }
}