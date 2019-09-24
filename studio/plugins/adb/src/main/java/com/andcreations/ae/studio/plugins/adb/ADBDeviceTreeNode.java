package com.andcreations.ae.studio.plugins.adb;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeNode;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class ADBDeviceTreeNode extends LabelTreeNode {
    /** */
    ADBDeviceTreeNode(ImageIcon icon,String value,String htmlValue) {
        super(icon,value,htmlValue);
    }
}