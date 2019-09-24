package com.andcreations.ae.studio.plugins.text.editor;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;

import com.andcreations.ae.studio.plugins.file.EditedFile;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.common.UIKeys;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.ae.studio.plugins.ui.main.view.View;

/**
 * @author Mikolaj Gucki
 */
public class TextEditor {
    /** */
    private static TextEditor instance;
    
    /** */
    private TextEditorState state;
    
    /** */
    private TextEditorViewFactory viewFactory;
    
    /** */
    private TextEditorCache cache;
    
    /** */
    private List<EditorMediator> mediators = new ArrayList<>();
    
    /** The mediator (component) which currently holds the focus. */
    private EditorMediator focusMediator;
    
    /** The listeners. */
    private List<TextEditorListener> listeners = new ArrayList<>();
    
    /** The key listeners. */
    private Map<KeyStroke,TextEditorKeyListener> keyListeners = new HashMap<>();
    
    /** */
    private TextChangedTimer changedTimer;
    
    /** */
    private Thread changedTimerThread;
    
    /** */
    private TextEditorFindDialog findDialog;
    
    /** */
    private TextEditorReplaceDialog replaceDialog;
    
    /** */
    void init(TextEditorState state) {
        this.state = state;        
        create();
    }
    
    /**
     * Called when the plugin is stopping.
     */
    void stop() {
        changedTimer.stop();
        try {
            changedTimerThread.join();
        } catch (InterruptedException exception) {
        }
    }
    
    /** */
    private void create() {
        cache = new TextEditorCache();
        AELuaTokenMaker.install();
        state.init();
        initLineHighlights();
        createChangedTimer();
        createFindDialog();
        createReplaceDialog();
        createViewFactory();
    }    
    
    /** */
    private void addLineHighlight(EditorMediator mediator,
        LineHighlight highlight) {
    //
        try {
            highlight.setTag(mediator.getComponent().addLineHighlight(
                highlight.getLine(),highlight.getColor()));   
        } catch (BadLocationException exception) {
        }
        mediator.getComponent().updateCurrentLineHighlight();
    }
    
    /** */
    private void initLineHighlights() {
        LineHighlights.get().init(new LineHighlightsListener() {
            /** */
            @Override
            public void lineHighlightAdded(LineHighlight highlight) {
                EditorMediator mediator = getEditor(highlight.getFile());
                if (mediator != null) {
                    addLineHighlight(mediator,highlight);
                }
            }
            
            /** */
            @Override
            public void lineHighlightRemoved(LineHighlight highlight) {
                EditorMediator mediator = getEditor(highlight.getFile());
                if (mediator != null) {
                    mediator.getComponent().removeLineHighlight(
                        highlight.getTag());
                    highlight.setTag(null);
                }
            }            
        });
    }
    
    /** */
    private void createChangedTimer() {
        changedTimer = new TextChangedTimer(new TextChangedTimerListener() {
            /** */
            @Override
            public void textChangedTimerFired(EditorMediator mediator) {
                synchronized (listeners) {
                    for (TextEditorListener listener:listeners) {
                        synchronized (TextEditor.this) {
                            listener.textRecentlyChanged(mediator);
                        }
                    }
                }                                
            }
        });
        changedTimerThread = new Thread(changedTimer,"TextEditorChangedTimer");
        changedTimerThread.start();
    }
    
