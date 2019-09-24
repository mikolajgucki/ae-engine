package com.andcreations.ae.lua.doc;

import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.lua.parser.LuaElement;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class LuaDocFileParser {
    /** The string resources. */
    private BundleResources res = new BundleResources(LuaDocFileParser.class); 
    
    /** */
    private List<LuaDocParser> parsers = new ArrayList<>();
    
    /** */
    public LuaDocFileParser() {
        create();
    }
    
    /** */
    private void create() {
        parsers.add(new LuaDocModuleParser());
        parsers.add(new LuaDocFuncParser());
        parsers.add(new LuaDocVarParser());
    }
    
    /** */
    private LuaDocParser findParser(LuaDocCommentBlock block,
        List<LuaElement> elements,int index) {
    //
        for (LuaDocParser parser:parsers) {
            if (parser.match(block,elements,index)) {
                return parser;
            }
        }
        
        return null;    
    }    
    
    /** */
    private void parse(LuaCommentBlock luaCommentBlock,
        List<LuaElement> elements,int index,LuaDocParseContext context) {
    // parse comment block
        LuaCommentBlockParser commentBlockParser =
            new LuaCommentBlockParser(context);
        LuaDocCommentBlock block = commentBlockParser.parse(luaCommentBlock);
        if (block.isEmpty() == true) {
            return;
        }
        
    // find a parser which can parse the block
        LuaDocParser parser = findParser(block,elements,index);            
        if (parser == null) {
            LuaDocIssue.addError(context,
                LuaDocParser.ERROR_UNKNOWN_LUA_DOC_BLOCK,null,
                res.getStr("unknown.lua.doc.block"),block.getBeginLine());
            return;
        }
            
    // parse
        parser.parse(block,elements,index,context);
    }
    
    /** */
    public LuaDocFileData parse(List<LuaElement> elements,
        LuaDocParseContext context) {

    // file data
        if (context.getFileData() == null) {
            context.setFileData(new LuaDocFileData());
        }
        
    // TODO Fail if a comment is inside body function
            
    // for each element
        for (int index = 0; index < elements.size(); index++) {
            LuaElement element = elements.get(index);
            
        // if comment block
            if (element instanceof LuaCommentBlock) {
                parse((LuaCommentBlock)element,elements,index,context);
            }
        }
        
        return context.getFileData();
    }
}