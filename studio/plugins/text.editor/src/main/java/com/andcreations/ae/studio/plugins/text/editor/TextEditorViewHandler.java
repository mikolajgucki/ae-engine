package com.andcreations.ae.studio.plugins.text.editor;

import com.andcreations.ae.studio.plugins.ui.main.view.View;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButton;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewDropDown;

/**
 * @author Mikolaj Gucki
 */
public class TextEditorViewHandler {
    /** */
    private View view;
    
    /** */
    private ViewDropDown secondaryActions;
    
    /** */
    TextEditorViewHandler(View view,ViewDropDown secondaryActions) {
        this.view = view;
        this.secondaryActions = secondaryActions;
    }
    
    /**
     * Gets the view the handler is associated with.
     *
     * @return The view.
     */
    public View getView() {
        return view;
    }
    
    /**
     * Adds a button to the view.
     *
     * @return The new view.
     */
    public ViewButton addButton() {
        return view.addButton();
    }
    
    /**    
     * Adds a (secondaray action) button which is grouped in a drop down.
     *
     * @return The new button.
     */
    public ViewButton addSecondaryButton() {
        return secondaryActions.addButton();
    }
}