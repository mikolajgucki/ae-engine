package com.andcreations.ae.studio.plugins.ui.common.layout;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.resources.ResourceLoader;

/**
 * @author Mikolaj Gucki
 */
public class FormLayoutUtil {
    /** */
    private Class<?> clazz;
    
    /** */
    private String resourceName;
    
    /** */
    private Map<String,JComponent> components = new HashMap<>();
    
    /** */
    private Map<String,String> texts = new HashMap<>();
    
    /** */
    private Map<String,JLabel> labels = new HashMap<>();
    
    /** */
    public FormLayoutUtil(Class<?> clazz,String resourceName) {
        this.clazz = clazz;
        this.resourceName = resourceName;
    }
    
    /** */
    public void layoutCenterRight(String id,JComponent center,
        JComponent right) {
    //
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(center,BorderLayout.CENTER);
        panel.add(right,BorderLayout.EAST);
        addComponent(id,panel);
    }
    
    /** */
    public void addComponent(String id,JComponent component) {
        components.put(id,component);
    }
    
    /** */
    public void addLabelText(String id,String text) {
        texts.put(id,text);
    }
    
    /** */
    public JLabel getLabel(String id) {
        return labels.get(id);
    }
    
    /** */
    public JPanel build() {
        FormLayoutBuilder builder;
        try {
            String src = ResourceLoader.loadAsString(clazz,resourceName);
            builder = new FormLayoutBuilder(src);
        } catch (IOException exception) {
            Log.error("Failed to read form layout: " + exception.getMessage());
            return null;
        }        
        
        for (String id:components.keySet()) {
            builder.add(components.get(id),id);
        }
        for (String id:texts.keySet()) {
            JLabel label = builder.addLabel(texts.get(id),id);
            labels.put(id,label);
        }
        
        return builder.getPanel();        
    }
}