    /** */
    private void createFindDialog() {
        UICommon.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                findDialog = new TextEditorFindDialog(MainFrame.get());
            }
        });
    }
    
    /** */
    private void createReplaceDialog() {
        UICommon.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                replaceDialog = new TextEditorReplaceDialog(MainFrame.get());
            }
        });
    }
    
    /** */
    private void createViewFactory() {
        TextEditorViewFactoryListener listener =
            new TextEditorViewFactoryListener() {
                /** */
                @Override
                public void textEditorCreated(EditorMediator mediator,
                    TextEditorViewHandler viewHandler) {
                //
                    TextEditor.this.textEditorCreated(mediator,viewHandler);
                }
            };
        
        viewFactory = new TextEditorViewFactory(listener,state,cache);
        MainFrame.get().getViewManager().registerViewFactory(viewFactory);
    }
    
    
    /** */
    private View createView(EditorCfg cfg) {
        state.addCfg(cfg);
        return viewFactory.createView(cfg.getId());
    }
    
    /**
     * Opens a view which allows to edit a text file.
     *
     * @param cfg The configuration.
     */
    public void edit(EditorCfg cfg) {
    // view
        View view = cache.get(cfg);
        if (view == null) {        
            view = createView(cfg);
        }
        
    // show view
        MainFrame.get().getViewManager().showView(view);
        
    // go to 
        EditorComponent comp = (EditorComponent)view.getComponent();
        if (cfg.hasLineRange() == false) {
            if (cfg.hasLine() == true) {
                comp.goToLine(cfg.getLine(),cfg.getLineOffset());
            }
            else {
                comp.goToLine(1);
            }
        }
        else {
            comp.goToLineRange(cfg.getStartLine(),cfg.getEndLine(),
                cfg.getLineOffset());
        }
    }    
    
    /** */
    public EditorMediator getEditor(File file) {
        for (EditorMediator mediator:mediators) {
            if (mediator.getFile().equals(file) == true) {
                return mediator;
            }
        }
        
        return null;
    }
    
    /** */
    void setFocusMediator(EditorMediator newFocusMediator) {
    // notify lost
        if (focusMediator != null) {
            synchronized (listeners) {
                synchronized (this) {
                    for (TextEditorListener listener:listeners) {
                        listener.focusLost(focusMediator);
                    }
                }
            }
        }
        
    // set
        this.focusMediator = newFocusMediator;
        if (newFocusMediator != null) {
            EditedFile.get().setLastEditedFile(newFocusMediator.getFile());
        }
        
    // notify gained
        if (focusMediator != null) {
            synchronized (listeners) {
                for (TextEditorListener listener:listeners) {
                    listener.focusGained(focusMediator);
                }
            }
        }        
    }
    
    /** */
    EditorMediator getFocusMediator() {
        return focusMediator;
    }

    /** */
    void textChanged(EditorMediator mediator) {
        changedTimer.startCounting(mediator);
    }
    
    /** */
    void fileSaved(EditorMediator mediator) {
        synchronized (listeners) {
            for (TextEditorListener listener:listeners) {
                listener.fileSaved(mediator);
            }
        }
    }
    
    /** */
    private void textEditorCreated(EditorMediator mediator,
        TextEditorViewHandler viewHandler) {
    //
        synchronized (mediators) {
            mediators.add(mediator);
            
        // highlights
            List<LineHighlight> editorHighlights =
                LineHighlights.get().getHighlights(mediator.getFile());
            for (LineHighlight highlight:editorHighlights) {
                addLineHighlight(mediator,highlight);
            }
            
        // notify
            synchronized (listeners) {
                for (TextEditorListener listener:listeners) {
                    listener.textEditorCreated(mediator,viewHandler);
                }
            }                        
        }        
    }    
    
    /** */
    void textEditorClosed(EditorMediator mediator) {
        synchronized (mediators) {
            mediators.remove(mediator);
            
        // notify
            synchronized (listeners) {
                for (TextEditorListener listener:listeners) {
                    listener.textEditorClosed(mediator);
                }
            }
        }
    }
    
    /** */
    void showFindDialog() {
        findDialog.setVisible(true);
    }
    
    /** */
    void showReplaceDialog() {
        replaceDialog.setVisible(true);
    }
    
    /** */
    void clearAllMarkAllSelections() {
        synchronized (mediators) {
            for (EditorMediator mediator:mediators) {
                mediator.getComponent().clearMarkAllSelection();
            }
        }
    }
    
    /** */
    public void addTextEditorListener(TextEditorListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    public void addKeyListener(KeyStroke keyStroke,
        TextEditorKeyListener listener) {
    //
        synchronized (keyListeners) {
            keyListeners.put(keyStroke,listener);
        }
    }
    
    /** */
    boolean notifyKeyPressed(KeyEvent event) {
        EditorMediator focusMediator = getFocusMediator();
        if (focusMediator == null) {
            return false;
        }
        
        synchronized (keyListeners) {
            for (KeyStroke stroke:keyListeners.keySet()) {
                if (event.getKeyCode() == stroke.getKeyCode() &&
                    UIKeys.testModifiersEx(event,stroke) == true) {
                //
                    keyListeners.get(stroke).textEditorKeyPressed(
                        focusMediator,event);
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /** */
    boolean notifyKeyReleased(KeyEvent event) {
        EditorMediator focusMediator = getFocusMediator();
        if (focusMediator == null) {
            return false;
        }
            
        synchronized (keyListeners) {
            for (KeyStroke stroke:keyListeners.keySet()) {
                if (event.getKeyCode() == stroke.getKeyCode() &&
                    UIKeys.testModifiersEx(event,stroke) == true) {
                //
                    keyListeners.get(stroke).textEditorKeyReleased(
                        focusMediator,event);
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /** */
    boolean notifyKeyTyped(KeyEvent event) {
        EditorMediator focusMediator = getFocusMediator();
        if (focusMediator == null) {
            return false;
        }
        
        synchronized (keyListeners) {
            for (KeyStroke stroke:keyListeners.keySet()) {
                if (event.getKeyCode() == stroke.getKeyCode() &&
                    UIKeys.testModifiersEx(event,stroke) == true) {
                //
                    keyListeners.get(stroke).textEditorKeyTyped(
                        focusMediator,event);
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /** */
    void gutterIconRemoved(EditorMediator mediator,GutterIcon gutterIcon) {
        synchronized (listeners) {
            for (TextEditorListener listener:listeners) {
                listener.gutterIconRemoved(mediator,gutterIcon);
            }
        }
    }
    
    /** */
    void gutterIconLinesUpdated(EditorMediator mediator) {
        synchronized (listeners) {
            for (TextEditorListener listener:listeners) {
                listener.gutterIconLinesUpdated(mediator);
            }
        }
    }
    
    /** */
    public List<EditorMediator> getDirtyEditors() {
        List<EditorMediator> dirtyMediators = new ArrayList<>();
    // for each editor
        for (EditorMediator mediator:mediators) {
            if (mediator.isDirty() == true) {
                dirtyMediators.add(mediator);
            }
        }
        
        return dirtyMediators;
    }
    
    /** */
    public boolean hasDirtyEditors() {
        for (EditorMediator mediator:mediators) {
            if (mediator.isDirty() == true) {
            	return true;
            }
        }
    	return false;
    }
    
    /** */
    public static TextEditor get() {
        if (instance == null) {
            instance = new TextEditor();
        }
        
        return instance;
    }
}