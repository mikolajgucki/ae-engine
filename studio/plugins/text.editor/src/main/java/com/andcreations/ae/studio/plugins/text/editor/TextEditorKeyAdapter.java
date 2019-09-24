package com.andcreations.ae.studio.plugins.text.editor;

import java.awt.event.KeyEvent;

/**
 * @author Mikolaj Gucki
 */
public abstract class TextEditorKeyAdapter implements TextEditorKeyListener {
    /** */
    @Override
    public void textEditorKeyPressed(EditorMediator mediator,KeyEvent event) {
    }
    
    /** */
    @Override
    public void textEditorKeyReleased(EditorMediator mediator,KeyEvent event) {
    }
    
    /** */
    @Override
    public void textEditorKeyTyped(EditorMediator mediator,KeyEvent event) {
    }
}