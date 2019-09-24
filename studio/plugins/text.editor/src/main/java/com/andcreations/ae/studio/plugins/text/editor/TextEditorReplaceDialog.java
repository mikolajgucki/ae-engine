package com.andcreations.ae.studio.plugins.text.editor;

import java.awt.Frame;

import org.fife.rsta.ui.search.ReplaceDialog;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class TextEditorReplaceDialog extends ReplaceDialog {
    /** */
    TextEditorReplaceDialog(Frame owner) {
        super(owner,new FindDialogSearchListener());
        create();
    }
    
    /** */
    private void create() {
        context.setMarkAll(false);
    }
    
    /** */
    @Override
    public void setVisible(boolean visible) {
        if (visible == false) {
            TextEditor.get().clearAllMarkAllSelections();
        }
        super.setVisible(visible);
    }   
}