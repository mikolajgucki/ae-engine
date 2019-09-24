package com.andcreations.ae.studio.plugins.android.explorer;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.ae.studio.plugins.ui.main.view.AddListenerViewDecorator;
import com.andcreations.ae.studio.plugins.ui.main.view.DefaultViewProvider;
import com.andcreations.ae.studio.plugins.ui.main.view.SingleViewFactory;
import com.andcreations.ae.studio.plugins.ui.main.view.View;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewAdapter;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCategory;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewListener;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class AndroidExplorerInit {
    /** The view identifier. */
    private static final String VIEW_ID = AndroidExplorerInit.class.getName();
        
    /** */
    private BundleResources res =
        new BundleResources(AndroidExplorerInit.class);          
        
    /** */
    private AndroidExplorerComponent component;
    
    /** */
    void init() {
    // component
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                component = new AndroidExplorerComponent();
            }
        });
        
    // view listener
        ViewListener viewListener = new ViewAdapter() {
            /** */
            @Override
            public void viewFocusGained(View view) {
                component.viewFocusGained();
            }
        };        
        
    // icon
        ImageIcon icon = Icons.getIcon(AndroidExplorerIcons.ANDROID_EXPLORER);
        
    // factory
        SingleViewFactory factory = new SingleViewFactory(
            VIEW_ID,component,res.getStr("view.title"),icon);
        factory.setViewCategory(ViewCategory.NAVIGATOR);
        factory.addViewDecorator(new AddListenerViewDecorator(viewListener));
        MainFrame.get().getViewManager().registerViewFactory(factory);
        
    // provider
        DefaultViewProvider provider = new DefaultViewProvider(
            res.getStr("view.title"),icon,factory);
        MainFrame.get().getViewManager().addViewProvider(provider);    
    }
}