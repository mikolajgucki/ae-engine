package com.andcreations.ae.studio.plugins.text.editor.autocomplete;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class TECompletionCellRenderer extends DefaultListCellRenderer {
    /** */
    @Override
    public Component getListCellRendererComponent(JList<?> list,Object value,
        int index,boolean isSelected,boolean cellHasFocus) {
    //
        JLabel label = (JLabel)super.getListCellRendererComponent(
            list,value,index,isSelected,cellHasFocus);
        Autocompl autocompl = (Autocompl)value;
        
    // text
        if (isSelected == false) {
            label.setText(autocompl.getDisplayHTML());
        }
        else {
            label.setText(autocompl.getDisplayText());
        }
        
    // icon 
        label.setIcon(autocompl.getIcon());
        
        return label;
    }    
}