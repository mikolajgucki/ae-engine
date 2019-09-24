package com.andcreations.ae.studio.plugins.lua.editor;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.text.editor.EditorMediator;
import com.andcreations.ae.studio.plugins.text.editor.TextEditor;
import com.andcreations.ae.studio.plugins.text.editor.TextEditorAdapter;
import com.andcreations.ae.studio.plugins.text.editor.TextEditorViewHandler;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.common.UIKeys;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButton;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButtonListener;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class LuaUTF8 {
    /** */
    private static LuaUTF8 instance;
    
    /** */
    private BundleResources res = new BundleResources(LuaUTF8.class);
        
    /** */
    private LuaUTF8Dialog luaUtf8Dialog;
    
    /** */
    private LuaUTF8() {
        create();
    }
    
    /** */
    private void create() {
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                luaUtf8Dialog = new LuaUTF8Dialog();
            }
        });         
    }
    
    /** */
    private void showUTF8Dialog(EditorMediator editor) {
    // selection
        String text = editor.getSelectedText();
        int start = editor.getSelectionStart();
        int end = editor.getSelectionEnd();
        
    // show dialog
        if (luaUtf8Dialog.showDialog(text) == true) {
            String luaString = luaUtf8Dialog.getLuaString();
        // either insert or replace
            if (text == null || text.length() == 0) {
                editor.insertAtCaret(luaString);
            }
            else {
                editor.replace(start,end,luaString);
            }
        }
    }
    
    /** */
    private void addAction(final EditorMediator editor,
        TextEditorViewHandler viewHandler) {
    //
        KeyStroke keyStroke = KeyStroke.getKeyStroke(
            KeyEvent.VK_8,UIKeys.menuKeyMask());
        ViewButton button = viewHandler.addSecondaryButton();
        button.setText(res.getStr("open.utf8.dialog.text",
            UIKeys.keyStrokeToString(keyStroke)));
        button.setTooltip(res.getStr("open.utf8.dialog.tooltip"));
        button.setIcon(Icons.getIcon(DefaultIcons.AZ));
        button.setAccelerator(keyStroke);        
        button.addViewButtonListener(new ViewButtonListener() {
            /** */
            @Override
            public void actionPerformed(ViewButton button) {
                showUTF8Dialog(editor);
            }
        });        
    }
    
    /** */
    void init() {
        TextEditor.get().addTextEditorListener(new TextEditorAdapter() {
            /** */
            @Override
            public void textEditorCreated(EditorMediator editor,
                TextEditorViewHandler viewHandler) {
            //
                if (ProjectLuaFiles.get().isLuaFile(editor.getFile())) {
                    addAction(editor,viewHandler);
                }
            }
        });        
    }
    
    /** */
    static LuaUTF8 get() {
        if (instance == null) {
            instance = new LuaUTF8();
        }
        
        return instance;
    }
}