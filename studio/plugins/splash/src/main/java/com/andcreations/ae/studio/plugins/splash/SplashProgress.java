package com.andcreations.ae.studio.plugins.splash;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class SplashProgress extends JComponent {
    /** */
    private String info;
    
    /** */
    private double progress;
    
    /** */
    private Image image;
    
    /** */
    private SplashProgressListener listener;
    
    /** */
    SplashProgress(Image image,SplashProgressListener listener) {
        this.image = image;
        this.listener = listener;
        create();
    }
    
    /** */
    private void create() {
        addMouseListener(new MouseAdapter() {
            /** */
            @Override
            public void mouseClicked(MouseEvent event) {
                listener.splashClicked();
            }
        });
    }
    
    /** */
    void setProgress(String info,double progress) {
        this.info = info;
        this.progress = progress;
        repaint();
    }
    
    /** */
    private void drawInfo(Graphics graphics) {
        final int margin = 6;
        final int height = 6;
        
        graphics.setColor(new Color(128,128,128));
        graphics.drawString(info,margin,getHeight() - margin - height * 2);
    }
    
    /** */
    private void drawProgress(Graphics graphics) {
        final int margin = 6;
        final int height = 6;

        graphics.setColor(new Color(114,179,235));
        graphics.fillRect(margin,getHeight() - margin - height,
            (int)((getWidth() - margin * 2) * progress),height);
    }
    
    /** */
    @Override
    public void paint(Graphics graphics) {
        graphics.drawImage(image,0,0,getWidth(),getHeight(),null);
        drawInfo(graphics);
        drawProgress(graphics);
    }
}