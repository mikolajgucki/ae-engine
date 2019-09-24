package com.andcreations.ae.plugin.ui.studio.plugin.layout;

import java.io.File;

/**
 * @author Mikolaj Gucki
 */
interface LayoutViewerComponentListener {
    /** */
    void addToLayouts(File file);
    
    /** */
    void layoutViewerComponentShown();
    
    /** */
    void layoutViewerComponentResized();
}