package com.andcreations.ae.lua.doc;

import java.io.File;
import java.io.IOException;

import com.andcreations.ae.lua.parser.LuaParseException;
import com.andcreations.io.DirScanner;
import com.andcreations.resources.BundleResources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Mikolaj Gucki
 */
public class LuaDocLuaDirParser {
    /** The string resources. */
    private BundleResources res = new BundleResources(LuaDocLuaDirParser.class);
    
    /** */
    private void parseFile(File dir,String file)
        throws LuaParseException,LuaDocException,IOException {
    //
        if (file.endsWith(".lua") == false) {
            return;
        }
        String moduleName = file
            .substring(0,file.length() - 4)            
            .replace('\\','.')
            .replace('/','.');
        
        LuaDocLuaFileParser parser = new LuaDocLuaFileParser();
        LuaDocParseContext context = new LuaDocParseContext();
        System.out.println("------------ " + file);
        LuaDocFileData data = parser.parse(
            new File(dir,file),moduleName,context);
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(data));
        
        for (LuaDocError error:data.getErrors()) {
            System.out.println("  eror: " + error);
        }         
        for (LuaDocWarning warning:data.getWarnings()) {
            System.out.println("  warn: " + warning);
        }         
    }
    
    /** */
    private void parseDir(File dir) throws LuaParseException,
        LuaDocException,IOException {
    //
        DirScanner dirScanner = new DirScanner(dir,false);
        String[] files = dirScanner.build();
        for (String file:files) {
            try {
                parseFile(dir,file);
            } catch (LuaDocException exception) {
                File fullFile = new File(dir,file);
                LuaDocException luaDocExcepton = new LuaDocException(
                    res.getStr("lua.doc.exception",
                    exception.getMessage(),fullFile.getAbsolutePath()),
                    exception.getLine());
                throw luaDocExcepton;
            }
        }
    }
    
    /** */
    public void parse(String[] dirs) throws LuaParseException,
        LuaDocException,IOException {
    //
        for (String dir:dirs) {
            parseDir(new File(dir));
        }
    }
}