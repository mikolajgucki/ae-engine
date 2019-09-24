package com.andcreations.ae.studio.plugins.project.explorer;

import java.io.File;

import com.andcreations.ae.studio.plugins.project.ProjectPlugin;
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
class ProjectExplorerInit {
    /** The view identifier. */
    private static final String VIEW_ID = ProjectExplorerInit.class.getName();
        
    /** */
    private BundleResources res =
        new BundleResources(ProjectExplorerInit.class);          
        
    /** */
    private ProjectExplorerComponent component;
    
    /** */
    void init(Object projectPluginObject) {
        final ProjectPlugin projectPlugin = (ProjectPlugin)projectPluginObject;
        initFileTreeComponent(projectPlugin.getProjectDir());
        
    // icons
        ProjectExplorerIcons.init();
        
    // component
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                component = new ProjectExplorerComponent(
                    projectPlugin.getProjectDir());
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
            Icons.getIcon(ProjectExplorerIcons.PROJECT_EXPLORER));
        factory.setViewCategory(ViewCategory.NAVIGATOR);
        factory.addViewDecorator(new AddListenerViewDecorator(viewListener));
        factory.addViewDecorator(new EditorSyncViewDecorator(component));        
        MainFrame.get().getViewManager().registerViewFactory(factory);
        
    // provider
        DefaultViewProvider provider = new DefaultViewProvider(
            res.getStr("view.title"),
            Icons.getIcon(ProjectExplorerIcons.PROJECT_EXPLORER),factory);
        MainFrame.get().getViewManager().addViewProvider(provider);    
    }
    
    /** */
    private static void initFileTreeComponent(File projectDir) {
        /*
        FileTreeComponent.registerFileTreeNodeRenderer(
            new ProjectExplorerFileTreeNodeRenderer(projectDir));
        */
    }
}