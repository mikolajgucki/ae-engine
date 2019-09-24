package com.andcreations.ae.studio.logo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.andcreations.ae.image.Image;

/**
 * @author Mikolaj Gucki
 */
public class GearGen {
    /** */
    private static void drawLine(Graphics graphics,
        int x0,int y0,int x1,int y1) {
    //
        graphics.drawLine(x0,y0,x1,y1);
    }
    
    /** */
    private static Image gen(int imageSize,int cogs) {
        Image image = new Image(imageSize,imageSize);
        Graphics2D graphics = image.getBufferedImage().createGraphics();
        graphics.setColor(new Color(0,0,0,255));
        /*graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);*/
         
        double angle = Math.PI * 2 / cogs;
        int c = imageSize / 2;
        
        double r1 = imageSize * 0.45;
        double r2 = r1 * 0.8;
        
        double t0 = 0;
        double t1 = 0.5;
        double t2 = 0.57;
        double t3 = 0.92;
        double t4 = 1;
        
        for (int cog = 0; cog < cogs; cog++) {
            double x0 = Math.cos(angle * cog + angle * t0) * r2 + c;
            double y0 = Math.sin(angle * cog + angle * t0) * r2 + c;
            
            double x1 = Math.cos(angle * cog + angle * t1) * r2 + c;
            double y1 = Math.sin(angle * cog + angle * t1) * r2 + c;
            
            double x2 = Math.cos(angle * cog + angle * t2) * r1 + c;
            double y2 = Math.sin(angle * cog + angle * t2) * r1 + c;
            
            double x3 = Math.cos(angle * cog + angle * t3) * r1 + c;
            double y3 = Math.sin(angle * cog + angle * t3) * r1 + c;
            
            double x4 = Math.cos(angle * cog + angle * t4) * r2 + c;
            double y4 = Math.sin(angle * cog + angle * t4) * r2 + c;
            
            drawLine(graphics,(int)x0,(int)y0,(int)x1,(int)y1);
            drawLine(graphics,(int)x1,(int)y1,(int)x2,(int)y2);
            drawLine(graphics,(int)x2,(int)y2,(int)x3,(int)y3);
            drawLine(graphics,(int)x3,(int)y3,(int)x4,(int)y4);
        }
        
        return image;
    }
    
    /** */
    public static void main(String[] args) throws Exception {
        Image image = gen(1024,7);
        image.savePNG("gear.png");
    }
}