package com.andcreations.ae.plugin.ui.studio.plugin.layout;

/**
 * @author Mikolaj Gucki
 */
public class LayoutSize {
    /** */
    private String module;
    
    /** */
    private boolean useSimulatorSize;
    
    /** */
    private int width;

    /** */
    private int height;

    /** */
    LayoutSize(String module) {
        this.module = module;
    }
    
    /** */
    public void setModule(String module) {
        this.module = module;
    }
    
    /** */
    public String getModule() {
        return module;
    }
    
    /** */
    public boolean getUseSimulatorSize() {
        return useSimulatorSize;
    }
    
    /** */
    public void setUseSimulatorSize(boolean useSimulatorSize) {
        this.useSimulatorSize = useSimulatorSize;
    }
    
    /** */
    public int getWidth() {
        return width;
    }
    
    /** */
    public void setWidth(int width) {
        this.width = width;
    }
    
    /** */
    public int getHeight() {
        return height;
    }
    
    /** */
    public void setHeight(int height) {
        this.height = height;
    }
}