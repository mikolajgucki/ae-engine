package com.andcreations.ae.studio.plugins.ui.common.settings;

/**
 * @author Mikolaj Gucki
 */
public class SettingsContext {
    /** */
    private SettingsDialog dialog;
    
    /** */
    SettingsContext(SettingsDialog dialog) {
        this.dialog = dialog;
    }
    
    /** */
    public void setPageError(SettingsPage page) {
        dialog.setPageError(page);
    }
    
    /** */
    public void clearPageIssues(SettingsPage page) {
        dialog.clearPageIssues(page);
    }
}