package com.andcreations.ae.studio.plugins.file;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;

/**
 * @author Mikolaj Gucki
 */
public enum FileIssueSeverity {
    /** */
    ERROR(DefaultIcons.ERROR),
    
    /** */
    WARNING(DefaultIcons.WARNING);
    
    /** */
    private String iconName;
    
    /** */
    private ImageIcon icon;    
    
    /** */
    private FileIssueSeverity(String iconName) {
        this.iconName = iconName;
        this.icon = Icons.getIcon(iconName);
    }
    
    /** */
    public String getIconName() {
        return iconName;
    }
        
    /** */
    public ImageIcon getIcon() {
        return icon;
    }    
}