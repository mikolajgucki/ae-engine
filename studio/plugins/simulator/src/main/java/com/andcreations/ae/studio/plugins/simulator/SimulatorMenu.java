package com.andcreations.ae.studio.plugins.simulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.andcreations.ae.studio.plugins.project.ProjectMenu;
import com.andcreations.ae.studio.plugins.ui.common.UIKeys;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class SimulatorMenu {
    /** */
    private BundleResources res = new BundleResources(SimulatorMenu.class);
    
    /** */
    private SimulatorMenuListener listener;
    
    /** */
    private JMenuItem startItem;
    
    /** */
    private JMenuItem stopItem;
    
    /** */
    private JMenuItem pauseResumeItem;
    
    /** */
    private JMenuItem debugItem;
    
    /** */
    SimulatorMenu(SimulatorMenuListener listener) {
        this.listener = listener;
        create();
    }
    
    /** */
    private void create() {
    // start
        startItem = new JMenuItem(res.getStr("start"),
            Icons.getIcon(DefaultIcons.PLAY));
        startItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9,0));        
        startItem.addActionListener(new ActionListener() {
            /** */  
            @Override
            public void actionPerformed(ActionEvent event) {
                listener.startSimulator();
            }
        });
        
    // stop
        stopItem = new JMenuItem(res.getStr("stop"),
            Icons.getIcon(DefaultIcons.STOP));
        stopItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11,0));        
        stopItem.addActionListener(new ActionListener() {
            /** */  
            @Override
            public void actionPerformed(ActionEvent event) {
                listener.stopSimulator();
            }
        });
        
    // pause/resume
        pauseResumeItem = new JMenuItem(res.getStr("start"),
            Icons.getIcon(DefaultIcons.PLAY));
        pauseResumeItem.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_F12,0));        
        pauseResumeItem.addActionListener(new ActionListener() {
            /** */  
            @Override
            public void actionPerformed(ActionEvent event) {
                listener.pauseResumeSimulator();
            }
        });

    // debug
        debugItem = new JMenuItem(res.getStr("debug"),
            Icons.getIcon(DefaultIcons.DEBUG));
        debugItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_F9,UIKeys.menuKeyMask()));        
        debugItem.addActionListener(new ActionListener() {
            /** */  
            @Override
            public void actionPerformed(ActionEvent event) {
                listener.debugSimulator();
            }
        });
                
    // add to menu
        ProjectMenu.get().insertSeparator(0);
        ProjectMenu.get().insert(debugItem,0);
        ProjectMenu.get().insert(pauseResumeItem,0);
        ProjectMenu.get().insert(stopItem,0);
        ProjectMenu.get().insert(startItem,0);
    }
    
    /** */
    void init() {
        stopped();
    }
    
    /** */
    private void setItem(JMenuItem item,String text,ImageIcon icon,
        boolean enabled) {
    //
        item.setText(text);
        item.setIcon(icon);
        item.setEnabled(enabled);
    }
    
    
    /** */
    void stopped() {
        setItem(startItem,res.getStr("start"),
            Icons.getIcon(DefaultIcons.PLAY),true);
        setItem(stopItem,res.getStr("stop"),
            Icons.getIcon(DefaultIcons.STOP),false);
        setItem(pauseResumeItem,res.getStr("pause"),
            Icons.getIcon(DefaultIcons.PAUSE),false);
        setItem(debugItem,res.getStr("debug"),
            Icons.getIcon(DefaultIcons.DEBUG),true);
    }    
    
    /** */
    void running() {
        setItem(startItem,res.getStr("restart"),
            Icons.getIcon(DefaultIcons.RESTART),true);
        setItem(stopItem,res.getStr("stop"),
            Icons.getIcon(DefaultIcons.STOP),true);
        setItem(pauseResumeItem,res.getStr("pause"),
            Icons.getIcon(DefaultIcons.PAUSE),true);
        setItem(debugItem,res.getStr("debug"),
            Icons.getIcon(DefaultIcons.DEBUG),false);
    }
    
    /** */
    void paused() {
        setItem(pauseResumeItem,res.getStr("resume"),
            Icons.getIcon(DefaultIcons.RESUME),true);
    }
    
    /** */
    void resumed() {
        setItem(pauseResumeItem,res.getStr("pause"),
            Icons.getIcon(DefaultIcons.PAUSE),true);
    }
}