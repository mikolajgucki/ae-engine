package com.andcreations.ae.launch.image;

import java.io.File;

import com.andcreations.ae.color.Color;

/**
 * @author Mikolaj Gucki
 */
public class LaunchImageTest {
    /** */
    public static void main(String[] args) throws Exception {
        CenterLaunchImageLayout layout = new CenterLaunchImageLayout(
            new Color(0,0,0,255),new File("li/icon.png"),0.2);
        LaunchImage ios = LaunchImage.ios(layout);
        ios.generate(new File("li/out"));
    }
}