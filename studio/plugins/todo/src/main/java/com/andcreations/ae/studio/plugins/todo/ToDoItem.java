package com.andcreations.ae.studio.plugins.todo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.studio.plugins.text.editor.EditorAnnotation;

/**
 * @author Mikolaj Gucki
 */
public class ToDoItem {
    /** */
    private String tag;
    
    /** */
    private String description;
    
    /** */
    private String resource;
    
    /** */
    private Object location;
    
    /** */
    private File file;
    
    /** */
    private EditorAnnotation editorAnnotation;
    
    /** */
    private List<ToDoItemListener> listeners = new ArrayList<>();
    
    /** */
    public ToDoItem(String tag,String description) {
        this(tag,description,null,null);
    }
    
    /** */
    public ToDoItem(String tag,String description,String resource) {
        this(tag,description,resource,null);
    }
    
    /** */
    public ToDoItem(String tag,String description,String resource,
        Object location) {
    //
        this.tag = tag;
        this.description = description;
        this.resource = resource;
        this.location = location;
    }
    
    /** */
    public ToDoItem(String tag,String description,String resource,
        Object location,File file) {
    //
        this.tag = tag;
        this.description = description;
        this.resource = resource;
        this.location = location;
        this.file = file;
    }
    
    /** */
    public String getTag() {
        return tag;
    }
    
    /** */
    public String getDescription() {
        return description;
    }
    
    /** */
    public String getResource() {
        return resource;
    }
    
    /** */
    public Object getLocation() {
        return location;
    }
    
    /** */
    public File getFile() {
        return file;
    }
    
    /** */
    void setEditorAnnotation(EditorAnnotation editorAnnotation) {
        this.editorAnnotation = editorAnnotation;
    }
    
    EditorAnnotation getEditorAnnotation() {
        return editorAnnotation;
    }
    
    /** */
    public void addToDoItemListener(ToDoItemListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    void notifyDoubleClicked() {
        synchronized (listeners) {
            for (ToDoItemListener listener:listeners) {
                listener.toDoItemDoubleClicked(this);
            }
        }
    }
}