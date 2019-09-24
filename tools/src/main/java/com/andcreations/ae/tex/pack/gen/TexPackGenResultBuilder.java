package com.andcreations.ae.tex.pack.gen;

import java.util.List;

import com.andcreations.ae.tex.pack.data.TexImage;
import com.andcreations.ae.tex.pack.data.TexInset;
import com.andcreations.ae.tex.pack.data.TexPack;
import com.andcreations.ae.tex.pack.data.TexPoint;

/**
 * @author Mikolaj Gucki
 */
public class TexPackGenResultBuilder {
    /** */
    private TexPack data;
    
    /** */
    TexPackGenResultBuilder(TexPack data) {
        this.data = data;
    }
    
    /** */
    private TexInset getTexInset(ImageSubtexture image) {
        if (data.getInset() != null) {
            return data.getInset();
        }
        if (image.getTexInset() != null) {
            return image.getTexInset();
        }
        
        return new TexInset(Double.valueOf(0));
    }
    
    /** */
    private void addPoints(ImageSubtexture image,
        TexPackGenSubtexture subtexture) {
    //
        TexImage texImage = image.getTexImage();
        if (texImage == null || texImage.getPoints() == null) {
            return;
        }
        
        for (TexPoint texPoint:texImage.getPoints()) {
            double u = texPoint.getX() / image.getWidth();
            double v = 1 - (texPoint.getY()) / image.getHeight();
            
            subtexture.addPoint(new TexPackGenPoint(texPoint.getId(),
                new TexPackGenUVCoords(u,v)));
        }
    }
    
    /** */
    private TexPackGenSubtexture createSubtexture(int width,int height,
        ImageSubtexture image) {
    //
        TexInset inset = getTexInset(image);
        double x = image.getX() + inset.getLeft();
        double y = image.getY() + inset.getTop();
    
        double u = (double)x / width;
        double v = (double)y / height;
        TexPackGenUVCoords coords = new TexPackGenUVCoords(u,v);
        
        double imageWidth = image.getWidth() - inset.getLeft() -
            inset.getRight();
        double imageHeight = image.getHeight() - inset.getTop() -
            inset.getBottom();
        
        double subtexWidth = imageWidth / width;
        double subtexHeight = imageHeight / height;
        
        double aspect = imageHeight / imageWidth;
        
        TexPackGenSubtexture subtexture = new TexPackGenSubtexture(
            image.getId(),coords,subtexWidth,subtexHeight,aspect);
        addPoints(image,subtexture);

        return subtexture;        
    }
    
    /** */
    TexPackGenResult build(int width,int height,List<ImageSubtexture> images) {
        TexPackGenResult result = new TexPackGenResult(width,height);
        for (ImageSubtexture image:images) {
            result.addSubtexture(createSubtexture(width,height,image));
        }
        
        return result;
    }
}