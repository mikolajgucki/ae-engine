package com.andcreations.ae.studio.plugins.lua.autocompletion.luadoc;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.andcreations.ae.lua.doc.LuaDocFileData;
import com.andcreations.ae.studio.plugins.file.FileAdapter;
import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.lua.parser.LuaParsedFile;
import com.andcreations.ae.studio.plugins.lua.parser.LuaParser;
import com.andcreations.ae.studio.plugins.lua.parser.LuaParserAdapter;
import com.andcreations.ae.studio.plugins.text.editor.EditorMediator;

/**
 * Provides the LuaDoc from the Lua files.
 *
 * @author Mikolaj Gucki
 */
public class LuaDocAutocompletion {
    /** */
    private static LuaDocAutocompletion instance;
    
    private Map<EditorMediator,LuaDocEditorAutocompletion> editors =
        new HashMap<EditorMediator,LuaDocEditorAutocompletion>();
        
    /** */
    public void init() {        
        addLuaParserListener();
        addFileListener();
    }
    
    /** */
    private void removeCompletions(File file) {
        synchronized (editors) {
            for (LuaDocEditorAutocompletion autocompl:editors.values()) {
                autocompl.removeCompletions(file);
            }
        }
    }
    
    /** */
    private void luaFileParsed(LuaParsedFile parsedFile) {
        synchronized (editors) {
        // remove
            removeCompletions(parsedFile.getFile());
            
            LuaDocFileData luaDocInfo = parsedFile.getLuaDocInfo();
        // add
            if (luaDocInfo != null) {
                for (LuaDocEditorAutocompletion autocompl:editors.values()) {
                    autocompl.addCompletions(parsedFile);
                }            
            }
        }
    }
    
    /** */
    private void addLuaParserListener() {
        LuaParser.get().addLuaParserListener(new LuaParserAdapter() {
            /** */
            @Override
            public void luaFileParsed(LuaParsedFile parsedFile) {
                LuaDocAutocompletion.this.luaFileParsed(parsedFile);
            }            
        });
    }
    
    /** */
    private void addFileListener() {
        Files.get().addFileListener(new FileAdapter() {
            /** */
            @Override
            public void fileDeleted(File file) {
                removeCompletions(file);
            }
            
            /** */            
            @Override
            public void fileRenamed(File src,File dst) {
                removeCompletions(src);
            }
        });
    }
    
    /** */
    public void install(EditorMediator editor) {
        synchronized (editors) {
            LuaDocEditorAutocompletion editorAutocompletion =
                new LuaDocEditorAutocompletion(editor.getFile());
            editors.put(editor,editorAutocompletion);
            editor.getAutocompletion().addSource(
                editorAutocompletion.getSource());
        }
    }
    
    /** */
    public void remove(EditorMediator editor) {
        synchronized (editors) {
            editors.remove(editor);
        }
    }
        
    /** */
    public static LuaDocAutocompletion get() {
        if (instance == null) {
            instance = new LuaDocAutocompletion();
        }
        
        return instance;
    }
}