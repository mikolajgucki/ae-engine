package com.andcreations.ae.tex.pack.gen;

import com.andcreations.ae.image.Image;
import com.andcreations.ae.tex.pack.data.TexImage;
import com.andcreations.ae.tex.pack.data.TexInset;
import com.andcreations.ae.tex.pack.strategy.AbstractSubtexture;

/**
 * @author Mikolaj Gucki
 */
class ImageSubtexture extends AbstractSubtexture implements TextureDrawable {
    /** */
    private Image image;
    
    /** */
    private TexImage texImage;
    
    /** */
    ImageSubtexture(String id,Image image) {
        super(id);
        this.image = image;
    }
    
    /** */
    void setTexImage(TexImage texImage) {
        this.texImage = texImage;
    }
    
    /** */
    TexImage getTexImage() {
        return texImage;
    }
    
    /** */
    TexInset getTexInset() {
        if (texImage != null) {
            return texImage.getInset();
        }        
        return null;
    }
    
    /** */
    @Override
    public int getWidth() {
        return image.getWidth();
    }
    
    /** */
    @Override
    public int getHeight() {
        return image.getHeight();
    }
    
    /** */
    public void draw(Image texture) {
        texture.putImage(image,getX(),getY());
    }
}