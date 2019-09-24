package com.andcreations.ae.studio.plugins.assets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.andcreations.ae.studio.plugins.project.ProjectMenu;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.common.UIKeys;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class Assets {
    /** */
    private static Assets instance;
    
    /** */
    private BundleResources res = new BundleResources(Assets.class);      
    
    /** */
    void init() {
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {        
                addRunAssetPipelineMenu();
                ProjectMenu.get().addSeparator();                
            }
        });        
        
    // pipelin
        AssetsBuilderPipeline.get().init();
    }
    
    /** */
    private void addRunAssetPipelineMenu() {
        JMenuItem item = new JMenuItem(res.getStr("run.pipeline"));
        item.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_A,UIKeys.menuKeyMask() | KeyEvent.SHIFT_DOWN_MASK));
        item.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                runAssetPipeline();
            }
        });
        
        ProjectMenu.get().add(item);           
    }
    
    /** */    
    private void runAssetPipeline() {
        AssetsBuilderPipeline.get().runPipeline();
    }
    
    /** */
    public static Assets get() {
        if (instance == null) {
            instance = new Assets();
        }
        
        return instance;
    }
}