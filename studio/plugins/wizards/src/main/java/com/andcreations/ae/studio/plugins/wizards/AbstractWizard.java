package com.andcreations.ae.studio.plugins.wizards;

import javax.swing.ImageIcon;

/**
 * @author Mikolaj Gucki
 */
public abstract class AbstractWizard implements Wizard {
    /** */
    private String name;
    
    /** */
    private ImageIcon icon;
    
    /** */
    private String desc;
    
    /** */
    protected AbstractWizard() {
    }
    
    /** */
    protected AbstractWizard(String name,ImageIcon icon,String desc) {
        this.name = name;
        this.icon = icon;
        this.desc = desc;
    }
    
    /** */
    protected void setName(String name) {
        this.name = name;
    }
    
    /** */
    @Override
    public String getName() {
        return name;
    }
    
    /** */
    protected void setIcon(ImageIcon icon) {
        this.icon = icon;
    }
    
    /** */
    @Override
    public ImageIcon getIcon() {
        return icon;
    }
    
    /** */
    protected void setDesc(String desc) {
        this.desc = desc;
    }
    
    /** */
    @Override
    public String getDesc() {
        return desc;
    }
}