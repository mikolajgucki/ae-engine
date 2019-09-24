package com.andcreations.ae.studio.plugins.ui.common.quickopen;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

/**
 * @author Mikolaj Gucki
 */
class QuickOpenListCellRenderer implements ListCellRenderer<QuickOpenItem> {
    /** */
    QuickOpenListCellRenderer() {
    }
    
    /** */
    @Override
    public Component getListCellRendererComponent(
        JList<? extends QuickOpenItem> list,QuickOpenItem item,int index,
        boolean selected,boolean cellHasFocus) {
    // panel
        JPanel panel = new JPanel(new BorderLayout());
        
    // label
        JLabel label = new JLabel();
        panel.add(label,BorderLayout.WEST);
        label.setIcon(item.getIcon());
        label.setBorder(BorderFactory.createEmptyBorder(0,0,1,0));        
        
    // selected?
        if (selected == false) {
            label.setText(item.getHTMLValue());
        }        
        else {
            label.setText(item.getValue());
            label.setForeground(UIManager.getColor("List.selectionForeground"));
            panel.setBackground(UIManager.getColor("List.selectionBackground"));
        }
    
        return panel;
    }    
}