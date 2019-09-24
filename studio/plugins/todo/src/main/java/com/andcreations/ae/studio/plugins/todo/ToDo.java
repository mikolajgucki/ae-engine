package com.andcreations.ae.studio.plugins.todo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;

import com.andcreations.ae.studio.plugins.file.LineLocation;
import com.andcreations.ae.studio.plugins.text.editor.EditorAnnotation;
import com.andcreations.ae.studio.plugins.text.editor.EditorMediator;
import com.andcreations.ae.studio.plugins.text.editor.TextEditor;
import com.andcreations.ae.studio.plugins.text.editor.TextEditorAdapter;
import com.andcreations.ae.studio.plugins.text.editor.TextEditorViewHandler;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.ae.studio.plugins.ui.main.view.DefaultViewProvider;
import com.andcreations.ae.studio.plugins.ui.main.view.SingleViewFactory;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCategory;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class ToDo {
    /** The view identifier. */
    private static final String VIEW_ID =
        "com.andcreations.ae.studio.plugins.todo";      
    
    /** The class instance. */
    private static ToDo instance;
        
    /** The view component. */
    private ToDoViewComponent component;
    
    /** The items. */
    private List<ToDoItem> items = new ArrayList<>();
    
    /** */
    private BundleResources res = new BundleResources(ToDo.class);     
    
    /**
     * Adds a todo item.
     *
     * @param item The item.
     */
    public void addItem(ToDoItem item) {
        items.add(item);
        component.addItem(item);
        addEditorAnnotation(item);
    }
    
    /**
     * Removes a todo item.
     *
     * @param item The item.
     */
    public void removeItem(ToDoItem item) {
        items.remove(item);
        component.removeItem(item);
        removeEditorAnnotation(item);
    }
    
    /** */
    String[] getTags() {
        return new String[]{"TODO"};
    }
    
    /** */
    void init() {
        TextEditor.get().addTextEditorListener(new TextEditorAdapter() {
            /** */
            @Override
            public void textEditorCreated(EditorMediator editor,
                TextEditorViewHandler viewHandler) {
            //
                addEditorAnnotations(editor);
            }
        });
        
    // UI
        initUI();
    }
    
    /** */
    private void initUI() {
    // component
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                component = new ToDoViewComponent();
            }
        });
        
        String title = res.getStr("view.title");
        ImageIcon icon = Icons.getIcon(ToDoIcons.TODO);
        
    // view factory
        SingleViewFactory factory = new SingleViewFactory(
            VIEW_ID,component,title,icon);
        factory.setViewCategory(ViewCategory.DETAILS);
        MainFrame.get().getViewManager().registerViewFactory(factory);
        
    // view provider
        DefaultViewProvider provider = new DefaultViewProvider(
            title,icon,factory);
        MainFrame.get().getViewManager().addViewProvider(provider);
    }     
    
    /** */
    private void addEditorAnnotation(ToDoItem item,EditorMediator editor) {
        File file = editor.getFile();
        if (file == null) {
            return;
        }
        
        if (file.equals(item.getFile()) == true &&
            item.getLocation() instanceof LineLocation) {
        //
            LineLocation location = (LineLocation)item.getLocation();
            try {
                EditorAnnotation annotation = editor.addAnnotation(
                    location.getLine(),item.getDescription(),
                    Icons.blue(),EditorAnnotation.Priority.LOW);
                item.setEditorAnnotation(annotation);
            } catch (BadLocationException exception) {
            }
        }        
    }
    
    /** */
    private void addEditorAnnotation(ToDoItem item) {
        EditorMediator editor = TextEditor.get().getEditor(item.getFile());
        if (editor == null) {
            return;
        }
        addEditorAnnotation(item,editor);
    }
    
    /** */
    private void removeEditorAnnotation(ToDoItem item) {
        EditorMediator editor = TextEditor.get().getEditor(item.getFile());
        if (editor != null && item.getEditorAnnotation() != null) {
            editor.removeAnnotation(item.getEditorAnnotation());
        }
        item.setEditorAnnotation(null);
    }
    
    /** */
    private void addEditorAnnotations(EditorMediator editor) {
    // for each item
        for (ToDoItem item:items) {
            addEditorAnnotation(item,editor);
        }
    }
    
    /**
     * Gets the class instance.
     *
     * @return The class instance.
     */
    public static ToDo get() {
        if (instance == null) {
            instance = new ToDo();
        }
        
        return instance;
    }    
}