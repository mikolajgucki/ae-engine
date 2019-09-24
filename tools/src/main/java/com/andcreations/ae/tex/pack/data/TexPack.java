package com.andcreations.ae.tex.pack.data;

import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class TexPack {
    /** */
    private String group;
    
    /** */
    private TexSpacing spacing;
    
    /** */
    private TexInset inset;
    
    /** */
    private List<TexPath> dirs;
    
    /** */
    private List<TexGrid> grids;
    
    /** */
    private List<TexImage> images;
    
    /** */
    public void setGroup(String group) {
        this.group = group;
    }
    
    /** */
    public String getGroup() {
        return group;
    }
    
    /** */
    public void setSpacing(TexSpacing spacing) {
        this.spacing = spacing;
    }
    
    /** */
    public TexSpacing getSpacing() {
        return spacing;
    }
            
    /** */
    public void setInset(TexInset inset) {
        this.inset = inset;
    }
    
    /** */
    public TexInset getInset() {
        return inset;
    }    
    
    /** */
    public void setDirs(List<TexPath> dirs) {
        this.dirs = dirs;
    }
    
    /** */
    public List<TexPath> getDirs() {
        return dirs;
    }
    
    /** */
    public void setGrids(List<TexGrid> grids) {
        this.grids = grids;
    }
    
    /** */
    public List<TexGrid> getGrids() {
        return grids;
    }
    
    /** */
    public void setImages(List<TexImage> images) {
        this.images = images;
    }
    
    /** */
    public List<TexImage> getImages() {
        return images;
    }
    
    /** */
    public TexImage findTexImage(String id) {
        if (images == null) {
            return null;
        }

        for (TexImage image:images) {
            if (image.getId().equals(id) == true) {
                return image;
            }
        }
        
        return null;
    }
}