package com.andcreations.ae.studio.plugins.problems;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.resources.BundleResources;

/**
 * Problem severity.
 *
 * @author Mikolaj Gucki
 */
public enum ProblemSeverity {
    /** */
    ERROR("error",Icons.getIcon(DefaultIcons.ERROR)),    
    
    /** */
    WARNING("warning",Icons.getIcon(DefaultIcons.WARNING));

    /** */
    private String label;
    
    /** */
    private ImageIcon icon;
    
    /** */
    private ProblemSeverity(String key,ImageIcon icon) {
        this.label = new BundleResources(ProblemSeverity.class).getStr(key);
        this.icon = icon;
    }
    
    /** */
    public String getLabel() {
        return label;
    }
    
    /** */
    public ImageIcon getIcon() {
        return icon;
    }
}