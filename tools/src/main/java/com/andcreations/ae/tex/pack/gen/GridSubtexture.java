package com.andcreations.ae.tex.pack.gen;

import com.andcreations.ae.tex.pack.TexPackException;
import com.andcreations.ae.tex.pack.strategy.AbstractSubtexture;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class GridSubtexture extends AbstractSubtexture {
    /** */
    private BundleResources res = new BundleResources(GridSubtexture.class);
    
    /** */
    private int columns;
    
    /** */
    private int rows;
        
    /** */
    private ImageSubtexture[] images;
    
    /** */
    GridSubtexture(String id,int columns,int rows,ImageSubtexture[] images)
        throws TexPackException {
    //
        super(id);
        this.columns = columns;
        this.rows = rows;
        this.images = images;
        
        create();
    }
    
    /** */
    void create() throws TexPackException {
        verifyAllImagesExist();
        verifySizesFit();
    }
    
    /** */
    boolean contains(ImageSubtexture image) {
        for (ImageSubtexture gridImage:images) {
            if (gridImage == image) {
                return true;
            }
        }
        
        return false;
    }
    
    /** */
    @Override
    public int getWidth() {
        int width = 0;
        for (int x = 0; x < columns; x++) {
            width += getImage(x,0).getWidth();
        }
        
        return width;
    }
    
    /** */
    @Override
    public void setLocation(int x,int y) {
        int imagex = 0;        
        for (int gridx = 0; gridx < columns; gridx++) {
            
            int imagey = 0;
            for (int gridy = 0; gridy < rows; gridy++) {
                ImageSubtexture image = getImage(gridx,gridy);
                image.setLocation(x + imagex,y + imagey);
                
                imagey += getImage(0,gridy).getHeight();
            }
            imagex += getImage(gridx,0).getWidth();
        }
    }
    
    /** */
    @Override
    public int getHeight() {
        int height = 0;
        for (int y = 0; y < rows; y++) {
            height += getImage(0,y).getHeight();
        }
        
        return height;
    }
    
    /** */
    private void verifyAllImagesExist() throws TexPackException {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                if (getImage(x,y) == null) {
                    throw new TexPackException(res.getStr("missing.image",
                        Integer.toString(y)));
                }
            }
        }
    }
    
    /** */
    private void verifySizesFit() throws TexPackException {
    // rows            
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns - 1; x++) {
                ImageSubtexture left = getImage(x,y);
                ImageSubtexture right = getImage(x + 1,y);
                
                if (left.getHeight() != right.getHeight()) {
                    throw new TexPackException(res.getStr("height.mismatch",
                        left.getId(),right.getId()));
                }
            }
        }
        
    // columns
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows - 1; y++) {
                ImageSubtexture upper = getImage(x,y);
                ImageSubtexture lower = getImage(x,y + 1);
                
                if (upper.getWidth() != lower.getWidth()) {
                    throw new TexPackException(res.getStr("width.mismatch",
                        upper.getId(),lower.getId()));
                }
            }
        }
    }
    
    /** */
    private int getIndex(int x,int y) {
        return getIndex(columns,x,y);
    }
    
    /** */
    private ImageSubtexture getImage(int x,int y) {
        return images[getIndex(x,y)];
    }
    
    /** */
    static int getIndex(int columns,int x,int y) {
        return y * columns + x;
    }
    
    /** */
    static void setImage(int columns,ImageSubtexture[] images,
        int x,int y,ImageSubtexture image) {
    //
        images[getIndex(columns,x,y)] = image;
    }
}