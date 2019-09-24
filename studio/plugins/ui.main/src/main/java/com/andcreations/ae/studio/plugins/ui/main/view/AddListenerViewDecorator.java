package com.andcreations.ae.studio.plugins.ui.main.view;

/**
 * @author Mikolaj Gucki
 */
public class AddListenerViewDecorator implements ViewDecorator {
    /** */
    private ViewListener listener;
    
    /** */
    public AddListenerViewDecorator(ViewListener listener) {
        this.listener = listener;
    }
    
    /** */
    @Override
    public void decorateView(View view) {
        view.addViewListener(listener);
    }
}