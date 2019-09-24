package com.andcreations.ae.studio.plugins.lua.templates;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.andcreations.ae.studio.plugins.lua.LuaIcons;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.text.editor.EditorMediator;
import com.andcreations.ae.studio.plugins.text.editor.TextEditor;
import com.andcreations.ae.studio.plugins.text.editor.TextEditorAdapter;
import com.andcreations.ae.studio.plugins.text.editor.TextEditorViewHandler;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.common.UIKeys;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButton;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButtonListener;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class InsertFuncTemplate {
    /** */
    private BundleResources res =
        new BundleResources(InsertFuncTemplate.class);  
    
    /** */
    private InsertFuncDialog dialog;    
    
    /** */
    InsertFuncTemplate() {
        create();
    }
    
    /** */
    private void create() {
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                dialog = new InsertFuncDialog();
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
                    addTextEditorActions(editor,viewHandler);
                }
            }
        });
    }
    
    /** */
    private void addInsertLocalFuncAction(final EditorMediator editor,
        TextEditorViewHandler viewHandler) {
    //
        KeyStroke keyStroke = KeyStroke.getKeyStroke(
            KeyEvent.VK_I,UIKeys.menuKeyMask());
        ViewButton button = viewHandler.addSecondaryButton();
        button.setText(res.getStr("insert.local.func.text",
            UIKeys.keyStrokeToString(keyStroke)));
        button.setTooltip(res.getStr("insert.local.func.tooltip"));
        button.setIcon(Icons.getIcon(LuaIcons.LUA_FUNC_LOCAL));
        button.setAccelerator(keyStroke);        
        button.addViewButtonListener(new ViewButtonListener() {
            /** */
            @Override
            public void actionPerformed(ViewButton button) {
                dialog.showDialog(editor,true);
            }
        });  
    }

        
    /** */
    private void addInsertFuncAction(final EditorMediator editor,
        TextEditorViewHandler viewHandler) {
    //
        KeyStroke keyStroke = KeyStroke.getKeyStroke(
            KeyEvent.VK_U,UIKeys.menuKeyMask());
        ViewButton button = viewHandler.addSecondaryButton();
        button.setText(res.getStr("insert.func.text",
            UIKeys.keyStrokeToString(keyStroke)));
        button.setTooltip(res.getStr("insert.func.tooltip"));
        button.setIcon(Icons.getIcon(LuaIcons.LUA_FUNC));
        button.setAccelerator(keyStroke);        
        button.addViewButtonListener(new ViewButtonListener() {
            /** */
            @Override
            public void actionPerformed(ViewButton button) {
                dialog.showDialog(editor,false);
            }
        });  
    }

    
    /** */
    private void addTextEditorActions(EditorMediator editor,
        TextEditorViewHandler viewHandler) {
    //
        addInsertLocalFuncAction(editor,viewHandler);
        addInsertFuncAction(editor,viewHandler);
    }    
}