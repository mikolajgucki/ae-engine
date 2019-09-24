package com.andcreations.ae.launch.image;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;

import com.andcreations.ae.color.Color;
import com.andcreations.ant.AETask;
import com.andcreations.ant.AntPath;
import com.andcreations.ant.AntSingleValue;

/**
 * @author Mikolaj Gucki
 */
public class CenterLaunchImageAntTask extends AETask {
    /** The source image file. */
    private AntPath image;   
    
    /** The background color. */
    private AntSingleValue bgColor;
    
    /** The size factor. */
    private AntSingleValue size;
    
    /** The destination directory. */
    private AntPath dstDir;   
    
    /** */
    public AntPath createImage() {
        if (image != null) {
            duplicatedElement("image");
        }
        
        image = new AntPath();
        return image;
    }
    
    /** */
    public AntSingleValue createBgColor() {
        if (bgColor != null) {
            duplicatedElement("bgcolor");
        }
        
        bgColor = new AntSingleValue();
        return bgColor;
    }
    
    /** */
    public AntSingleValue createSize() {
        if (size != null) {
            duplicatedElement("size");
        }
        
        size = new AntSingleValue();
        return size;
    }
    
    
    /** */
    public AntPath createDstDir() {
        if (dstDir != null) {
            duplicatedElement("dstdir");
        }
        
        dstDir = new AntPath();
        return dstDir;
    }
    
    /** */
    @Override
    public void execute() {
        verifyElementSet(image,"image");
        verifyElementSet(bgColor,"bgcolor");
        verifyElementSet(size,"size");
        verifyElementSet(dstDir,"dstdir");
        
    // source image
        File imageFile = new File(image.getPath());
        verbose("Image file is " + imageFile.getAbsolutePath());
        
    // destination directory
        File dstDirFile = new File(dstDir.getPath());
        verbose("Destination directory is " + dstDirFile.getAbsolutePath());
     
    // background color
        Color color = null;
        try {
            color = Color.fromHex(bgColor.getValue());
        } catch (IllegalArgumentException exception) {
            throw new BuildException("Invalid color " + bgColor.getValue());
        }
        
    // size (factory)
        double factor;
        try {
            factor = Double.parseDouble(size.getValue());
            if (factor <= 0 || factor > 1) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException exception) {
            throw new BuildException("Invalid size " + size.getValue());
        }
        
    // listener
        LaunchImageListener listener = new LaunchImageListener() {
            /** */
            @Override
            public void generatingImage(File file,LaunchImageEntry entry) {
                log(String.format("Generating launch image %s (%dx%d)",
                    file.getAbsolutePath(),entry.getWidth(),entry.getHeight()));
            }
        };
        
    // generate
        try {
            CenterLaunchImageLayout layout = new CenterLaunchImageLayout(
                color,imageFile,factor);
            LaunchImage ios = LaunchImage.ios(layout);
            ios.addLaunchImageListener(listener);
            ios.generate(dstDirFile);
        } catch (IOException exception) {
            throw new BuildException(exception.getMessage(),exception);
        }
    }
}