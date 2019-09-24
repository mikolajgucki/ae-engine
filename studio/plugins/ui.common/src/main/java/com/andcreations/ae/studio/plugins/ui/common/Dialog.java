package com.andcreations.ae.studio.plugins.ui.common;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class Dialog extends JDialog {
    /** The dialog owner. */
    private Window owner;
    
    /** */
    private JButton selectedButton;
    
    /**
     * Constructs a {@link Dialog}.
     * 
     * @param owner The dialog owner.
     * @param title The title.
     * @param content The content.
     * @param buttons The buttons to display at the bottom.
     * @param modal The modal indicator.
     */
    public Dialog(Frame owner,String title,JComponent content,
        JButton[] buttons,boolean modal) {
    //
        super(owner,modal);
        this.owner = owner;
        
        setTitle(title);        
        setIconImage(Icons.getIcon(DefaultIcons.AE_STUDIO).getImage());
        create(content,buttons);
    }
    
    /**
     * Constructs a {@link Dialog} with no content.
     * Use {@link #create(JComponent, JButton[])} to create the dialog.
     * 
     * @param owner The dialog owner.
     * @param title The title.
     * @param modal The modal indicator.
     */
    protected Dialog(Frame owner,String title,boolean modal) {
        super(owner,modal);
        setTitle(title);
        setIconImage(Icons.getIcon(DefaultIcons.AE_STUDIO).getImage());
    }    
    
    /**
     * Constructs a {@link Dialog}.
     * 
     * @param owner The dialog owner.
     * @param title The title.
     * @param content The content.
     * @param buttons The buttons to display at the bottom.
     * @param modal The modal indicator.
     */
    public Dialog(java.awt.Dialog owner,String title,JComponent content,
        JButton[] buttons,boolean modal) {
    //
        super(owner,modal);
        this.owner = owner;
        
        setTitle(title);        
        setIconImage(Icons.getIcon(DefaultIcons.AE_STUDIO).getImage());
        create(content,buttons);
    }
    
    /**
     * Constructs a {@link Dialog} with no content.
     * Use {@link #create(JComponent, JButton[])} to create the dialog.
     * 
     * @param owner The dialog owner.
     * @param title The title.
     * @param modal The modal indicator.
     */
    protected Dialog(java.awt.Dialog owner,String title,boolean modal) {
        super(owner,modal);
        setTitle(title);
        setIconImage(Icons.getIcon(DefaultIcons.AE_STUDIO).getImage());
    }    
    
    /**
     * Constructs a {@link Dialog} with no content.
     * Use {@link #create(JComponent, JButton[])} to create the dialog.
     * 
     * @param owner The dialog owner.
     * @param modal The modal indicator.
     */
    protected Dialog(Frame owner,boolean modal) {
        super(owner,modal);
    }
    
    /**
     * Constructs a {@link Dialog} with no content.
     * Use {@link #create(JComponent, JButton[])} to create the dialog.
     * 
     * @param owner The dialog owner.
     * @param modal The modal indicator.
     */
    protected Dialog(java.awt.Dialog owner,boolean modal) {
        super(owner,modal);
    }
        
    /** */
    protected void create(JComponent content,JButton... buttons) {
        int size = getRelativeSize();
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(size,size,size,size));
        getContentPane().add(panel);
        
    // content
        panel.add(content,BorderLayout.CENTER);
        
    // buttons
        JPanel buttonPanel = ButtonPanel.createButtonPanel(buttons);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(size,0,0,0));
        panel.add(buttonPanel,BorderLayout.SOUTH);
        
        for (JButton button:buttons) {
            button.addActionListener(new ActionListener() {
                /** */
                @Override
                public void actionPerformed(ActionEvent event) {
                    selectedButton = (JButton)event.getSource();
                    dispose();
                }                
            });
        }
        
        setMinimumSize(new Dimension(size * 24,size));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
    
    /** */
    protected int getRelativeSize() {
        int size = 12; // default with no font available
        if (getFont() != null) {
            size = getFont().getSize();
        }       
        
        return size;
    }
    
    /** */
    public void setRelativeMinimumSize(int width,int height) {
        int size = getRelativeSize();
        setMinimumSize(new Dimension(size * width,size * height));
    }
    
    /** */
    public void setRelativePreferredSize(int width,int height) {
        int size = getRelativeSize();
        setPreferredSize(new Dimension(size * width,size * height));
    }
    
    /** */
    protected void makeEscapable() {
        JRootPane rootPane = getRootPane();
        
        KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0);
        final String escapeKey = "OnEsc";
        
        InputMap inputMap = rootPane.getInputMap(
            JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(keyStroke,escapeKey);

        ActionMap actionMap = rootPane.getActionMap();
        actionMap.put(escapeKey, new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                selectedButton = null;
                dispose();
            }
        });
    }
    
    /**
     * Centers a dialog in an owner window.
     *
     * @param owner The owner window.
     * @param dialog The dialog.
     */
    public static void center(Window owner,JDialog dialog) {
    // center inside the owner
        if (owner != null) {
            dialog.setLocation(
                owner.getX() + (owner.getWidth() - dialog.getWidth()) / 2,
                owner.getY() + (owner.getHeight() - dialog.getHeight()) / 2);
            
            return;
        }
        
    // center in the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation(
            (screenSize.width - dialog.getWidth()) / 2,
            (screenSize.height - dialog.getHeight()) / 2);
    }
    
    /** */
    public JButton showDialog() {
        pack();
        center(owner,this);
        setVisible(true);
        
        return selectedButton;
    }
    
    /** */
    protected void close(JButton button) {
        this.selectedButton = button;
        dispose();
    }
}
