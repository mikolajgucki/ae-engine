package com.andcreations.ae.studio.plugins.ui.common.validation;

//import javax.swing.
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.border.Border;

import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;

/**
 * @author Mikolaj Gucki
 */
public abstract class DefaultVerifier extends InputVerifier {
    /** */
    private String toolTip;
    
    /** */
    private String errorToolTip;
    
    /** */
    private Border border;
    
    /** */
    private IconBorder errorBorder;
    
    /** */
    public DefaultVerifier(JComponent component,String errorToolTip) {
        this.toolTip = component.getToolTipText();
        this.errorToolTip = errorToolTip;        
        this.border = component.getBorder();
        this.errorBorder = new IconBorder(
            border,Icons.getIcon(DefaultIcons.VALIDATION_ERROR));
    }
    
    /** */
    public String getErrorToolTip() {
        return errorToolTip;
    }
    
    /** */
    public abstract boolean verify();
    
    /** */
    @Override
    public boolean verify(JComponent component) {
        boolean ok = verify();
        if (ok == true) {
            component.setToolTipText(toolTip);
            component.setBorder(border);
        }
        else {
            component.setToolTipText(errorToolTip);
            component.setBorder(errorBorder);
        }
        
        return ok;
    }
}