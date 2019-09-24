package com.andcreations.ae.studio.plugins.ui.common;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;

import com.andcreations.resources.BundleResources;

/**
 * An option dialog.
 * 
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class OptionDialog extends Dialog {
    /** */
    public static enum Option {
        /** */
        OK,
        
        /** */
        CANCEL,
        
        /** */
        YES,
        
        /** */
        NO,
        
        /** */
        YES_TO_ALL;
    }
    
    /** */
    public static class OptionHolder {
        /** */
        private Option option;
        
        /** */
        public void setOption(Option option) {
            this.option = option;
        }
        
        /** */
        public Option getOption() {
            return option;
        }
    }
    
    /** */
    private final BundleResources res = new BundleResources(OptionDialog.class);      
    
    /** */
    private Map<JButton,Option> buttonMap = new HashMap<>();
    
    /**
     * Constructs an {@link OptionDialog}.
     * 
     * @param owner The dialog owner.
     * @param title The title.
     * @param content The content.
     * @param options The options to display at the bottom.
     * @param modal The modal indicator.
     */
    public OptionDialog(Frame owner,String title,JComponent content,
        Option[] options,boolean modal) {
    //
        super(owner,title,modal);
        create(content,options);
    }
    
    /**
     * Constructs an {@link OptionDialog}.
     * 
     * @param owner The dialog owner.
     * @param title The title.
     * @param modal The modal indicator.
     */    
    public OptionDialog(Frame owner,String title,boolean modal) {
        super(owner,title,modal);    
    }
        
    /**
     * Constructs an {@link OptionDialog}.
     * 
     * @param owner The dialog owner.
     * @param title The title.
     * @param content The content.
     * @param options The options to display at the bottom.
     * @param modal The modal indicator.
     */
    public OptionDialog(java.awt.Dialog owner,String title,JComponent content,
        Option[] options,boolean modal) {
    //
        super(owner,title,modal);
        create(content,options);
    }
    
    /**
     * Constructs an {@link OptionDialog}.
     * 
     * @param owner The dialog owner.
     * @param title The title.
     * @param modal The modal indicator.
     */    
    public OptionDialog(java.awt.Dialog owner,String title,boolean modal) {
        super(owner,title,modal);    
    }    
    
    /**
     * Constructs an {@link OptionDialog}.
     * 
     * @param owner The dialog owner.
     * @param modal The modal indicator.
     */    
    public OptionDialog(Frame owner,boolean modal) {
        super(owner,modal);    
    }    
    
    /**
     * Constructs an {@link OptionDialog}.
     * 
     * @param owner The dialog owner.
     * @param modal The modal indicator.
     */    
    public OptionDialog(java.awt.Dialog owner,boolean modal) {
        super(owner,modal);    
    }     
    
    /** */
    protected void create(JComponent content,Option[] options) {
    // buttons
        List<JButton> buttons = new ArrayList<>();
        for (Option option:options) {
            String label = res.getStr(option.name().toLowerCase());
            JButton button = new JButton(label);
            buttonMap.put(button,option);
            buttons.add(button);
        }
        
        create(content,buttons.toArray(new JButton[]{}));
    }
    
    /** */
    protected JButton getButton(Option option) {
        for (Map.Entry<JButton,Option> entry:buttonMap.entrySet()) {
            if (entry.getValue() == option) {
                return entry.getKey();
            }
        }
        
        return null;
    }
    
    /** */
    public void setDefaultOption(Option option) {
        for (Map.Entry<JButton,Option> entry:buttonMap.entrySet()) {
            if (entry.getValue() == option) {
                JButton button = entry.getKey();
                getRootPane().setDefaultButton(button);
                button.requestFocus();
            }
        }
    }
    
    /** */
    public Option showOptionDialog() {
        JButton button = showDialog();
        return buttonMap.get(button);
    }
    
    /** */
    protected void close(Option option) {
        for (Map.Entry<JButton,Option> entry:buttonMap.entrySet()) {
            if (entry.getValue() == option) {
                close(entry.getKey());
                return;
            }
        }
    }
}
