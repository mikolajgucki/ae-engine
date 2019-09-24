package com.andcreations.ae.studio.plugins.outline;

/**
 * The default outline source implementation.
 *
 * @author Mikolaj Gucki
 */
public abstract class DefaultOutlineSource implements OutlineSource {
    /** */
    private OutlineSourceListener listener;
    
    /** */
    @Override
    public void setOutlineSourceListener(OutlineSourceListener listener) {
        this.listener = listener;
    }
    
    /** */
    public void outlineSourceClosed() {
        if (listener != null) {
            listener.outlineSourceClosed();
        }
    }
}