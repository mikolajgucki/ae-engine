package com.andcreations.ae.studio.plugins.lua.explorer;

import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeComponent;
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
class LuaExplorerInit {
    /** The view identifier. */
    private static final String VIEW_ID =
        "com.andcreations.ae.studio.plugins.lua.explorer";  
        
    /** */
    private BundleResources res =
        new BundleResources(LuaExplorerInit.class);          
        
    /** */
    private LuaExplorerComponent component;
    
    /** */
    void init() {
        LuaExplorerIcons.init();
                
    // file tree
        FileTreeComponent.registerFileTreeNodeRenderer(
            new LuaFileTreeNodeRenderer());
        FileTreeComponent.registerFileTreeNodeListener(
            new LuaFileTreeNodeListener());             
        
    // component
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                component = new LuaExplorerComponent();
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
        
    // factory
        SingleViewFactory factory = new SingleViewFactory(
            VIEW_ID,component,res.getStr("view.title"),
            Icons.getIcon(LuaExplorerIcons.LUA_EXPLORER));
        factory.setViewCategory(ViewCategory.NAVIGATOR);
        factory.addViewDecorator(new AddListenerViewDecorator(viewListener));
        factory.addViewDecorator(new EditorSyncViewDecorator(component));   
        MainFrame.get().getViewManager().registerViewFactory(factory);
        
    // provider
        DefaultViewProvider provider = new DefaultViewProvider(
            res.getStr("view.title"),
            Icons.getIcon(LuaExplorerIcons.LUA_EXPLORER),factory);
        MainFrame.get().getViewManager().addViewProvider(provider);    
    }
}