package com.andcreations.ae.studio.plugins.lua.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.andcreations.ae.lua.doc.LuaDocError;
import com.andcreations.ae.lua.doc.LuaDocFileData;
import com.andcreations.ae.lua.doc.LuaDocFunc;
import com.andcreations.ae.lua.doc.LuaDocLuaFileParser;
import com.andcreations.ae.lua.doc.LuaDocModule;
import com.andcreations.ae.lua.doc.LuaDocParseContext;
import com.andcreations.ae.lua.doc.LuaDocVar;
import com.andcreations.ae.lua.doc.LuaDocWarning;
import com.andcreations.ae.lua.parser.LuaFileInfo;
import com.andcreations.ae.lua.parser.LuaFileParser;
import com.andcreations.ae.lua.parser.LuaParseException;
import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.file.FileAdapter;
import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.file.cache.TextFileCache;
import com.andcreations.ae.studio.plugins.lua.LuaFile;
import com.andcreations.ae.studio.plugins.project.ProjectLuaFiles;
import com.andcreations.ae.studio.plugins.text.editor.EditorMediator;
import com.andcreations.ae.studio.plugins.text.editor.TextEditor;
import com.andcreations.ae.studio.plugins.text.editor.TextEditorAdapter;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.io.FileNode;
import com.andcreations.io.FileTree;

/**
 * @author Mikolaj Gucki
 */
public class LuaParser {
    /** */
    private static LuaParser instance;
    
    /** */
    private LuaParserRunnable parserRunnable;
    
    /** */
    private Thread parserThread;
    
    /** */
    private Map<File,LuaParsedFile> files = new HashMap<File,LuaParsedFile>();
    
    /** */
    private List<LuaParserListener> listeners = new ArrayList<>();
    
    /** */
    private LuaFileParser luaFileParser = new LuaFileParser();  
    
    /** */
    private LuaDocLuaFileParser luaDocFileParser = new LuaDocLuaFileParser();
    
    /** */
    private LuaParserProblems luaParserProblems = new LuaParserProblems();
    
    /** */
    private LuaParser() {
        create();
    }
    
    /** */
    private void create() {
    // file listener
        FileAdapter fileListener = new FileAdapter() {
            /** */
            @Override    
            public void fileCreated(File file) {
                LuaParser.this.fileCreated(file);
            }            
            
            /** */
            @Override    
            public void fileChanged(File file) {
                LuaParser.this.fileChanged(file);
            }            
            
            /** */
            @Override
            public void fileDeleted(File file) {
                LuaParser.this.fileDeleted(file);                
            }
            
            /** */
            @Override
            public void fileRenamed(File src,File dst) {
                fileDeleted(src);
                fileCreated(dst);
            }
        };        
        Files.get().addFileListener(fileListener);              
        
    // text editor listener
        TextEditor.get().addTextEditorListener(new TextEditorAdapter() {
            /** */
            @Override        
            public void textRecentlyChanged(EditorMediator editor) {
                LuaParser.this.fileChanged(
                    editor.getFile(),editor.getContent());
            }
        });
        
    // runnable
        parserRunnable = new LuaParserRunnable();
    }
    
    /** */
    private void fileChanged(File file,String src) {
        if (LuaFile.isLuaFile(file) == false) {
            return;
        }         
        parserRunnable.add(file,src);
    }
    
    /** */
    private void fileChanged(File file) {
        fileChanged(file,null);
    }

    /** */
    private void fileCreated(File file) {
        if (LuaFile.isLuaFile(file) == false) {
            return;
        }
        parserRunnable.add(file);
    }
    
    /** */
    private void fileDeleted(File file) {
        luaParserProblems.clear(file);
    }    
    
    /** */
    void put(LuaParsedFile parsedFile) {
        synchronized (files) {
            files.put(parsedFile.getFile(),parsedFile);
        }
    }
    
    /** */
    private void notifyLuaFileParsed(LuaParsedFile parsedFile) {
        synchronized (listeners) {
            for (LuaParserListener listener:listeners) {
                listener.luaFileParsed(parsedFile);
            }
        }
    }
    
