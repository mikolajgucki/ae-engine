package com.andcreations.ae.studio.plugins.lua.explorer;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import javax.swing.KeyStroke;

import com.andcreations.ae.lua.parser.LuaFileInfo;
import com.andcreations.ae.lua.parser.LuaFunc;
import com.andcreations.ae.lua.parser.LuaParseException;
import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.lua.LuaIcons;
import com.andcreations.ae.studio.plugins.lua.explorer.GoToLuaItemCommon.GoToLine;
import com.andcreations.ae.studio.plugins.lua.parser.LuaParser;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.text.editor.EditorMediator;
import com.andcreations.ae.studio.plugins.text.editor.TextEditor;
import com.andcreations.ae.studio.plugins.text.editor.TextEditorAdapter;
import com.andcreations.ae.studio.plugins.text.editor.TextEditorViewHandler;
import com.andcreations.ae.studio.plugins.ui.common.UIKeys;
import com.andcreations.ae.studio.plugins.ui.common.quickopen.QuickOpenDialog;
import com.andcreations.ae.studio.plugins.ui.common.quickopen.QuickOpenItem;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButton;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButtonListener;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class GoToLuaItem {
    /** */
    private static GoToLuaItem instance;
    
    /** */
    private final BundleResources res = new BundleResources(GoToLuaItem.class);    
    
    /** */
    private GoToLuaItemCommon goToLuaItemCommon = new GoToLuaItemCommon();
    
    /** */
    private GoToLuaItem() {
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
    private void addTextEditorActions(final EditorMediator editor,
        TextEditorViewHandler viewHandler) {
    // go to Lua item action
        KeyStroke keyStroke = KeyStroke.getKeyStroke(
            KeyEvent.VK_G,UIKeys.menuKeyMask());
        ViewButton button = viewHandler.addSecondaryButton();
        button.setText(res.getStr("go.to.lua.item.text",
            UIKeys.keyStrokeToString(keyStroke)));
        button.setTooltip(res.getStr("go.to.lua.item.tooltip"));
        button.setIcon(Icons.getIcon(LuaIcons.LUA_FILE,
            DefaultIcons.DECO_POINTER));
        button.setAccelerator(keyStroke);        
        button.addViewButtonListener(new ViewButtonListener() {
            /** */
            @Override
            public void actionPerformed(ViewButton button) {
                showDialog(editor);
            }
        });  
    }    
    
    /** */
    private LuaFileInfo getLuaFileInfo(EditorMediator editor) {
    // parse
        LuaFileInfo luaFileInfo;
        try {
            luaFileInfo = LuaParser.get().parseLua(editor.getContent());
        } catch (LuaParseException exception) {
            CommonDialogs.error(res.getStr("parse.error"),
                res.getStr("failed.to.parse",editor.getFile().getAbsolutePath(),
                exception.getMessage()));
            return null;
        }        
        
        return luaFileInfo;
    }
    
    /** */
    private void goToLineRange(EditorMediator editor,int beginLine,
        int endLine) {
    //
        int marginLine = beginLine - 8;
        if (marginLine < 1) {
            marginLine = 1;
        }                
        
        editor.goToLineRange(marginLine,endLine);
        editor.goToLine(beginLine);        
    }
    
    /** */
    private void showDialog(EditorMediator editor) {
        File file = editor.getFile();
        if (LuaFile.isLuaFile(file) == false) {
            return;
        }
        
    // parse
        LuaFileInfo luaFileInfo = getLuaFileInfo(editor);
        if (luaFileInfo == null) {
            return;
        }
        
    // items
        List<QuickOpenItem> items = goToLuaItemCommon.createItems(luaFileInfo);
        
    // show dialog
        QuickOpenDialog dialog = new QuickOpenDialog(MainFrame.get(),
            res.getStr("title"),items,true);
        dialog.setMatcher(res.getStr("matcher.label"),new GoToLuaItemMatcher());
        dialog.showOptionDialog();      
        
    // go to the selected item
        QuickOpenItem selectedItem = dialog.getSelectedItem();
        if (selectedItem != null) {
            GoToLine goToLine = (GoToLine)selectedItem.getObject();
            goToLineRange(editor,goToLine.getBeginLine(),goToLine.getEndLine());
        } 
    }    
    
    /**
     * Goes to a Lua function in a given file. Does nothing if there is no such
     * function.
     *
     * @param luaFile The Lua file.
     * @param luaFuncName The Lua function name.
     */
    public void goToLuaFunc(File luaFile,String luaFuncName) {
    // open editor
        LuaFile.edit(luaFile);
        EditorMediator editor = TextEditor.get().getEditor(luaFile);
        
    // parse
        LuaFileInfo luaFileInfo = getLuaFileInfo(editor);
        if (luaFileInfo == null) {
            return;
        }        
        
    // go to the function
        for (LuaFunc func:luaFileInfo.getFuncs()) {
            String name = func.getName();
            if (name.equals(luaFuncName) == true ||
                name.endsWith("." + luaFuncName) == true ||
                name.endsWith(":" + luaFuncName) == true) {
            //
                goToLineRange(editor,func.getBeginLine(),func.getEndLine());
                break;
            }
        }            
    }
    
    /** */
    public static GoToLuaItem get() {
        if (instance == null) {
            instance = new GoToLuaItem();
        }
        
        return instance;
    }
}