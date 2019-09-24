package com.andcreations.ae.studio.plugins.todo;

/**
 * The interface for todo item listeners.
 *
 * @author Mikolaj Gucki
 */
public interface ToDoItemListener {
    /**
     * Called when a todo item has been double-clicked.
     *
     * @param item The item.
     */
    void toDoItemDoubleClicked(ToDoItem item);
}