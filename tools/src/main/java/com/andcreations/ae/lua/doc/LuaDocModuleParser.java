package com.andcreations.ae.lua.doc;

import java.util.List;

import com.andcreations.ae.lua.parser.LuaElement;

/**
 * @author Mikolaj Gucki
 */
class LuaDocModuleParser extends LuaDocParser {
    /** */
    LuaDocModuleParser() {
    }
    
    /** */
    @Override
    boolean match(LuaDocCommentBlock block,List<LuaElement> elements,
        int index) {
    //
        if (matchByTag(block,LuaDocTag.MODULE) == true) {
            return true;
        }
        
        return block.getBeginLine() == 1;
    }
    
    /** */
    @Override
    void parse(LuaDocCommentBlock block,List<LuaElement> elements,int index,
        LuaDocParseContext context) {
    // validate
        if (validateTags(context,block,new String[] {
            LuaDocTag.MODULE + "?",
            LuaDocTag.BRIEF + "?",
            LuaDocTag.FULL + "?",
            LuaDocTag.GROUP + "?"}) == false) {
        //
            return;
        }

    // module
        LuaDocModule module = context.getFileData().getModule();
            
    // values
        String name = block.getTagValue(LuaDocTag.MODULE);
        if (name != null) { 
            module.setName(name);
        }
        module.setBrief(block.getTagValue(LuaDocTag.BRIEF));
        module.setFull(block.getTagValue(LuaDocTag.FULL));
        module.setGroup(block.getTagValue(LuaDocTag.GROUP));
    }
}