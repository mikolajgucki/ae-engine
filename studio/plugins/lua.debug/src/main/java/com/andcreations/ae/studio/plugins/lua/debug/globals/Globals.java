package com.andcreations.ae.studio.plugins.lua.debug.globals;

import java.util.List;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.lua.debug.LuaDebugIcons;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.ae.studio.plugins.ui.main.view.DefaultViewProvider;
import com.andcreations.ae.studio.plugins.ui.main.view.SingleViewFactory;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCategory;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class Globals {
    /** The view identifier. */
    private static final String VIEW_ID =
        "com.andcreations.ae.studio.plugins.lua.debug.globals";
        
    /** */
    private BundleResources res = new BundleResources(Globals.class); 
    
    /** */
    private static Globals instance;
    
    /** */
    private GlobalsViewComponent component;
    
    /** */
    public void init() {
        initUI();
    }
    
    /** */
    private void initUI() {
    // component
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                component = new GlobalsViewComponent();
            }
        });
        
        String title = res.getStr("view.title");
        ImageIcon icon = Icons.getIcon(LuaDebugIcons.GLOBALS);
        
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
    public void setGlobals(GlobalsItem rootItem) {
        component.setRootItem(rootItem);
    }
    
    /** */
    public void clear() {
        component.clear();
    }
    
    /** */
    public void addChildItem(GlobalsItem parent,GlobalsItem item) {
        component.addChildItem(parent,item);
    }    
    
    /** */
    public List<GlobalsItem> getAllItems() {
        return component.getAllItems();
    }
    
    /** */
    public static Globals get() {
        if (instance == null) {
            instance = new Globals();
        }
        
        return instance;
    }
}