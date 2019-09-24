package com.andcreations.ae.studio.plugins.ui.main.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import com.andcreations.ae.studio.plugins.ui.main.MainFrame;

/**
 * A view factory which provides a single view.
 *
 * @author Mikolaj Gucki
 */
public class SingleViewFactory implements ViewFactory {
    /** */
    private String viewId;
    
    /** */
    private JComponent component;
    
    /** */
    private String title;
    
    /** */
    private ImageIcon icon;
    
    /** */
    private ViewCategory category;
    
    /** */
    private List<ViewDecorator> decorators = new ArrayList<>();
    
    /**
     * Constructs a {@link SingleViewFactory}.
     *
     * @param viewId The view identifier.
     * @param component The view component.
     * @param title The view title.
     * @param icon The view icon.
     */
    public SingleViewFactory(String viewId,JComponent component,String title,
        ImageIcon icon) {
    //
        this.viewId = viewId;
        this.component = component;
        this.title = title;
        this.icon = icon;
    }
    
    /**
     * Sets the view category.
     *
     * @param category The category.
     */
    public void setViewCategory(ViewCategory category) {
        this.category = category;
    }
    
    /**
     * Adds a view decorator.
     *
     * @param decorator The decorator.
     */
    public void addViewDecorator(ViewDecorator decorator) {
        decorators.add(decorator);
    }
    
    /** */
    @Override
    public String getId() {
        return viewId;
    }
    
    /** */
    @Override
    public View createView(String viewId) {
        View view = MainFrame.get().getViewManager().getView(this,viewId);
        if (view == null) {
            view = MainFrame.get().getViewManager().createView(this,viewId);
            view.setTitle(title);
            view.setIcon(icon);
            view.setComponent(component);
            view.setCategory(category);
            
            for (ViewDecorator decorator:decorators) {
                decorator.decorateView(view);
            }
        }
        
        return view;        
    }
}