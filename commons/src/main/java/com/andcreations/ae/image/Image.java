package com.andcreations.ae.image;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.andcreations.ae.color.Color;
import com.andcreations.ae.math.Interpolation;

/**
 * An image.
 *
 * @author Mikolaj Gucki
 */
public class Image {
    /**
     * The width of the output image.
     */
    private int width;
    
    /**
     * The height of the output image.
     */
    private int height;
    
    /**
     * The output image.
     */
    private BufferedImage image; 
    
    /**
     * Constructs an Image object.
     *
     * @param width The width of the image.
     * @param height The height of the image.
     */
    public Image(int width,int height) {
        this.width = width;
        this.height = height;
        
        create();
    }
    
    /**
     * Constructs an Image object from a buffered image.
     * 
     * @param image The buffered image.
     */
    public Image(BufferedImage image) {
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.image = image;
    }
    
    /**
     * Creates the image.
     */
    private void create() {
        image = new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR);
    }
    
    /**
     * Gets the width of the generator.
     *
     * @return The width.
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Gets the height of the generator.
     *
     * @return The height.
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Gets the tiled X-coordinate.
     * 
     * @param x The X-coordinate.
     * @return The tiled X-coordinate.
     */
    public int getTiledX(int x) {
        while (x < 0) {
            x += width;
        }
        while (x >= width) {
            x -= width;
        }
        
        return x;
    }
    
    /**
     * Gets the tiled Y-coordinate.
     * 
     * @param y The Y-coordinate.
     * @return The tiled Y-coordinate.
     */
    public int getTiledY(int y) {
        while (y < 0) {
            y += height;
        }
        while (y >= height) {
            y -= height;
        }
        
        return y;
    }
    
    /**
     * Indicates if a point is inside the image.
     * 
     * @param x The X-coordinate of the point.
     * @param y The Y-coordinate of the point.
     * @return <tt>true</tt> if the point is inside, <tt>false</tt> otherwise.
     */
    public boolean inside(int x,int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
    
    /**
     * Gets the buffered image for this image.
     * 
     * @return The buffered image.
     */
    public BufferedImage getBufferedImage() {
        return image;
    }
    
    /**
     * Sets a pixel in the image.
     *
     * @param x The X-coordinate of the pixel.
     * @param y The Y-coordinate of the pixel.
     * @param r The red component.
     * @param g The green component.
     * @param b The blue component.
     * @param a The alpha component.     
     */
    public void setRGBA(int x,int y,int r,int g,int b,int a) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return;
        }        
        
        int rgba = (a << 24) + (r << 16) + (g << 8) + b;
        image.setRGB(x,y,rgba);
    }
    
    /**
     * Sets a pixel on the image.
     *
     * @param x The X-coordinate of the pixel.
     * @param y The Y-coordinate of the pixel.
     * @param r The red component.
     * @param g The green component.
     * @param b The blue component. 
     */
    public void setRGB(int x,int y,int r,int g,int b) {
        setRGBA(x,y,r,g,b,255);
    }        
    
    /**
     * Gets the alpha component of a pixel.
     *
     * @param x The X-coordinate of the pixel.
     * @param y The Y-coordinate of the pixel.
     * @return The alpha component.     
     */
    public int getAlpha(int x,int y) {
        int rgba = image.getRGB(x,y);
        return (rgba >> 24) & 0xff;
    }    
    
    /**
     * Gets the red component of a pixel.
     *
     * @param x The X-coordinate of the pixel.
     * @param y The Y-coordinate of the pixel.
     * @return The red component.     
     */
    public int getRed(int x,int y) {
        int rgba = image.getRGB(x,y);
        return (rgba >> 16) & 0xff;
    }
        
    /**
     * Gets the green component of a pixel.
     *
     * @param x The X-coordinate of the pixel.
     * @param y The Y-coordinate of the pixel.
     * @return The green component.     
     */
    public int getGreen(int x,int y) {
        int rgba = image.getRGB(x,y);
        return (rgba >> 8) & 0xff;
    }
        
    /**
     * Gets the blue component of a pixel.
     *
     * @param x The X-coordinate of the pixel.
     * @param y The Y-coordinate of the pixel.
     * @return The blue component.     
     */
    public int getBlue(int x,int y) {
        int rgba = image.getRGB(x,y);
        return rgba & 0xff;
    }    

    /**
     * Gets the color of a pixel.
     *
     * @param x The X-coordinate of the pixel.
     * @param y The Y-coordinate of the pixel.
     * @return The color.   
     */
    public Color getColor(int x,int y) {
        return new Color(getRed(x,y),getGreen(x,y),getBlue(x,y),getAlpha(x,y));
    }
    
    /**
     * Sets the color of a pixel.
     *
     * @param x The X-coordinate of the pixel.
     * @param y The Y-coordinate of the pixel.
     * @param color The color.       
     */
    public void setColor(int x,int y,Color color) {
        setRGBA(x,y,color.r,color.g,color.b,color.a);
    }
    
    /**
     * Gets the color of a subpixel.
     * 
     * @param x The X-coordinate of the subpixel.
     * @param y The Y-coordinate of the subpixel.
     * @param interpolation The subpixel color interpolation method.
     * @return The subpixel color.
     */
    public Color getSubpixel(float x,float y,Interpolation interpolation) {    
        x -= 0.5f;
        y -= 0.5f;
        
        int xi = (int)(Math.floor(x));
        int yi = (int)(Math.floor(y));
        
        float xf = x - xi;
        float yf = y - yi;

        int x0 = xi;
        if (x0 < 0) {
            x0 = 0;
        }
        
        int y0 = yi;
        if (y0 < 0) {
            y0 = 0;
        }
        
        int x1 = xi + 1;
        if (x1 >= width) {
            x1 = width - 1;
        }
        
        int y1 = yi + 1;
        if (y1 >= height) {
            y1 = height - 1;
        }
        
        Color c00 = getColor(x0,y0);
        Color c10 = getColor(x1,y0);
        Color c11 = getColor(x1,y1);
        Color c01 = getColor(x0,y1);
        
        int r0 = (int)(interpolation.interpolate(c00.r,c10.r,xf));
        int g0 = (int)(interpolation.interpolate(c00.g,c10.g,xf));
        int b0 = (int)(interpolation.interpolate(c00.b,c10.b,xf));
        int a0 = (int)(interpolation.interpolate(c00.a,c10.a,xf));
        
        int r1 = (int)(interpolation.interpolate(c01.r,c11.r,xf));
        int g1 = (int)(interpolation.interpolate(c01.g,c11.g,xf));
        int b1 = (int)(interpolation.interpolate(c01.b,c11.b,xf));
        int a1 = (int)(interpolation.interpolate(c01.a,c11.a,xf));
        
        int r = (int)(interpolation.interpolate(r0,r1,yf));
        int g = (int)(interpolation.interpolate(g0,g1,yf));
        int b = (int)(interpolation.interpolate(b0,b1,yf));
        int a = (int)(interpolation.interpolate(a0,a1,yf));
                
        return new Color(r,g,b,a);
    }
    
    /**
     * Fills a rectangle.
     *
     * @param x The X-coordinate of the upper-left corner.
     * @param y The Y-coordinate of the upper-left corner.
     * @param width The rectangle width.
     * @param height The rectangle height.
     * @param color The color;
     */ 
    public void fillRect(int x,int y,int width,int height,Color color) {
        Graphics graphics = image.getGraphics();
        graphics.setColor(color.getAWTColor());
        graphics.fillRect(x,y,width,height);
    }

    /**
     * Puts an image (copies the red, green, blue and alpha components).
     * 
     * @param image The image to put. 
     * @param x The X-coordinate of the upper-left of the image to put.
     * @param y The Y-coordinate of the upper-left of the image to put.  
     */
    public void putImage(Image image,int x,int y) {
        for (int y0 = 0; y0 < image.getHeight(); y0++) {
            for (int x0 = 0; x0 < image.getWidth(); x0++) {
            // if outside the destination image
                if (x + x0 < 0 || x + x0 >= getWidth()) {
                    continue;
                }
                if (y + y0 < 0 || y + y0 >= getHeight()) {
                    continue;
                }
                
                int r = image.getRed(x0,y0);
                int g = image.getGreen(x0,y0);
                int b = image.getBlue(x0,y0);
                int a = image.getAlpha(x0,y0);
                
                setRGBA(x + x0,y + y0,r,g,b,a);
            }
        }
    }    
    
    /**
     * Puts an image (copies the red, green, blue and alpha components).
     * 
     * @param image The image to put. 
     * @param x The X-coordinate of the upper-left of the image to put.
     * @param y The Y-coordinate of the upper-left of the image to put.  
     */
    private void putImage(java.awt.Image awtImage,int x,int y) {
        Graphics graphics = image.getGraphics();
        graphics.drawImage(awtImage,0,0,null);
    }
    
    /**
     * Draws an image (the alpha channel is taken as opacity).
     *
     * @param image The image to draw.
     * @param x The X-coordinate of the upper-left of the image to draw.
     * @param y The Y-coordinate of the upper-left of the image to draw.
     * @param mode The draw mode.
     */
    public void drawImage(Image image,int x,int y,DrawMode mode) {
        for (int y0 = 0; y0 < image.getHeight(); y0++) {
            for (int x0 = 0; x0 < image.getWidth(); x0++) {
            // if outside the destination image
                if (x + x0 < 0 || x + x0 >= getWidth()) {
                    continue;
                }
                if (y + y0 < 0 || y + y0 >= getHeight()) {
                    continue;
                }
                
                int r = getRed(x + x0,y + y0);
                int g = getGreen(x + x0,y + y0);
                int b = getBlue(x + x0,y + y0);
                int a = getAlpha(x + x0,y + y0);
                
                int ri = image.getRed(x0,y0);
                int gi = image.getGreen(x0,y0);
                int bi = image.getBlue(x0,y0);                 
                int ai = image.getAlpha(x0,y0);
                
                Color color = mode.getColor(new Color(r,g,b,a),
                    new Color(ri,gi,bi,ai));
                setRGBA(x + x0,y + y0,color.r,color.g,color.b,color.a);
            }
        }
    }
    
        /**
     * Draws an image (the alpha channel is taken as opacity).
     *
     * @param image The image to draw.
     * @param x The X-coordinate of the upper-left of the image to draw.
     * @param y The Y-coordinate of the upper-left of the image to draw.
     */
    public void drawImage(Image image,int x,int y) {
        drawImage(image,x,y,DrawMode.NORMAL);
    }
    
    /**
     * Scales the image.
     * 
     * @param newWidth The width of the scaled image.
     * @param newHeight The height of the scaled image.
     * @param interpolation The pixel color interpolation method.
     * @return The scaled image.
     */
    public Image scale(int newWidth,int newHeight,Interpolation interpolation) {    
        Image output = new Image(newWidth,newHeight);
        
        for (int y = 0; y < newHeight; y++) {
            float ys = y * height / (float)newHeight;
            
            for (int x = 0; x < newWidth; x++) {
                float xs = x * width / (float)newWidth;
                
                Color color = getSubpixel(xs,ys,interpolation);
                output.setRGBA(x,y,color.r,color.g,color.b,color.a);
            }
        }
        
        return output;
    }    

    /**
     * Scaled the image using the AWT method.
     *
     * @param newWidth The width of the scaled image.
     * @param newHeight The height of the scaled image.
     * @return The scale image.
     */
    public Image scale(int newWidth,int newHeight) {
        java.awt.Image scaledImage = image.getScaledInstance(newWidth,newHeight,
            java.awt.Image.SCALE_SMOOTH);
        
        Image scaled = new Image(newWidth,newHeight);
        scaled.putImage(scaledImage,0,0);        
        return scaled;
    }
    
    /**
     * Creates a subimage of the image.
     * 
     * @param x The X-coordinate of the upper-left corner of the subimage.
     * @param y The Y-coordinate of the upper-left corner of the subimage.
     * @param width The width of the subimage.
     * @param height The height of the subimage.
     * @return The subimage.
     */
    public Image subimage(int x,int y,int width,int height) {
        Image subimage = new Image(width,height);
        
        for (int yi = 0; yi < height; yi++) {
            for (int xi = 0; xi < width; xi++) {
                subimage.setRGBA(xi,yi,
                    getRed(xi + x,yi + y),
                    getGreen(xi + x,yi + y),
                    getBlue(xi + x,yi + y),
                    getAlpha(xi + x,yi + y));
            }
        }
        
        return subimage;
    }
    
    /**
     * Rotates the image by 90 degrees.
     *
     * @return The rotated image.
     */
    public Image rotate90() {
        Image rotated = new Image(getHeight(),getWidth());
        
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                int r = getRed(x,y);
                int g = getGreen(x,y);
                int b = getBlue(x,y);
                int a = getAlpha(x,y);
                
                rotated.setRGBA(getHeight() - y - 1,x,r,g,b,a);
            }
        }
        
        return rotated;
    }
    
    /**
     * Saves the output image to a PNG file.
     *
     * @param file The file to which to write.
     * @throws IOException on I/O error.
     */
    public void savePNG(File file) throws IOException {
        ImageIO.write(image,"png",file);                    
    }  
    
    /**
     * Saves the output image to a PNG file.
     *
     * @param filename The name of the file.
     * @throws IOException on I/O error.
     */
    public void savePNG(String filename) throws IOException {
        savePNG(new File(filename));                    
    }  
    
    /**
     * Loads an image from a file.
     * 
     * @param file The file from which to load the image.
     * @return The loaded image.
     * @throws IOException on I/O error.
     */    
    public static Image load(File file) throws IOException {
        BufferedImage image = ImageIO.read(file);
        if (image == null) {
            throw new IOException("Failed to load image");
        }
        return new Image(image);
    }
    
    /**
     * Loads an image from a file.
     * 
     * @param filename The name of the file which to load the image.
     * @return The loaded image.
     * @throws IOException on I/O error.
     */
    public static Image load(String filename) throws IOException {
        return load(new File(filename));
    }
}