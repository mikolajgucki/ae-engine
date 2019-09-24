package com.andcreations.ae.studio.plugins.ui.main.view;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.ui.main.MainFrame;

/**
 * The default view provider which creates view via {@link ViewFactory}.
 *
 * @author Mikolaj Gucki
 */
public class DefaultViewProvider implements ViewProvider {
    /** */
    private String title;
    
    /** */
    private ImageIcon icon;
    
    /** */
    private ViewFactory viewFactory;
    
    /** */
    private String viewId;
    
    /**
     * Constructs a {@link DefaultViewProvider}.
     *
     * @param title The title.
     * @param icon The icon.
     * @param viewFactory The view factory which create the views.
     * @param viewId The view identifier passed to the factory.
     */
    public DefaultViewProvider(String title,ImageIcon icon,
        ViewFactory viewFactory,String viewId) {
    //
        this.title = title;
        this.icon = icon;
        this.viewFactory = viewFactory;
        this.viewId = viewId;
    }
    
    /**
     * Constructs a {@link DefaultViewProvider} with null view identifier.
     *
     * @param title The title.
     * @param icon The icon.
     * @param viewFactory The view factory which create the views.
     */
    public DefaultViewProvider(String title,ImageIcon icon,
        ViewFactory viewFactory) {
    //
        this(title,icon,viewFactory,null);
    }
    
    /** */
    @Override
    public String getTitle() {
        return title;
    }
    
    /** */
    @Override
    public ImageIcon getIcon() {
        return icon;
    }
    
    /** */
    @Override
    public void showView() {
        View view = MainFrame.get().getViewManager().getView(
            viewFactory,viewId);
        if (view != null) {
            MainFrame.get().getViewManager().focus(view);
            return;
        }
        
        view = viewFactory.createView(viewId);
        MainFrame.get().getViewManager().showView(view); 
    }
    
    /**
     * Shows the view even if it's closed.
     */
    public void forceShow() {
        
    }
}