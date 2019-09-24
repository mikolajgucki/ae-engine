package com.andcreations.ae.studio.plugins.ui.main.view;

/**
 * @author Mikolaj Gucki
 */
public class SetClosingListenerViewDecorator implements ViewDecorator {
    /** */
    private ViewClosingListener listener;
    
    /** */
    public SetClosingListenerViewDecorator(ViewClosingListener listener) {
        this.listener = listener;
    }
    
    /** */
    @Override
    public void decorateView(View view) {
        view.setViewClosingListener(listener);
    }
}