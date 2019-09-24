package com.andcreations.ae.tex.pack.gen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.image.Image;
import com.andcreations.ae.tex.pack.TexPackException;
import com.andcreations.ae.tex.pack.data.TexGrid;
import com.andcreations.ae.tex.pack.data.TexImage;
import com.andcreations.ae.tex.pack.data.TexPack;
import com.andcreations.ae.tex.pack.strategy.AbstractSubtexture;
import com.andcreations.ae.tex.pack.strategy.MaxRectsTexPackStrategy;
import com.andcreations.ae.tex.pack.strategy.Subtexture;
import com.andcreations.ae.tex.pack.strategy.SubtexturesDontFitException;
import com.andcreations.ae.tex.pack.strategy.TexPackStrategy;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class TexPackGen {
    /** */
    private static final int MIN_SIZE = 128;
    
    /** */
    private static final int MAX_SIZE = 1024;
    
    /** */
    private BundleResources res = new BundleResources(TexPackGen.class);
    
    /** */
    private TexPackGenCfg cfg;
    
    /** */
    private TexPackStrategy strategy = new MaxRectsTexPackStrategy();
    
    /** */
    private int idSequence;
    
    /** */
    private int width;
    
    /** */
    private int height;
    
    /** */
    private List<ImageSubtexture> images;
    
    /** */
    private List<GridSubtexture> grids;
    
    /** */
    private Image texture;
    
    /** */
    private TexPackGenResult result;
    
    /** */
    public TexPackGen(TexPackGenCfg cfg) {
        this.cfg = cfg;
    }
    
    /** */
    private String nextId() {
        idSequence++;
        return "_subtex" + idSequence;
    }
    
    /** */
    private void resetSize() {
        width = MIN_SIZE;
        height = MIN_SIZE;
    }
    
    /** */
    private boolean nextSize() {
        if (width == MAX_SIZE && height == MAX_SIZE) {
            return false;
        }
        
        if (width == MAX_SIZE) {
            width = MIN_SIZE;
            height *= 2;
        }
        else {
            width *= 2;
        }
        
        return true;
    }
    
    /** */
    public static String getImageId(File file) {
        String id = file.getName();
        
        int indexOf = id.lastIndexOf('.');
        if (indexOf > 0) {
            id = id.substring(0,indexOf);
        }
        
        return id;
    }
    
    /** */
    private boolean containsImage(String id) {
        for (ImageSubtexture image:images) {
            if (id.equals(image.getId())) {
                return true;
            }
        }
        
        return false;
    }
    
    /** */
    private void loadImages(File dir) throws IOException,TexPackException {
        TexPack data = cfg.getTexPackerCfg().getData();
        
        File[] files = dir.listFiles();
    // for each file
        for (File file:files) {
            String id = getImageId(file);
            if (containsImage(id) == true) {
                throw new TexPackException(res.getStr("duplicated",id));
            }
            
        // load
            Image image;
            try {
                image = Image.load(file);
            } catch (IOException exception) {
                throw new TexPackException(res.getStr("failed.to.load.image",
                    file.getAbsolutePath()));
            }
            ImageSubtexture subtexture = new ImageSubtexture(id,image);
            
        // extra info
            TexImage texImage = data.findTexImage(id);
            subtexture.setTexImage(texImage);
            
        // add
            images.add(subtexture);
        }
    }
    
    /** */
    private void loadImages() throws IOException,TexPackException {
        images = new ArrayList<>();
        
    // for each directory
        for (File dir:cfg.getDirs()) {
            loadImages(dir);
        }
    }
    
    /** */
    private ImageSubtexture getImage(String id)
        throws TexPackException {
    //
        for (ImageSubtexture image:images) {
            if (image.getId().equals(id) == true) {
                return image;
            }
        }
        
        throw new TexPackException(res.getStr("unknown.image",id));
    }
    
    /** */
    private void createGridSubtexture(TexGrid texGrid) throws TexPackException {
        int width = texGrid.getWidth();
        int height = texGrid.getHeight();
        ImageSubtexture[] images = new ImageSubtexture[width * height];
            
        for (int y = 0; y < height; y++) {
            String[] values = texGrid.getRow(y);
            for (int x = 0; x < width; x++) {
                ImageSubtexture image = getImage(values[x]);
                GridSubtexture.setImage(width,images,x,y,image);
            }
        }
        grids.add(new GridSubtexture(nextId(),width,height,images));
    }
    
    /** */
    private void createGrids() throws TexPackException {
        grids = new ArrayList<>();
        
        TexPack data = cfg.getTexPackerCfg().getData();
        if (data.getGrids() != null) {
            for (TexGrid grid:data.getGrids()) {
                createGridSubtexture(grid);
            }
        }
    }
    
    /** */
    private void pack(List<AbstractSubtexture> subtextures)
        throws TexPackException {
    //
        List<Subtexture> spacingSubtextures = new ArrayList<>();
        for (AbstractSubtexture subtexture:subtextures) {
            spacingSubtextures.add(new SpacingSubtexture(nextId(),
                subtexture,cfg.getTexPackerCfg().getData().getSpacing()));
        }
        
        resetSize();
        while (true) {
            boolean fit = true;
            try {
                strategy.pack(width,height,spacingSubtextures);
            } catch (SubtexturesDontFitException exception) {
                fit = false;
            }
            if (fit) {
                break;
            }
            if (nextSize() == false) {
                throw new TexPackException(res.getStr("images.dont.fit"));
            }
        }
    }
    
    /** */
    private void draw(int width,int height,List<ImageSubtexture> images) {
        texture = new Image(width,height);
        for (ImageSubtexture image:images) {
            image.draw(texture);
        }
    }
    
    /** */
    private boolean isReferenced(ImageSubtexture image) {
        for (GridSubtexture grid:grids) {
            if (grid.contains(image) == true) {
                return true;
            }
        }
        
        return false;
    }
    
    /** */
    public List<File> listImageFiles() {
        List<File> imageFiles = new ArrayList<>();
        
    // for each directory
        for (File dir:cfg.getDirs()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file:files) {
                    imageFiles.add(file);
                }
            }
        }        
        
        return imageFiles;
    }
    
    /** */
    private void createResult(int width,int height,
        List<ImageSubtexture> images) {
    //
        TexPackGenResultBuilder builder = new TexPackGenResultBuilder(
            cfg.getTexPackerCfg().getData());
        result = builder.build(width,height,images);
    }
    
    /** */
    public void run() throws IOException,TexPackException {
    // create subtextures
        loadImages();
        if (images.isEmpty() == true) {
            //throw new TexPackException(res.getStr("no.images"));
            return;
        }
        createGrids();
        
    // get subtextures to pack    
        List<AbstractSubtexture> subtextures = new ArrayList<>();
        for (ImageSubtexture image:images) {
            if (isReferenced(image) == false) {
                subtextures.add(image);
            }
        }
        subtextures.addAll(grids);
        
    // pack, draw
        pack(subtextures);
        draw(width,height,images);
        
    // result
        createResult(width,height,images);
    }
    
    /** */
    public Image getTextureImage() {
        return texture;
    }
    
    /** */
    public TexPackGenResult getResultData() {
        return result;
    }    
}