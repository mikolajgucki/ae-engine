package com.andcreations.ae.studio.plugins.ui.common.tree;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class LabelTreeCellRenderer extends DefaultTreeCellRenderer {
    /** */
    @Override
    public Component getTreeCellRendererComponent(JTree tree,Object value,
            boolean selected,boolean expanded,boolean leaf,int row,
            boolean hasFocus) {
    // node
        LabelTreeNode node = (LabelTreeNode)value;
           
    // label
        JLabel label = (JLabel)super.getTreeCellRendererComponent(
            tree,value,selected,expanded,leaf,row,hasFocus);
        if (node.getIcon() != null) {
            label.setIcon(node.getIcon());
        }
        label.setBorder(BorderFactory.createEmptyBorder(0,0,1,0));
        label.setToolTipText(node.getToolTip());
        
    // selected?
        if (selected == false) {
            label.setText(node.getHTMLValue());
        }        
        else {
            label.setText(node.getValue());
            label.setForeground(UIManager.getColor("Tree.selectionForeground"));
            label.setBackground(UIManager.getColor("Tree.selectionBackground"));
        }
        
        return label;
    }
}
