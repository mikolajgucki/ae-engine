package com.andcreations.ae.studio.plugins.ui.main;

import java.awt.Frame;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.ui.common.MessageDialog;
import com.andcreations.ae.studio.plugins.ui.common.OptionDialog.Option;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;

/**
 * Utility methods for showing default dialogs.
 * 
 * @author Mikolaj Gucki
 */
public class CommonDialogs {
    /** */
    private static void show(Frame owner,String title,String message,
        ImageIcon icon) {
    //
        MessageDialog.show(owner,title,message,icon,
            new Option[]{Option.OK},Option.OK);        
    }
    
    /** */
    public static void info(Frame owner,String title,String message) {
        show(owner,title,message,Icons.getIcon(DefaultIcons.INFO));
    }
    
    /** */
    public static void info(String title,String message) {
        info(MainFrame.get(),title,message);
    }
    
    /** */
    public static void error(Frame owner,String title,String message) {
        show(owner,title,message,Icons.getIcon(DefaultIcons.ERROR));
    }
    
    /** */
    public static void error(String title,String message) {
        error(MainFrame.get(),title,message);
    }
    
    /** */
    public static void error(String title,Exception exception) {
        error(title,exception.getMessage());
    }
        
    /** */
    public static void warning(Frame owner,String title,String message) {
        show(owner,title,message,Icons.getIcon(DefaultIcons.WARNING));
    }
    
    /** */
    public static void warning(String title,String message) {
        warning(MainFrame.get(),title,message);
    }    
    
    /** */
    public static boolean okCancel(Frame owner,String title,String message) {
        ImageIcon icon = Icons.getIcon(DefaultIcons.QUESTION);
        Option option = MessageDialog.show(owner,title,message,icon,
            new Option[]{Option.OK,Option.CANCEL},Option.CANCEL);
        return option == Option.OK;
    }
    
    /** */
    public static boolean okCancel(String title,String message) {
        return okCancel(MainFrame.get(),title,message);
    }    
    
    /** */
    public static boolean yesNo(Frame owner,String title,String message) {
        ImageIcon icon = Icons.getIcon(DefaultIcons.QUESTION);
        Option option =  MessageDialog.show(owner,title,message,icon,
            new Option[]{Option.YES,Option.NO},Option.NO);
        return option == Option.YES;
    }
    
    /** */
    public static boolean yesNo(String title,String message) {
        return yesNo(MainFrame.get(),title,message);
    }    
    
    /** */
    public static Option yesNoCancel(Frame owner,String title,String message) {
        ImageIcon icon = Icons.getIcon(DefaultIcons.QUESTION);
        return MessageDialog.show(owner,title,message,icon,
            new Option[]{Option.YES,Option.NO,Option.CANCEL},Option.CANCEL);
    }
    
    /** */
    public static Option yesNoCancel(String title,String message) {
        return yesNoCancel(MainFrame.get(),title,message);
    }    
}
