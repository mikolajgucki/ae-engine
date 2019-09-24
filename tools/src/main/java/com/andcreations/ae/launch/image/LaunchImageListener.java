package com.andcreations.ae.launch.image;

import java.io.File;

/**
 * @author Mikolaj Gucki
 */
public interface LaunchImageListener {
    /** */
    void generatingImage(File file,LaunchImageEntry entry);
}