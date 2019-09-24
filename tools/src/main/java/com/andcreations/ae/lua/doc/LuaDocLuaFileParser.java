package com.andcreations.ae.lua.doc;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.andcreations.ae.lua.parser.LuaElement;
import com.andcreations.ae.lua.parser.LuaFileInfo;
import com.andcreations.ae.lua.parser.LuaFileParser;
import com.andcreations.ae.lua.parser.LuaParseException;

/**
 * @author Mikolaj Gucki
 */
public class LuaDocLuaFileParser {
    /** */
    private LuaFileParser luaFileParser = new LuaFileParser();
    
    /**
     * Parses a Lua file.
     *
     * @param src The source.
     * @param context The context.
     * @return The info on the file. 
     */
    public LuaDocFileData parse(List<LuaElement> elements,
        LuaDocParseContext context) {
    //
        List<LuaElement> foldedElements =
            LuaCommentBlock.foldComments(elements);
        
        LuaDocFileParser parser = new LuaDocFileParser();
        return parser.parse(foldedElements,context);
    }
    
    /**
     * Parses a Lua file.
     *
     * @param src The source.
     * @param context The context.
     * @return The info on the file.
     * @throws LuaParseException      
     */
    public LuaDocFileData parse(String src,LuaDocParseContext context)
        throws LuaParseException {
    //
        LuaFileInfo info = luaFileParser.parse(src);
        return parse(info.getElements(),context);
    }
    
    /**
     * Parses a Lua file.
     *
     * @param src The source.
     * @param moduleName The module name.
     * @return The info on the file.
     * @throws LuaParseException    
     * @throws IOException    
     */    
    public LuaDocFileData parse(String src,String moduleName,
        LuaDocParseContext context) throws LuaParseException,IOException {
    // file data
        LuaDocFileData fileData = new LuaDocFileData();
        
    // module
        LuaDocModule module = new LuaDocModule();
        module.setName(moduleName);
        fileData.setModule(module);
        
    // context
        context.setFileData(fileData);
        
    // parse
        return parse(src,context);
    }    
    
    /**
     * Parses a Lua file.
     *
     * @param file The file.
     * @param moduleName The module name.
     * @return The info on the file.
     * @throws LuaParseException    
     * @throws LuaDocException    
     * @throws IOException    
     */    
    public LuaDocFileData parse(File file,String moduleName,
        LuaDocParseContext context) throws LuaParseException,IOException {
    // source
        String src = FileUtils.readFileToString(file);
        
        return parse(src,moduleName,context);
    }    
}
