package com.andcreations.ae.studio.plugins.adb;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.android.AndroidIcons;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.ae.studio.plugins.ui.main.view.DefaultViewProvider;
import com.andcreations.ae.studio.plugins.ui.main.view.SingleViewFactory;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCategory;
import com.andcreations.android.adb.ADBDevice;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class ADBDevices {
    /** The view identifier. */
    private static final String VIEW_ID =
        "com.andcreations.ae.studio.plugins.adb.devices"; 
        
    /** */
    private static ADBDevices instance;
    
    /** */
    private BundleResources res = new BundleResources(ADBDevices.class);     
    
    /** */
    private ADBDevicesViewComponent component;
    
    /** */
    void init() {
        initUI();
    }
    
    /** */
    private void initUI() {
    // component
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                component = new ADBDevicesViewComponent();
            }
        });
        
        String title = res.getStr("view.title");
        ImageIcon icon = Icons.getIcon(AndroidIcons.ANDROID_DEVICES);
        
    // view factory
        SingleViewFactory factory = new SingleViewFactory(
            VIEW_ID,component,title,icon);
        factory.setViewCategory(ViewCategory.DETAILS);
        MainFrame.get().getViewManager().registerViewFactory(factory);
        
    // view provider
        DefaultViewProvider provider = new DefaultViewProvider(
            title,icon,factory);
        MainFrame.get().getViewManager().addViewProvider(provider);        
    }
    
    /** */
    void deviceConnected(final ADBDevice device) {       
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                component.deviceConnected(device);
            }
        });
    }
    
    /** */
    void deviceDisconnected(final ADBDevice device) {
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                component.deviceDisconnected(device);
            }            
        });
    }
    
    /** */
    static ADBDevices get() {
        if (instance == null) {
            instance = new ADBDevices();
        }
        
        return instance;
    }
}