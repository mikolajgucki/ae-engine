package com.andcreations.ae.studio.plugins.text.editor;

import org.fife.rsta.ui.GoToDialog;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class TextEditorGoToLineDialog extends GoToDialog {
    /** */
    private final BundleResources res =
        new BundleResources(TextEditorGoToLineDialog.class);    
    
    /** */
    private TextEditorGoToLineDialog() {
        super(MainFrame.get());
        setTitle(res.getStr("title"));
    }
    
    /** */
    static void showDialog() {
        EditorMediator mediator = TextEditor.get().getFocusMediator();
        if (mediator == null) {
            return;
        }    
        RSyntaxTextArea textArea = mediator.getComponent().getTextArea();
        
    // dialog
        TextEditorGoToLineDialog dialog = new TextEditorGoToLineDialog();
        dialog.setModal(true);
        dialog.setMaxLineNumberAllowed(textArea.getLineCount());
        dialog.setVisible(true);
        
    // go to
        int line = dialog.getLineNumber();
        if (line > 0) {
            mediator.getComponent().goToLine(line);
        }        
    }
    
    /** */
    @Override
    protected void displayInvalidLineNumberMessage() {
        CommonDialogs.error(res.getStr("title"),res.getStr("no.such.line",
            Integer.toString(getMaxLineNumberAllowed())));
    }
}