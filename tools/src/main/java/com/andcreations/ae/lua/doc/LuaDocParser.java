package com.andcreations.ae.lua.doc;

import java.util.List;

import com.andcreations.ae.lua.parser.LuaElement;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
abstract class LuaDocParser {    
    /** */
    public static final String ERROR_MODULE_NAME_NOT_SET =
        "module.name.not.set";
    
    /** */
    public static final String ERROR_NOT_ONCE_OR_NONE = "not.once.or.none";
    
    /** */
    public static final String ERROR_NOT_ONCE_OR_MORE = "not.once.or.more";
    
    /** */
    public static final String ERROR_NOT_EXACTLY_ONE = "not.exactly.one";
    
    /** */
    public static final String ERROR_UNKNOWN_TAG = "unknown.tag";
    
    /** */
    public static final String ERROR_UNKNOWN_LUA_DOC_BLOCK = "unknown.block";
    
    /** */
    public static final String ERROR_TAG_VALUE = "tag.value";
    
    /** */
    public static final String NO_MODULE_NAME = "no.module.name";
    
    /** The string resources. */
    private BundleResources res = new BundleResources(LuaDocParser.class); 
    
    /** */
    protected LuaDocModule getModule(LuaDocParseContext context) {
        return context.getFileData().getModule();
    }
    
    /** */
    protected String getLocalName(String name,LuaDocParseContext context)
        throws LuaDocException {
    //
        LuaDocModule module = context.getFileData().getModule();
        if (module == null || module.getName() == null) {
            LuaDocIssue.addError(context,ERROR_MODULE_NAME_NOT_SET,null,
                res.getStr("module.name.not.set"),1);
        // thrown so that it's known that it failed to get the local name
            throw new LuaDocException(res.getStr("module.name.not.set"),1);
        }
        
        return module.getElementLocalName(name);    
    }
    
    /** */
    private char getQuantitiferFromPattern(String pattern) {
        char ch = pattern.charAt(pattern.length() - 1);
        if (ch != '?' && ch != '+' && ch != '*') {
            return 0;
        }
        return ch;
    }
    
    /** */
    private String getTagNameFromPattern(String pattern) {
        char ch = getQuantitiferFromPattern(pattern);
        if (ch != '?' && ch != '+' && ch != '*') {
            return pattern;
        }
        return pattern.substring(0,pattern.length() - 1);
    }
    
    /** */
    private boolean validateTags(LuaDocParseContext context,
        LuaDocCommentBlock block,String pattern) {
    // quantifier
        char ch = getQuantitiferFromPattern(pattern);
        String tagName = getTagNameFromPattern(pattern);

    // the number of occurences of the tag, line number
        int count = 0;
        int line = -1;
        
    // for each tag
        for (LuaDocTag tag:block.getTags()) {
            if (tagName.equals(tag.getName())) {
                if (line == -1) {
                    line = tag.getLine();
                }
                count++;
            }
        }
        
        if (line == -1) {
            line = block.getBeginLine();
        }
        
    // quantifier
        if (ch == '?') {  
        // once or not at all
            if (count > 1) {
                LuaDocIssue.addError(context,ERROR_NOT_ONCE_OR_NONE,tagName,
                    res.getStr("not.once.or.none",tagName),line);
                return false;
            }
        }
        else if (ch == '+') {
        // one or more times
            if (count == 0) {
                LuaDocIssue.addError(context,ERROR_NOT_ONCE_OR_MORE,tagName,
                    res.getStr("not.once.or.more",tagName),line);
                return false;
            }
        }
        else if (ch == '*') {
        // zero or more times        
        }
        else {
            // exaclty one
            if (count != 1) {
                LuaDocIssue.addError(context,ERROR_NOT_EXACTLY_ONE,tagName,
                    res.getStr("not.exactly.one",tagName),line);
                return false;                
            }
        }
        
        return true;
    }
    
    /** */
    private boolean validateUnknownTags(LuaDocParseContext context,
        LuaDocTag tag,String[] patterns) {
    //
        for (String pattern:patterns) {
            String tagName = getTagNameFromPattern(pattern);
            if (tag.getName().equals(tagName) == true) {
                return true;
            }
        }
        
        LuaDocIssue.addError(context,ERROR_UNKNOWN_TAG,null,
            res.getStr("unknown.tag"),tag.getLine());
        return false;
    }
    
    /** */
    protected boolean validateTags(LuaDocParseContext context,
        LuaDocCommentBlock block,String[] patterns) {      
    //
        boolean valid = true;
        for (String pattern:patterns) {
            if (validateTags(context,block,pattern) == false) {
                valid = false;
            }
        }
        for (LuaDocTag tag:block.getTags()) {
            if (validateUnknownTags(context,tag,patterns) == false) {
                valid = false;
            }
        }
        
        return valid;
    }    
    
    /** */
    protected boolean validateNoValues(LuaDocParseContext context,
        LuaDocCommentBlock block,String tagName) {
    //
        boolean valid = true;
        for (LuaDocTag tag:block.getTags()) {
            if (tag.matchName(tagName) == true && tag.getValue() != null) {
                LuaDocIssue.addError(context,ERROR_TAG_VALUE,tagName,
                    res.getStr("tag.value",tagName),tag.getLine());
                valid = false;                
            }
        }
        
        return valid;
    }
    
    /** */
    protected LuaElement getNextElement(List<LuaElement> elements,int index) {
        if (index < elements.size() - 1) {
            return elements.get(index + 1);
        }
        
        return null;
    }
    
    /** */
    protected boolean matchByTag(LuaDocCommentBlock block,String name) {
        return block.hasTag(name);
    }
    
    /** */
    abstract boolean match(LuaDocCommentBlock block,List<LuaElement> elements,
        int index);
    
    /** */
    abstract void parse(LuaDocCommentBlock block,List<LuaElement> elements,
        int index,LuaDocParseContext context);
}