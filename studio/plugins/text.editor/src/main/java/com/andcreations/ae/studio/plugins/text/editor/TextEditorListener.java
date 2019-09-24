package com.andcreations.ae.studio.plugins.text.editor;

/**
 * @author Mikolaj Gucki
 */
public interface TextEditorListener {
    /**
     * Called when an editor has lost focus.
     *
     * @param editor The editor.
     */
    void focusLost(EditorMediator editor);
    
    /**
     * Called when an editor has gained focus.
     *
     * @param editor The editor.
     */
    void focusGained(EditorMediator editor);
    
    /**
     * Called when the content of a file has been saved.
     *
     * @param editor The editor.
     */ 
    void fileSaved(EditorMediator editor);
    
    /**
     * Called when the content has changed recently.
     *
     * @param editor The editor.
     */
    void textRecentlyChanged(EditorMediator editor);
    
    /**
     * Called when a text editor has been created.
     *
     * @param editor The editor.
     * @param viewHandler The view handler.
     */
    void textEditorCreated(EditorMediator editor,
        TextEditorViewHandler viewHandler);
    
    /**
     * Called when a text editor has been closed.
     *
     * @param editor The editor.
     */
    void textEditorClosed(EditorMediator editor);
    
    /**
     * Called when a gutter icon has been removed.
     *
     * @param editor The editor.
     * @param gutterIcon The gutter icon.
     */
    void gutterIconRemoved(EditorMediator editor,GutterIcon gutterIcon);
    
    /** 
     * Called when the lines of the gutter icons have been updated.
     *
     * @param editor The editor.
     */
    void gutterIconLinesUpdated(EditorMediator editor);
}