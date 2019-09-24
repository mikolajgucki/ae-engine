package com.andcreations.ae.studio.plugins.builders;

import javax.swing.ImageIcon;

/**
 * @author Mikolaj Gucki
 */
public abstract class AbstractBuilder implements Builder {
    /** */
    private String id;
    
    /** */
    private String name;
    
    /** */
    private ImageIcon icon;
    
    /** */
    private String desc;
    
    /** */
    protected AbstractBuilder(String id,String name,ImageIcon icon,
        String desc) {
    //
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.desc = desc;
    }
    
    /** */
    @Override
    public String getId() {
        return id;
    }
    
    /** */
    @Override
    public String getName() {
        return name;
    }
    
    /** */
    @Override
    public ImageIcon getIcon() {
        return icon;
    }
    
    /** */
    @Override
    public String getDesc() {
        return desc;
    }
            
    /** */
    @Override
    public boolean hasWarnings() {
        return false;
    }
    
    /** */
    @Override
    public boolean hasErrors() {
        return false;
    }
    
    /** */
    @Override
    public void terminate() {
    }
    
    /** */
    protected void setTerminable(boolean terminable) {
        Builders.get().setTerminable(terminable);
    }
    
    /** */
    protected void runBuilders(String ...builderIds) {
        for (String id:builderIds) {
            Builder builder = Builders.get().getBuilderById(id);
            if (builder == null) {
                throw new UnknownBuilderException(id);
            }
            builder.build();
        }
    }    
}