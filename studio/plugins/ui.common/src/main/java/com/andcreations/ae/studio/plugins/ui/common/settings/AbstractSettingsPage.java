package com.andcreations.ae.studio.plugins.ui.common.settings;

import javax.swing.JComponent;

/**
 * @author Mikolaj Gucki
 */
public abstract class AbstractSettingsPage implements SettingsPage {
    /** */
    private String title;
    
    /** */
    private String iconName;
    
    /** */
    private JComponent component;
    
    /** */
    private SettingsContext context;
    
    /** */
    protected void setTitle(String title) {
        this.title = title;
    }
    
    /** */
    protected void setIconName(String iconName) {
        this.iconName = iconName;
    }
    
    /** */
    protected void setComponent(JComponent component) {
        this.component = component;
    }
    
    /** */
    @Override
    public String getTitle() {
        return title;
    }
    
    /** */
    @Override
    public String getIconName() {
        return iconName;
    }        
    
    /** */
    @Override
    public JComponent getComponent() {
        return component;
    }
    
    /** */
    @Override
    public void setSettingsContext(SettingsContext context) {
        this.context = context;
    }
    
    /** */
    protected SettingsContext getSettingsContext() {
        return context;
    }
    
    /** */
    protected void setPageError() {
        getSettingsContext().setPageError(this);
    }
    
    /** */
    protected void clearPageIssues() {
        getSettingsContext().clearPageIssues(this);
    }
}