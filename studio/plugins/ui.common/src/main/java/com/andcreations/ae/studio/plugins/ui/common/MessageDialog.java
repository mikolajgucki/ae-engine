package com.andcreations.ae.studio.plugins.ui.common;

import java.awt.Frame;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.andcreations.ae.studio.plugins.ui.common.OptionDialog.Option;
import com.andcreations.ae.studio.plugins.ui.common.OptionDialog.OptionHolder;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;

/**
 * Shows message dialogs.
 * 
 * @author Mikolaj Gucki
 */
public class MessageDialog {
    /**
     * Shows a message modal dialog.
     *  
     * @param owner The dialog owner.
     * @param title The title.
     * @param message The message.
     * @param icon The icon.
     * @param options The options.
     * @param defaultOption The default option.
     * @return The selected option.
     */
    public static Option show(final Frame owner,final String title,
        final String message,final ImageIcon icon,final Option[] options,
        final Option defaultOption) {
    //
        final OptionHolder holder = new OptionHolder();
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {        
                JLabel label = new JLabel(message,icon,SwingConstants.LEFT);        
                OptionDialog dialog = new OptionDialog(
                    owner,title,label,options,true);
                dialog.makeEscapable();
                if (defaultOption != null) {
                    dialog.setDefaultOption(defaultOption);
                }
                holder.setOption(dialog.showOptionDialog());
            }
        });
        
        return holder.getOption();
    }
    
    /**
     * Shows a message modal dialog.
     *  
     * @param owner The dialog owner.
     * @param title The title.
     * @param message The message.
     * @param icon The icon.
     * @param options The options.
     * @param defaultOption The default option.
     * @return The selected option.
     */
    public static Option show(final java.awt.Dialog owner,final String title,
        final String message,final ImageIcon icon,final Option[] options,
        final Option defaultOption) {
    //
        final OptionHolder holder = new OptionHolder();
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {      
                JLabel label = new JLabel(message,icon,SwingConstants.LEFT);        
                OptionDialog dialog = new OptionDialog(
                    owner,title,label,options,true);
                dialog.makeEscapable();                
                if (defaultOption != null) {
                    dialog.setDefaultOption(defaultOption);
                }
                holder.setOption(dialog.showOptionDialog());
            }
        });
        
        return holder.getOption();
    }    
    
    /**
     * Shows a message modal dialog.
     *  
     * @param owner The dialog owner.
     * @param title The title.
     * @param message The message.
     * @param icon The icon.
     */
    public static void show(Frame owner,String title,String message,
        ImageIcon icon) {
    //
        show(owner,title,message,icon,new Option[]{Option.OK},Option.OK);
    } 
    
    /**
     * Shows a message modal dialog.
     *  
     * @param owner The dialog owner.
     * @param title The title.
     * @param message The message.
     * @param icon The icon.
     */
    public static void show(java.awt.Dialog owner,String title,String message,
        ImageIcon icon) {
    //
        show(owner,title,message,icon,new Option[]{Option.OK},Option.OK);
    }    
    
    /** */
    public static void info(Frame owner,String title,String message) {
        show(owner,title,message,Icons.getIcon(DefaultIcons.INFO));
    }
    
    /** */
    public static void info(java.awt.Dialog owner,String title,String message) {
        show(owner,title,message,Icons.getIcon(DefaultIcons.INFO));
    }
    
    /** */
    public static void error(Frame owner,String title,String message) {
        show(owner,title,message,Icons.getIcon(DefaultIcons.ERROR));
    }
           
    /** */
    public static void error(java.awt.Dialog owner,String title,
        String message) {
    //
        show(owner,title,message,Icons.getIcon(DefaultIcons.ERROR));
    }    
    
    /** */
    public static void warning(Frame owner,String title,String message) {
        show(owner,title,message,Icons.getIcon(DefaultIcons.WARNING));
    }    
    
    /** */
    public static void warning(java.awt.Dialog owner,String title,
        String message) {
    //
        show(owner,title,message,Icons.getIcon(DefaultIcons.WARNING));
    }    
    
    /** */
    public static Option okCancel(Frame owner,String title,String message) {
        ImageIcon icon = Icons.getIcon(DefaultIcons.QUESTION);
        return MessageDialog.show(owner,title,message,icon,
            new Option[]{Option.OK,Option.CANCEL},Option.CANCEL);
    }    
    
    /** */
    public static Option okCancel(java.awt.Dialog owner,String title,
        String message) {
    //
        ImageIcon icon = Icons.getIcon(DefaultIcons.QUESTION);
        return MessageDialog.show(owner,title,message,icon,
            new Option[]{Option.OK,Option.CANCEL},Option.CANCEL);
    }  
    
    /** */
    public static Option yesNo(Frame owner,String title,String message) {
        ImageIcon icon = Icons.getIcon(DefaultIcons.QUESTION);
        return MessageDialog.show(owner,title,message,icon,
            new Option[]{Option.YES,Option.NO},Option.NO);
    }
        
    /** */
    public static Option yesNo(java.awt.Dialog owner,String title,
        String message) {
    //
        ImageIcon icon = Icons.getIcon(DefaultIcons.QUESTION);
        return MessageDialog.show(owner,title,message,icon,
            new Option[]{Option.YES,Option.NO},Option.NO);
    }    
    
    /** */
    public static Option yesNoCancel(Frame owner,String title,String message) {
        ImageIcon icon = Icons.getIcon(DefaultIcons.QUESTION);
        return MessageDialog.show(owner,title,message,icon,
            new Option[]{Option.YES,Option.NO,Option.CANCEL},Option.CANCEL);
    }
    
    /** */
    public static Option yesNoCancel(java.awt.Dialog owner,String title,
        String message) {
    //
        ImageIcon icon = Icons.getIcon(DefaultIcons.QUESTION);
        return MessageDialog.show(owner,title,message,icon,
            new Option[]{Option.YES,Option.NO,Option.CANCEL},Option.CANCEL);
    }
}
