package com.andcreations.ae.studio.plugins.lua.debug.traceback;

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
public class Traceback {
    /** The view identifier. */
    private static final String VIEW_ID =
        "com.andcreations.ae.studio.plugins.lua.debug.traceback";
        
    /** */
    private BundleResources res = new BundleResources(Traceback.class);         
        
    /** */
    private static Traceback instance;
    
    /** */
    private TracebackViewComponent component;
    
    /** */
    public void init() {
        initUI();
    }
    
    /** */
    public void initUI() {
    // component
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                component = new TracebackViewComponent();
            }
        });
        
        String title = res.getStr("view.title");
        ImageIcon icon = Icons.getIcon(LuaDebugIcons.TRACEBACK);
        
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
    public void setTraceback(final TracebackItem rootItem) {
    // sort the values at the first level
    /*
        for (LabelTreeNode node:rootItem.getChildrenAsList()) {
            LabelTreeNodeUtil.sortByValueIgnoreCase(node);
        }
    */
        
        UICommon.invoke(new Runnable() {
            /** */
            @Override
            public void run() {
                component.setRootItem(rootItem);
                if (rootItem.getChildCount() > 0) {
                    component.expand((TracebackItem)rootItem.getChildAt(0));
                }
            }
        });
    }
    
    /** */
    public void addChildItem(TracebackItem parent,TracebackItem item) {
        component.addChildItem(parent,item);
    }
    
    /** */    
    public List<TracebackItem> getAllItems() {
        return component.getAllItems();
    }
    
    /** */
    public void clear() {
        component.clear();
    }
    
    /** */
    public static Traceback get() {
        if (instance == null) {
            instance = new Traceback();
        }
        
        return instance;
    }
}