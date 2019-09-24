package com.andcreations.ae.studio.plugins.outline;

import java.io.File;

/**
 * Provides outline items.
 *
 * @author Mikolaj Gucki
 */
public interface OutlineSource {
    /** */
    OutlineItem getOutlineRootItem(File file);
    
    /** */
    void setOutlineSourceListener(OutlineSourceListener listener);
}