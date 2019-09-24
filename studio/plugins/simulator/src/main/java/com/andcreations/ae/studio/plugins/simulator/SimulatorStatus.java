package com.andcreations.ae.studio.plugins.simulator;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class SimulatorStatus extends JPanel {
    /** */
    private BundleResources res = new BundleResources(SimulatorStatus.class);
    
    /** */
    private JLabel simulator;
    
    /** */
    private JLabel status;
    
    /** */
    SimulatorStatus() {
        create();
    }
    
    /** */
    private void create() {
        setLayout(new BorderLayout());
        
    // simulator
        simulator = new JLabel();
        simulator.setIcon(Icons.getIcon(SimulatorIcons.SIMULATOR));
        add(simulator,BorderLayout.WEST);
        
    // status
        status = new JLabel();
        add(status,BorderLayout.CENTER);
    }
    
    /** */
    private void setStatus(String str,String iconName) {
        status.setText(str);
        status.setIcon(Icons.getDecoIcon(Icons.DecoLocation.CENTER,iconName));            
    }
    
    /** */
    void running(boolean debug) {
        if (debug == false) {
            setStatus(res.getStr("running"),DefaultIcons.PLAY);
        }
        else {
            setStatus(res.getStr("debugging"),DefaultIcons.PLAY);
        }
    }
    
    /** */
    void stopped() {
        setStatus(res.getStr("stopped"),DefaultIcons.STOP);
    }
    
    /** */
    void paused() {
        setStatus(res.getStr("paused"),DefaultIcons.PAUSE);
    }
    
    /** */
    void resumed() {
        setStatus(res.getStr("running"),DefaultIcons.PLAY);
    }
    
    /** */
    void suspended() {
        setStatus(res.getStr("suspended"),DefaultIcons.PAUSE);
    }
    
    /** */
    void continuingExecution() {
        running(true);
    }
    
    /** */
    void init() {
        stopped();
    }
}