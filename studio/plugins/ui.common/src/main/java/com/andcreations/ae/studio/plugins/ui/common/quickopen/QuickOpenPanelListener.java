package com.andcreations.ae.studio.plugins.ui.common.quickopen;

/**
 * @author Mikolaj Gucki
 */
interface QuickOpenPanelListener {
    /** */
    void quickOpenItemSelected(QuickOpenItem item);
    
    /** */
    void quickOpenItemPicked(QuickOpenItem item);
}