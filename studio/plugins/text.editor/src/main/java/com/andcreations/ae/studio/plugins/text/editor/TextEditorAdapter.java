package com.andcreations.ae.studio.plugins.text.editor;

/**
 * @author Mikolaj Gucki
 */
public abstract class TextEditorAdapter implements TextEditorListener {
    /** */
    @Override
    public void focusLost(EditorMediator editor) {
    }
    
    /** */
    @Override
    public void focusGained(EditorMediator editor) {
    }
    
    /** */
    @Override
    public void fileSaved(EditorMediator editor) {
    }
    
    /** */
    @Override
    public void textRecentlyChanged(EditorMediator editor) {
    }    
    
    /** */
    @Override
    public void textEditorCreated(EditorMediator editor,
        TextEditorViewHandler viewHandler) {
    }    
    
    /** */
    @Override
    public void textEditorClosed(EditorMediator editor) {
    }
    
    /** */
    @Override
    public void gutterIconRemoved(EditorMediator editor,GutterIcon gutterIcon) {
    }
    
    /** */
    @Override
    public void gutterIconLinesUpdated(EditorMediator editor) {
    }
}