package com.andcreations.ae.studio.plugins.simulator;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.simulator.resources.R;
import com.andcreations.ae.studio.plugins.ui.common.layout.FormLayoutBuilder;
import com.andcreations.ae.studio.plugins.ui.common.validation.IntegerVerifier;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewBorder;
import com.andcreations.resources.BundleResources;
import com.andcreations.resources.ResourceLoader;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class SimulatorComponent extends JPanel {
    /** */
    private static final int VOLUME_SLIDER_MIN = 0;
    
    /** */
    private static final int VOLUME_SLIDER_MAX = 100;
    
    /** */
    private static BundleResources res =
        new BundleResources(SimulatorComponent.class);
    
    /** */
    private SimulatorComponentListener listener;
    
    /** */
    private JButton startButton;
    
    /** */
    private JButton stopButton;
    
    /** */
    private JButton pauseResumeButton;
    
    /** */
    private JButton debugButton;
    
    /** */
    private JSlider volumeSlider;
    
    /** */
    private JTextField widthField;
    
    /** */
    private IntegerVerifier widthVerifier;
    
    /** */
    private JTextField heightField;
    
    /** */
    private IntegerVerifier heightVerifier;
    
    /** */
    SimulatorComponent(SimulatorComponentListener listener) {
        this.listener = listener;
        create();
    }
    
    /** */
    private void create() {
        setLayout(new BorderLayout());
        
        createControlPanel();
        createAudioPanel();
        createScreenPanel();
        layoutComponents();
    }
    
    /** */
    private void layoutComponents() {
        FormLayoutBuilder builder;
        try {
            String src = ResourceLoader.loadAsString(
                R.class,"SimulatorComponent.formlayout");
            builder = new FormLayoutBuilder(src);
        } catch (IOException exception) {
            Log.error("Failed to read form layout " + exception.getMessage());
            return;
        }
        
        builder.addSeparator(res.getStr("control"),"c");
        builder.add(startButton,"s");
        builder.add(stopButton,"t");
        builder.add(pauseResumeButton,"p");
        builder.add(debugButton,"d");
        
        builder.addSeparator(res.getStr("audio"),"a");
        builder.addLabel(res.getStr("volume"),"v");
        builder.add(volumeSlider,"l");        

        builder.addSeparator(res.getStr("screen"),"i");
        builder.addLabel(res.getStr("width"),"wl");
        builder.add(widthField,"wf");
        builder.addLabel(res.getStr("height"),"hl");
        builder.add(heightField,"hf");
        
        JPanel panel = builder.getPanel();
        ViewBorder.set(panel);
        add(panel,BorderLayout.CENTER);
    }
    
    /** */
    private void createControlPanel() {
    // start
        startButton = new JButton();
        startButton.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                listener.startSimulator();
            }
        });

    // stop 
        stopButton = new JButton(res.getStr("stop"));
        stopButton.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                listener.stopSimulator();
            }
        });
            
    // pause/resume
        pauseResumeButton = new JButton();
        pauseResumeButton.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                listener.pauseResumeSimulator();
            }
        });
        
    // debug
        debugButton = new JButton(res.getStr("debug"));
        debugButton.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                listener.debugSimulator();
            }
        });
    }
    
    /** */
    private void createAudioPanel() {
    // volume
        volumeSlider = new JSlider(VOLUME_SLIDER_MIN,VOLUME_SLIDER_MAX);
        volumeSlider.addChangeListener(new ChangeListener() {
            /** */
            @Override
            public void stateChanged(ChangeEvent event) {
                double volume = volumeSlider.getValue() /
                    (double)(VOLUME_SLIDER_MAX - VOLUME_SLIDER_MIN);
                listener.volumeChanged(volume);
            }
        });
    }
    
    /** */
    private void createScreenPanel() {
        final int min = 1;
        final int max = 4096;
            
        DocumentListener documentListener = new DocumentListener() {
            /** */
            @Override
            public void changedUpdate(DocumentEvent e) {
                resolutionChanging();
            }
            
            /** */
            @Override
            public void removeUpdate(DocumentEvent e) {
                resolutionChanging();
            }
            
            /** */
            @Override
            public void insertUpdate(DocumentEvent e) {
                resolutionChanging();
            }            
        };
        
    // width
        widthField = new JTextField(4);
        widthField.setToolTipText(res.getStr("width.tool.tip",
            Integer.toString(min),Integer.toString(max)));
        widthVerifier = new IntegerVerifier(widthField,
            res.getStr("width.error.tool.tip",Integer.toString(min),
            Integer.toString(max)));
        widthVerifier.setMin(min);
        widthVerifier.setMax(max);
        widthField.setInputVerifier(widthVerifier);
        widthField.getDocument().addDocumentListener(documentListener);
        widthField.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                resolutionChanged();
            }
        });
        
    // height
        heightField = new JTextField(4);
        heightField.setToolTipText(res.getStr("height.tool.tip",
            Integer.toString(min),Integer.toString(max)));
        heightVerifier = new IntegerVerifier(heightField,
            res.getStr("height.error.tool.tip",Integer.toString(min),
                Integer.toString(max)));
        heightVerifier.setMin(min);
        heightVerifier.setMax(max);
        heightField.setInputVerifier(heightVerifier);
        heightField.getDocument().addDocumentListener(documentListener);
        heightField.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                resolutionChanged();
            }
        });
        
    }
    
    /** */
    void init() {
        stopped();
    }
    
    /** */
    private boolean verifyResolution() {
        return widthVerifier.verify() && heightVerifier.verify();
    }
    
    /** */
    private void resolutionChanging() {
        if (verifyResolution() == true) {
            listener.resolutionChanging(widthVerifier.getValue(),
                heightVerifier.getValue());
        }
    }
    
    /** */
    private void resolutionChanged() {
        if (verifyResolution() == true) {
            listener.resolutionChanged(widthVerifier.getValue(),
                heightVerifier.getValue());
        }
    }
    
    /** */
    private void setResolutionEnabled(boolean enabled) {
        widthField.setEnabled(enabled);
        heightField.setEnabled(enabled);
    }
    
    /** */
    private void setButton(JButton button,String text,ImageIcon icon,
        boolean enabled) {
    //
        button.setText(text);
        button.setIcon(icon);
        button.setEnabled(enabled);
    }
    
    /** */
    void stopped() {
        setButton(startButton,res.getStr("start"),
            Icons.getIcon(DefaultIcons.PLAY),true);
        setButton(stopButton,res.getStr("stop"),
            Icons.getIcon(DefaultIcons.STOP),false);
        setButton(pauseResumeButton,res.getStr("pause"),
            Icons.getIcon(DefaultIcons.PAUSE),false);   
        setButton(debugButton,res.getStr("debug"),
            Icons.getIcon(DefaultIcons.DEBUG),true);
        setResolutionEnabled(true);
    }
    
    /** */
    void running() {
        setButton(startButton,res.getStr("restart"),
            Icons.getIcon(DefaultIcons.PLAY),true);
        setButton(stopButton,res.getStr("stop"),
            Icons.getIcon(DefaultIcons.STOP),true);
        setButton(pauseResumeButton,res.getStr("pause"),
            Icons.getIcon(DefaultIcons.PAUSE),true);
        setButton(debugButton,res.getStr("debug"),
            Icons.getIcon(DefaultIcons.DEBUG),false);
        setResolutionEnabled(false);
    }
    
    /** */
    void paused() {
        setButton(pauseResumeButton,res.getStr("resume"),
            Icons.getIcon(DefaultIcons.PAUSE),true);
    }
    
    /** */
    void resumed() {
        setButton(pauseResumeButton,res.getStr("pause"),
            Icons.getIcon(DefaultIcons.PAUSE),true);
    }
    
    /** */
    void setVolume(double volume) {
        volumeSlider.setValue(VOLUME_SLIDER_MIN +
            (int)(volume * (VOLUME_SLIDER_MAX - VOLUME_SLIDER_MIN)));
    }
    
    /** */
    void setResolution(int width,int height) {
        widthField.setText(Integer.toString(width));
        heightField.setText(Integer.toString(height));
        resolutionChanged();
    }
}