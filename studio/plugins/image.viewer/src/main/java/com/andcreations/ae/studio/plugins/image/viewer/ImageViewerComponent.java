package com.andcreations.ae.studio.plugins.image.viewer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.andcreations.ae.image.Image;
import com.andcreations.ae.studio.plugins.ui.common.UIColors;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class ImageViewerComponent extends JPanel {
    /** */
    private final BundleResources res =
        new BundleResources(ImageViewerComponent.class); 
    
    /** */
    private double scale = 1;
    
    /** */
    private Image image;
        
    /** */
    private JLabel imageLabel;
    
    /** */
    private JScrollPane imageScroll;
    
    /** */
    private JLabel errorLabel;
    
    /** */
    private JPanel topPanel;
    
    /** */
    private JLabel infoLabel;
    
    /** */
    private JSlider scaleSlider;
    
    /** */
    private JButton oneToOne;
    
    /** */
    ImageViewerComponent() {
        create();
    }
    
    /** */
    private void create() {
        setLayout(new BorderLayout());
        
    // image
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageScroll = new JScrollPane(imageLabel);
        
    // error
        errorLabel = new JLabel(Icons.getIcon(DefaultIcons.ERROR));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setForeground(UIColors.error());
        
    // top panel
        topPanel = new JPanel();
        FlowLayout topPanelLayout = new FlowLayout();
        topPanelLayout.setAlignment(FlowLayout.LEFT);
        topPanel.setLayout(topPanelLayout);
        
    // info label
        infoLabel = new JLabel();
        topPanel.add(infoLabel);
        
    // slider
        scaleSlider = new JSlider(10,200); // in percent
        scaleSlider.addChangeListener(new ChangeListener() {
            /** */
            @Override
            public void stateChanged(ChangeEvent event) {
                scale = scaleSlider.getValue() / 100d;
                showImage();
            }
        });
        topPanel.add(scaleSlider);
        
    // 1:1
        oneToOne = new JButton(res.getStr("one.to.one"));
        oneToOne.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                scale = 1;
                scaleSlider.setValue((int)(scale * 100));
                showImage();
            }
        });
        topPanel.add(oneToOne);
    }
    
    /** */
    private void showError(String msg,File file) {
        remove(topPanel);
        remove(imageScroll);
        add(errorLabel,BorderLayout.CENTER);
        errorLabel.setText(res.getStr("failed.to.load.image",
            msg,file.getAbsolutePath()));
    }
    
    /** */
    private void showImage() {
        String widthStr = Integer.toString(image.getWidth());
        String heightStr = Integer.toString(image.getHeight());
        String scaleStr = String.format("%.2f",scale);
    // info
        infoLabel.setText(res.getStr("size",widthStr,heightStr,scaleStr));
        
        int width = image.getWidth();
        int height = image.getHeight();
    // scale
        java.awt.Image imageToShow = image.getBufferedImage();
        int newWidth = (int)(width * scale);
        int newHeight = (int)(height * scale);
        if (newWidth != width || newHeight != height) {
            imageToShow = imageToShow.getScaledInstance(newWidth,newHeight,
                BufferedImage.SCALE_SMOOTH);
        }
        
    // show
        imageLabel.setIcon(new ImageIcon(imageToShow));
        remove(errorLabel);
        add(topPanel,BorderLayout.NORTH);
        add(imageScroll,BorderLayout.CENTER);
    }
    
    /** */
    void showImage(File file) {
    // load
        try {
            image = Image.load(file);
        } catch (IOException exception) {
            showError(exception.getMessage(),file);
            return;
        }
        
    // show
        showImage();
    }
}