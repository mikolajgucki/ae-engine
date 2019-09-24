package com.andcreations.ae.studio.plugins.file.transfer;

import java.awt.Dialog;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.ui.common.MessageDialog;
import com.andcreations.ae.studio.plugins.ui.common.OptionDialog.Option;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;

/**
 * @author Mikolaj Gucki
 */
class OverwriteDialog {
    /** */
    private Dialog owner;
    
    /** */
    private String title;
    
    /** */
    OverwriteDialog(Dialog owner,String title) {
        this.owner = owner;
        this.title = title;
    }
    
    /** */
    Option show(String msg) {
        ImageIcon icon = Icons.getIcon(DefaultIcons.QUESTION);
        Option[] options = new Option[]{
            Option.YES,Option.NO,Option.YES_TO_ALL,Option.CANCEL};
        return MessageDialog.show(owner,title,msg,icon,options,null);
    }
}