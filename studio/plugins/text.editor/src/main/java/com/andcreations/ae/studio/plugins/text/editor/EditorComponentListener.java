package com.andcreations.ae.studio.plugins.text.editor;

import java.awt.event.KeyEvent;

/**
 * @author Mikolaj Gucki
 */
interface EditorComponentListener {
    /**
     * Called when the edited text has changed.
     */ 
    void textChanged();
    
    /**
     * Called when the file has been saved.
     */
    void fileSaved();
    
    /**
     * Called when a key has been pressed.
     *
     * @param event The event.
     * @return <code>true</code> if the event has been consumed,
     *   <code>false</code> otherwise.
     */
    boolean keyPressed(KeyEvent event);
    
    /**
     * Called when a key has been released.
     *
     * @param event The event.
     * @return <code>true</code> if the event has been consumed,
     *   <code>false</code> otherwise.
     */
    boolean keyReleased(KeyEvent event);
    
    /**
     * Called when a key has been pressed.
     *
     * @param event The event.
     * @return <code>true</code> if the event has been consumed,
     *   <code>false</code> otherwise.
     */
    boolean keyTyped(KeyEvent event);
    
    /**
     * Called when a gutter icon has been removed.
     *
     * @param gutterIcon The removed gutter icon.
     */
    void gutterIconRemoved(GutterIcon gutterIcon);
    
    /**
     * Called when the lines of the gutter icons have been updated.
     */     
    void gutterIconLinesUpdated();
}