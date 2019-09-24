package com.andcreations.ae.studio.plugins.splash;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JWindow;

import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.ResourceIconSource;

/**
 * @author Mikolaj Gucki
 */
class Splash {
    /** */
    private SplashProgress splashProgress;    
    
    /** */
    private Image image;
    
    /** */
    private JWindow window;
    
    /** */
    Splash() {
        create();
    }
    
    /** */
    private void create() {
    // window
        window = new JWindow();
        window.setIconImage(getIcon().getImage());
        
    // label
        ImageIcon imageIcon = loadImageIcon();
        if (imageIcon == null) {
            return;
        }
        image = imageIcon.getImage();
        
    // progress
        splashProgress = new SplashProgress(image,new SplashProgressListener() {
            /** */
            @Override
            public void splashClicked() {
                window.toFront();
            }
        });
        window.getContentPane().add(splashProgress);
        
    // window size        
        window.setSize(imageIcon.getIconWidth(),imageIcon.getIconHeight());
    }
    
    /** */
    private ImageIcon getIcon() {
        ResourceIconSource source = new ResourceIconSource(
            DefaultIcons.class,"resources/");
        return source.getIcon(DefaultIcons.AE_STUDIO);
    }
    
    /** */
    private ImageIcon loadImageIcon() {
        String resourceName = '/' +
            Splash.class.getPackage().getName().replace('.','/') +
            "/resources/splash.png"; 
        return new ImageIcon(Splash.class.getResource(resourceName));
    }
    
    /** */
    void show() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation(
            screenSize.width / 2 - (image.getWidth(null) / 2),
            screenSize.height / 2 - (image.getHeight(null) / 2));        
        window.setVisible(true);
    }
    
    /** */
    void setProgress(String info,double progress) {
        splashProgress.setProgress(info,progress);
        window.toFront();
    }
    
    /** */
    void hide() {
        window.setVisible(false);
        window.dispose();
    }
}