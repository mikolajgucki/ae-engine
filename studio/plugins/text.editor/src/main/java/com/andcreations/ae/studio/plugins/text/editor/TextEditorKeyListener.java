package com.andcreations.ae.studio.plugins.text.editor;

import java.awt.event.KeyEvent;

/**
 * @author Mikolaj Gucki
 */
public interface TextEditorKeyListener {
    /**
     * Called when a key has been pressed.
     *
     * @param mediator The editor with focus.
     * @param event The event.
     */
    void textEditorKeyPressed(EditorMediator mediator,KeyEvent event);
    
    /**
     * Called when a key has been released.
     *
     * @param mediator The editor with focus.
     * @param event The event.
     */
    void textEditorKeyReleased(EditorMediator mediator,KeyEvent event);
    
    /**
     * Called when a key has been typed.
     *
     * @param mediator The editor with focus.
     * @param event The event.
     */
    void textEditorKeyTyped(EditorMediator mediator,KeyEvent event);
}