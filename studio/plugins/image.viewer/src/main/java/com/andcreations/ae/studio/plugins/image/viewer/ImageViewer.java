package com.andcreations.ae.studio.plugins.image.viewer;

import java.io.File;

import com.andcreations.ae.studio.plugins.file.explorer.tree.FileTreeComponent;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.ae.studio.plugins.ui.main.view.DefaultViewProvider;
import com.andcreations.ae.studio.plugins.ui.main.view.SingleViewFactory;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCategory;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class ImageViewer {
    /** The view identifier. */
    private static final String VIEW_ID = ImageViewer.class.getName();
    
    /** */
    private static ImageViewer instance;
    
    /** */
    private BundleResources res = new BundleResources(ImageViewer.class);    
    
    /** */
    private ImageViewerComponent component;
    
    /** */
    private DefaultViewProvider viewProvider;
    
    /** */
    void init() {
    // file tree
        FileTreeComponent.registerFileTreeNodeRenderer(
            new ImageFileTreeNodeRenderer()); 
        FileTreeComponent.registerFileTreeNodeListener(
            new ImageFileTreeNodeListener());
        
    // component
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                component = new ImageViewerComponent();
            }
        });  
        
    // factory
        SingleViewFactory factory = new SingleViewFactory(
            VIEW_ID,component,res.getStr("view.title"),
            Icons.getIcon(DefaultIcons.IMAGE));
        factory.setViewCategory(ViewCategory.EDITOR);
        MainFrame.get().getViewManager().registerViewFactory(factory);
        
    // provider
        viewProvider = new DefaultViewProvider(
            res.getStr("view.title"),Icons.getIcon(DefaultIcons.IMAGE),factory);
        MainFrame.get().getViewManager().addViewProvider(viewProvider);
    }
    
    /** */
    public boolean isImageFile(File file) {
        return file.getName().endsWith(".png");
    }
    
    /** */
    public void showImage(File file) {
        component.showImage(file);
    }
    
    /** */
    public void showViewAndImage(File file) {
        viewProvider.showView();
        showImage(file);
    }
    
    /** */
    public static ImageViewer get() {
        if (instance == null) {
            instance = new ImageViewer();
        }
        
        return instance;
    }
}