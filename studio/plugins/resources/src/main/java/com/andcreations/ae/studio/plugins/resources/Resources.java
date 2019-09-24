package com.andcreations.ae.studio.plugins.resources;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.andcreations.ae.studio.plugins.project.ProjectMenu;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.common.UIKeys;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class Resources {
    /** */
    private static Resources instance;
    
    /** */
    private BundleResources res = new BundleResources(Resources.class);      
    
    /** */
    private OpenResourceDialog openResourceDialog;
    
    /** */
    private List<ResourceSource> sources = new ArrayList<>();
    
    /** */
    void init() {
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {        
                addOpenResourceMenu();
                createOpenResourceDialog();
            }
        });
    }
    
    /** */
    private void addOpenResourceMenu() {
        JMenuItem item = new JMenuItem(res.getStr("open.resource"));
        item.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_R,UIKeys.menuKeyMask() | KeyEvent.SHIFT_DOWN_MASK));
        item.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                openResource();
            }
        });
        
        ProjectMenu.get().add(item);           
    }    

    
    /** */
    private void createOpenResourceDialog() {
        openResourceDialog = new OpenResourceDialog();        
    }    
    
    /** */
    private void openResource() {
        OpenResourceDialog.Item item = openResourceDialog.show(sources);
        if (item != null) {
            item.source.openResource(item.resource);
        }        
    }
    
    /** */
    public void addSource(ResourceSource source) {
        sources.add(source);
    }
    
    /** */
    public static Resources get() {
        if (instance == null) {
            instance = new Resources();
        }
        
        return instance;
    }
}