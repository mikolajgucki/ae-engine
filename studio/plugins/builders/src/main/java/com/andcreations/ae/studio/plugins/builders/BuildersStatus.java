package com.andcreations.ae.studio.plugins.builders;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.andcreations.ae.studio.plugins.ui.common.IconButton;
import com.andcreations.ae.studio.plugins.ui.common.blinker.Blinker;
import com.andcreations.ae.studio.plugins.ui.common.blinker.BlinkerTask;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class BuildersStatus extends JPanel {
    /** */
    private BundleResources res = new BundleResources(BuildersStatus.class);
    
    /** */
    private BuildersStatusListener listener;
    
    /** */
    private IconButton terminateButton;
    
    /** */
    private JLabel status;
    
    /** */
    private ImageIcon icon;
    
    /** */
    private boolean blinking = false;
    
    /** */
    BuildersStatus(BuildersStatusListener listener) {
        this.listener = listener;
        create();
    }
    
    /** */
    private void create() {
        setLayout(new BorderLayout());
        
    // terminate
        terminateButton = new IconButton(
            Icons.getIcon(DefaultIcons.KILL),
            Icons.getIcon(DefaultIcons.KILL_HIGHLIGHT));
        terminateButton.setToolTipText(res.getStr("terminate"));
        terminateButton.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                listener.terminate();
            }
        });
        add(terminateButton,BorderLayout.EAST);
        
    // status
        status = new JLabel();
        add(status,BorderLayout.WEST);
        
    // blinker
        Blinker.get().addBlinkerTask(new BlinkerTask() {
            /** */
            @Override
            public void on() {
                status.setIcon(icon);
            }
            
            /** */
            @Override
            public void off() {
                if (blinking == true) {
                    status.setIcon(Icons.getIcon(DefaultIcons.EMPTY));
                }
            }
        });
        
    // init
        setNoAction();
    }
    
    /** */
    private void setStatus(ImageIcon icon,String text) {
        this.icon = icon;
        status.setIcon(icon);
        status.setText(text);
    }
    
    /** */
    void setAction(ImageIcon icon,String text) {
        setStatus(icon,text);
        blinking = true;
    }
    
    /** */
    void setNoAction() {
        ImageIcon icon = null;        
    // icon
        if (Builders.get().hasErrors()) {
            icon = Icons.getIcon(BuildersIcons.BUILDER,
                DefaultIcons.DECO_ERROR);
        }
        else if (Builders.get().hasWarnings()) {
            icon = Icons.getIcon(BuildersIcons.BUILDER,
                DefaultIcons.DECO_WARNING);
        }
        else {
            icon = Icons.getIcon(BuildersIcons.BUILDER);
        }
        
    // status
        setStatus(icon,res.getStr("no.action"));
        blinking = false;
        
    // terminate
        setTerminable(false);
    }
    
    /** */
    void setTerminable(boolean terminable) {
        terminateButton.setEnabled(terminable);
    }
}