package com.andcreations.ae.studio.plugins.lua.debug.breakpoints;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.lua.debug.LuaBreakpointState;
import com.andcreations.ae.studio.plugins.lua.debug.LuaDebugIcons;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.text.editor.EditorAnnotation;
import com.andcreations.ae.studio.plugins.text.editor.EditorMediator;
import com.andcreations.ae.studio.plugins.text.editor.GutterIcon;
import com.andcreations.ae.studio.plugins.text.editor.TextEditor;
import com.andcreations.ae.studio.plugins.text.editor.TextEditorAdapter;
import com.andcreations.ae.studio.plugins.text.editor.TextEditorViewHandler;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.common.UIKeys;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.ae.studio.plugins.ui.main.view.DefaultViewProvider;
import com.andcreations.ae.studio.plugins.ui.main.view.SingleViewFactory;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButton;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButtonListener;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCategory;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class LuaBreakpoints {
    /** The view identifier. */
    private static final String VIEW_ID =
        "com.andcreations.ae.studio.plugins.lua.debug.breakpoints";        
    
    /** */
    private static LuaBreakpoints instance;

    /** */
    private BundleResources res = new BundleResources(LuaBreakpoints.class);      
    
    /** */
    private List<LuaBreakpointsListener> listeners = new ArrayList<>();
    
    /** */
    private List<LuaBreakpoint> breakpoints = new ArrayList<>();
    
    /** */
    private LuaBreakpointsViewComponent component;
    
    /** */
    public void init(List<LuaBreakpointState> initBreakpoints) {
        TextEditor.get().addTextEditorListener(new TextEditorAdapter() {
            /** */
            @Override
            public void textEditorCreated(EditorMediator editor,
                TextEditorViewHandler viewHandler) {
            //
                if (ProjectLuaFiles.get().isLuaFile(editor.getFile())) {
                    addGutterIcons(editor);
                    addTextEditorActions(editor,viewHandler);
                }
            }
            
            /** */
            @Override
            public void textEditorClosed(EditorMediator editor) {
                if (ProjectLuaFiles.get().isLuaFile(editor.getFile())) {
                    clearGutterIcons(editor);
                }
            }
            
            /** */
            @Override
            public void gutterIconRemoved(EditorMediator editor,
                GutterIcon gutterIcon) {
            //
                LuaBreakpoint breakpoint = findBreakpoint(gutterIcon);
                if (breakpoint != null) {
                    breakpoints.remove(breakpoint);
                    breakpointsChanged();                        
                }
            }
            
            /** */
            @Override
            public void gutterIconLinesUpdated(EditorMediator editor) {
                for (LuaBreakpoint breakpoint:breakpoints) {
                    if (breakpoint.getEditor() == editor) {
                        int oldLine = breakpoint.updateLine();
                        breakpointsChanged();
                        synchronized (listeners) {
                            for (LuaBreakpointsListener listener:listeners) {
                                listener.breakpointLineChanged(
                                    breakpoint,oldLine);
                            }
                        }                        
                    }
                }
            }
        });
        
        initUI();
        setInitBreakpoints(initBreakpoints);
    }
    
    /** */
    private void initUI() {
    // component listener
        final LuaBreakpointsViewComponentListener componentListener =
            new LuaBreakpointsViewComponentListener() {
                /** */
                @Override
                public void breakpointStateChanged(LuaBreakpoint breakpoint) {
                    removeGutterIcon(breakpoint);
                    addGutterIcon(breakpoint);
                    synchronized (listeners) {
                        for (LuaBreakpointsListener listener:listeners) {
                            listener.breakpointStateChanged(breakpoint);
                        }
                    }
                }

                /** */
                @Override
                public void removeBreakpoint(LuaBreakpoint breakpoint) {
                    LuaBreakpoints.this.removeBreakpoint(breakpoint);
                    breakpointsChanged();
                }
            };
        
    // component
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                component = new LuaBreakpointsViewComponent(componentListener);
            }
        });

    // create the disabled Lua breakpoint icon in advance to pick name later on
        Icons.getDisabledIcon(LuaDebugIcons.LUA_BREAKPOINT);
        
        String title = res.getStr("view.title");
        ImageIcon icon = Icons.getIcon(LuaDebugIcons.LUA_BREAKPOINTS);
        
    // view factory
        SingleViewFactory factory = new SingleViewFactory(
            VIEW_ID,component,title,icon);
        factory.setViewCategory(ViewCategory.DETAILS);
        factory.addViewDecorator(new LuaBreakpointsViewActions());
        MainFrame.get().getViewManager().registerViewFactory(factory);
        
    // view provider
        DefaultViewProvider provider = new DefaultViewProvider(
            title,icon,factory);
        MainFrame.get().getViewManager().addViewProvider(provider);        
    }
    
    /** */
    public void addLuaBreakpointsListener(LuaBreakpointsListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    public void removeLuaBreakpointsListener(LuaBreakpointsListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }
    
    /** */
    private void setInitBreakpoints(List<LuaBreakpointState> initBreakpoints) {
        for (LuaBreakpointState initBreakpoint:initBreakpoints) {
            File file = ProjectLuaFiles.get().getFileByPath(
                initBreakpoint.getSource());
            if (file != null) {
                breakpoints.add(new LuaBreakpoint(file,
                    initBreakpoint.getSource(),initBreakpoint.getLine(),
                    initBreakpoint.getEnabled()));
            }
        }
        breakpointsChanged();
    }    
    
    /** */
    private void addGutterIcon(EditorMediator editor,LuaBreakpoint breakpoint) {
        String iconName = breakpoint.isEnabled() ?
            LuaDebugIcons.LUA_BREAKPOINT :
            Icons.getDisabledIconName(LuaDebugIcons.LUA_BREAKPOINT);
        
        GutterIcon gutterIcon = null;
        try {
            gutterIcon = editor.addGutterIcon(breakpoint.getLine(),
                iconName,"",EditorAnnotation.Priority.HIGH);
        } catch (BadLocationException exception) {
        }
        breakpoint.setGutterIcon(gutterIcon);
        breakpoint.setEditor(editor);                
    }
    
    /** */
    private void addGutterIcon(LuaBreakpoint breakpoint) {
        if (breakpoint.getEditor() == null) {
            return;
        }
        addGutterIcon(breakpoint.getEditor(),breakpoint);
    }
    
    /** */
    private void removeGutterIcon(LuaBreakpoint breakpoint) {
        if (breakpoint.getEditor() == null) {
            return;
        }
        
        try {
            breakpoint.getEditor().removeGutterIcon(breakpoint.getGutterIcon());
        } catch (BadLocationException exception) {
        }
    }
    
    /** */
    private void addGutterIcons(EditorMediator editor) {
        for (LuaBreakpoint breakpoint:breakpoints) {
            if (breakpoint.getFile().equals(editor.getFile()) == true) {
                addGutterIcon(editor,breakpoint);
            }
        }
    }
    
    /** */
    private void clearGutterIcons(EditorMediator editor) {
        for (LuaBreakpoint breakpoint:breakpoints) {
            if (breakpoint.getEditor() == editor) {
                breakpoint.setGutterIcon(null);
                breakpoint.setEditor(null);
            }
        }
    }    
    
    /** */
    private void addTextEditorActions(final EditorMediator editor,
        TextEditorViewHandler viewHandler) {
    // toggle breakpoint action
        KeyStroke toggleStroke = KeyStroke.getKeyStroke(
            KeyEvent.VK_B,UIKeys.menuKeyMask());
        ViewButton toggleButton = viewHandler.addSecondaryButton();
        toggleButton.setText(res.getStr("toggle.breakpoint.text",
    		UIKeys.keyStrokeToString(toggleStroke)));
        toggleButton.setTooltip(res.getStr("toggle.breakpoint.tooltip"));
        toggleButton.setIcon(Icons.getIcon(LuaDebugIcons.LUA_BREAKPOINT));
        toggleButton.setAccelerator(toggleStroke);        
        toggleButton.addViewButtonListener(new ViewButtonListener() {
            /** */
            @Override
            public void actionPerformed(ViewButton button) {
                toggleBreakpoint(editor);
            }
        });
    }    
    
    /** */
    private LuaBreakpoint findBreakpoint(File file,int line) {
        for (LuaBreakpoint breakpoint:breakpoints) {
            if (breakpoint.getFile().equals(file) == true &&
                breakpoint.getLine() == line) {
            //
                return breakpoint;
            }
        }
        
        return null;
    }
    
    /** */
    private LuaBreakpoint findBreakpoint(GutterIcon gutterIcon) {
        for (LuaBreakpoint breakpoint:breakpoints) {
            if (breakpoint.getGutterIcon() == gutterIcon) {
                return breakpoint;
            }
        }
        
        return null;
    }    
    
    /** */
    public LuaBreakpoint findBreakpoint(String source,int line) {
        for (LuaBreakpoint breakpoint:breakpoints) {
            if (breakpoint.getSource().equals(source) == true &&
                breakpoint.getLine() == line) {
            //
                return breakpoint;
            }
        }
        
        return null;        
    }
    
    /** */
    private boolean addBreakpoint(LuaBreakpoint breakpoint) {
        Log.trace(String.format("LuaBreakpoints.addBreakpoint(%s:%d)",
            breakpoint.getSource(),breakpoint.getLine()));
        
        synchronized (listeners) {
            for (LuaBreakpointsListener listener:listeners) {
                if (listener.breakpointAdded(breakpoint) == false) {
                    return false;
                }
            }
            breakpoints.add(breakpoint);
        }
        
        return true;
    }
    
    /** */
    private boolean removeBreakpoint(LuaBreakpoint breakpoint) {
        Log.trace(String.format("LuaBreakpoints.removeBreakpoint(%s:%d)",
            breakpoint.getSource(),breakpoint.getLine()));
        
        synchronized (listeners) {
            for (LuaBreakpointsListener listener:listeners) {
                if (listener.breakpointRemoved(breakpoint) == false) {
                    return false;
                }
            }
            
            breakpoints.remove(breakpoint); 
            EditorMediator editor = breakpoint.getEditor();
            if (editor != null) {
                try {
                    editor.removeGutterIcon(breakpoint.getGutterIcon());
                } catch (BadLocationException exception) {
                }            
            }
        }
     
        breakpointsChanged();
        return true;
    }
    
    /** */
    private void toggleBreakpoint(EditorMediator editor) {
        File file = editor.getFile();
        if (ProjectLuaFiles.get().isLuaFile(file) == false) {
            return;
        }
        
        int line = editor.getLine();
        String source = ProjectLuaFiles.get().getPath(file);
        Log.trace(String.format("LuaBreakpoints.toggleBreakpoint(%s:%d)",
            source,line));
        
        LuaBreakpoint breakpoint = findBreakpoint(file,line);
        if (breakpoint == null) {
            breakpoint = new LuaBreakpoint(file,source,line);
            if (addBreakpoint(breakpoint) == false) {
                return;
            }
            addGutterIcon(editor,breakpoint);
            breakpointsChanged();
        }
        else {
            if (removeBreakpoint(breakpoint) == false) {
                return;
            }
        }
    }
    
    /** */
    void deleteSelected() {
        List<LuaBreakpoint> selectedBreakpoints =
            component.getSelectedBreakpoints(); 
        for (LuaBreakpoint breakpoint:selectedBreakpoints) {
            removeBreakpoint(breakpoint);
        }            
    }
    
    /** */
    void deleteAll() {
        List<LuaBreakpoint> allBreakpoints = new ArrayList<>(breakpoints);
        for (LuaBreakpoint breakpoint:allBreakpoints) {
            removeBreakpoint(breakpoint);
        }        
    }
    
    /** */
    private void breakpointsChanged() {
        component.updateModel(breakpoints);
    }
    
    /** */
    public List<LuaBreakpoint> all() {
        return breakpoints;
    }
    
    /** */
    public static LuaBreakpoints get() {
        if (instance == null) {
            instance = new LuaBreakpoints();
        }
        
        return instance;
    }
}