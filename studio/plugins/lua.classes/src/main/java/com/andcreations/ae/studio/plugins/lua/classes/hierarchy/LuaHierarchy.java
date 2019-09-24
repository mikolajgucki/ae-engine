package com.andcreations.ae.studio.plugins.lua.classes.hierarchy;

import com.andcreations.ae.studio.plugins.lua.classes.LuaClassesIcons;
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
public class LuaHierarchy {
    /** The view identifier. */
    private static final String VIEW_ID = LuaHierarchy.class.getName();
    
    /** */
    private static LuaHierarchy instance;
    
    /** */
    private BundleResources res =
        new BundleResources(LuaHierarchy.class);
    
    /** */
    private LuaHierarchyComponent component;
    
    /** */
    public void init() {
    // component
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                component = new LuaHierarchyComponent();
            }
        });  
        
    // factory
        SingleViewFactory factory = new SingleViewFactory(
            VIEW_ID,component,res.getStr("view.title"),
            Icons.getIcon(LuaClassesIcons.LUA_HIERARCHY));
        factory.setViewCategory(ViewCategory.NAVIGATOR);
        factory.addViewDecorator(
            new LuaHierarchyRefreshViewDecorator(component));                
        MainFrame.get().getViewManager().registerViewFactory(factory);
        
    // provider
        DefaultViewProvider provider = new DefaultViewProvider(
            res.getStr("view.title"),
            Icons.getIcon(LuaClassesIcons.LUA_HIERARCHY),factory);
        MainFrame.get().getViewManager().addViewProvider(provider);         
    }
    
    /** */
    public static LuaHierarchy get() {
        if (instance == null) {
            instance = new LuaHierarchy();
        }
        
        return instance;
    }
}