    /** */
    private void notifyLuaFileParsingFailed(File file,String message) {
        synchronized (listeners) {
            for (LuaParserListener listener:listeners) {
                listener.luaFileParsingFailed(file,message);
            }
        }
    }
    
    /** */
    private LuaDocFileData parseLuaDoc(final File file,LuaFileInfo info) {
    // module
        String luaModule = ProjectLuaFiles.get().getLuaModule(file);
        LuaDocModule module = new LuaDocModule();
        module.setName(luaModule);
        
    // file data
        LuaDocFileData fileData = new LuaDocFileData();
        fileData.setModule(module);
        
    // context
        LuaDocParseContext context = new LuaDocParseContext();
        context.setFileData(fileData);     
        
    // parse
        fileData = luaDocFileParser.parse(info.getElements(),context);
        
    // defined in
        String definedIn = luaModule.replace('.','/') + ".lua";
        for (LuaDocFunc func:fileData.getFuncs()) {
            func.setDefinedIn(definedIn);
        }
        for (LuaDocVar var:fileData.getVars()) {
            var.setDefinedIn(definedIn);
        }
        
        return fileData;
    }       
    
    /** */
    public LuaFileInfo parseLua(String src) throws LuaParseException {
        return luaFileParser.parse(src);
    }
    
    /** */
    public LuaParsedFile parse(File file,String src)
        throws IOException,LuaParseException {            
    // load if no source provided
        if (src == null) {
            src = TextFileCache.get().read(file);
        }

    // parse
        LuaFileInfo info = luaFileParser.parse(src); 
        
    // parse LuaDoc
        LuaDocFileData luaDocData = parseLuaDoc(file,info);
        
    // parse result
        return new LuaParsedFile(file,info,luaDocData);
    }    
    
    /** */
    void cache(final File file,String src) {
        Log.trace(String.format("Parsing %s",file.getAbsolutePath()));
        
    // parse
        LuaParsedFile parsedFile;
        try {
            parsedFile = parse(file,src);
        } catch (Exception exception) {
            luaParserProblems.handleParseError(file,exception);
            notifyLuaFileParsingFailed(file,exception.getMessage());
            return;            
        }
        
        final LuaDocFileData fileData = parsedFile.getLuaDocInfo();
    // Process all the issues in one shot in the AWT thread. Otherwise
    // the gutter icons would flicker.
        UICommon.invoke(new Runnable() {
            /** */
            @Override
            public void run() {
                luaParserProblems.clear(file);
            // issues
                for (LuaDocWarning warning:fileData.getWarnings()) {
                    luaParserProblems.addWarning(file,warning.getMessage(),
                        warning.getLine());
                }
                for (LuaDocError error:fileData.getErrors()) {
                    luaParserProblems.addWarning(file,error.getMessage(),
                        error.getLine());
                }
            }
        });
        
    // keep
        files.put(file,parsedFile);
        notifyLuaFileParsed(parsedFile);
    }
        
    /** */
    private void parseAllLuaFiles() {
        Log.trace("Parsing all Lua files");
        FileTree tree = ProjectLuaFiles.get().getLuaSourceTree();
        List<FileNode> nodes = tree.flatten();
    // for each Lua file
        for (FileNode node:nodes) {
            if (node.isFile() && node.getFileCount() == 1) {
                cache(node.getFile(),null);
            }
        }
    }    
    
    /** */
    synchronized void start() {
        Log.info("Starting the Lua parser thread");
        
    // thread
        parserThread = new Thread(parserRunnable,"LuaParser");
        parserThread.start();
        
    // all files
        parseAllLuaFiles();
    }
    
    /** */
    synchronized void stop() {
        if (parserThread == null) {
            return;
        }
        
        parserRunnable.stop();
        try {
            parserThread.join();
        } catch (InterruptedException exception) {
        }
        Log.info("Stopped the Lua parser thread");        
    }
    
    /** */
    public void addLuaParserListener(LuaParserListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    /** */
    public Collection<LuaParsedFile> getParsedFiles() {
        return Collections.unmodifiableCollection(files.values());
    }
    
    /** */
    public static LuaParser get() {
        if (instance == null) {
            instance = new LuaParser();
        }
        
        return instance;
    }
}