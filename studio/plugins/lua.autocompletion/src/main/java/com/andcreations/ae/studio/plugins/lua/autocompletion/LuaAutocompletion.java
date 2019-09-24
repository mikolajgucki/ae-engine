package com.andcreations.ae.studio.plugins.lua.autocompletion;

import com.andcreations.ae.studio.plugins.lua.autocompletion.luadoc.CApiAutocompletion;
import com.andcreations.ae.studio.plugins.lua.autocompletion.luadoc.LuaCommentAutocompletion;
import com.andcreations.ae.studio.plugins.lua.autocompletion.luadoc.LuaDocAutocompletion;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.text.editor.EditorMediator;
import com.andcreations.ae.studio.plugins.text.editor.TextEditor;
import com.andcreations.ae.studio.plugins.text.editor.TextEditorAdapter;
import com.andcreations.ae.studio.plugins.text.editor.TextEditorViewHandler;

/**
 * @author Mikolaj Gucki
 */
class LuaAutocompletion {
    /** */
    private static LuaAutocompletion instance;
 
    /** */
    void init() {
        addTextEditorListener();
    }
    
    /** */
    private void addTextEditorListener() {
        TextEditor.get().addTextEditorListener(new TextEditorAdapter() {
            /** */
            @Override
            public void textEditorCreated(EditorMediator editor,
                TextEditorViewHandler viewHandler) {
            //
                if (ProjectLuaFiles.get().isLuaFile(editor.getFile())) {
                    install(editor);
                }
            } 
            
            /** */
            @Override
            public void textEditorClosed(EditorMediator editor) {
                LuaDocAutocompletion.get().remove(editor);
            }
        });
    }
    
    /** */
    private void install(final EditorMediator editor) {
    // Installing the autocompletion is a bit slow and thus we install it
    // in a separate thread. This way the editor shows up quicker.
        Runnable installer = new Runnable() {
            /** */
            @Override
            public void run() {
                ShorthandAutocompletion.get().install(editor);
                PredefinedAutocompletion.get().install(editor);
                LuaCommentAutocompletion.get().install(editor);
                CApiAutocompletion.get().install(editor);
                LuaDocAutocompletion.get().install(editor);
            }
        };
        Thread installerThread = new Thread(installer);
        installerThread.start();
    }
    
    /** */
    static LuaAutocompletion get() {
        if (instance == null) {
            instance = new LuaAutocompletion();
        }
        
        return instance;
    }
}