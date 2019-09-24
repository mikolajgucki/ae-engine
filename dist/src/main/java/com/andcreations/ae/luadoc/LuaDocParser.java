package com.andcreations.ae.luadoc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.andcreations.resources.BundleResources;
import com.andcreations.resources.StrResources;

/**
 * Parses a Lua source file and find the valid comments block.
 *
 * @author Mikolaj Gucki
 */
public class LuaDocParser {
    /** The parses state. */
    private enum State {
        /** */
        VOID,
        
        /** */
        LUADOC_COMMENT;
    };
    
    /** The string resources. */
    private StrResources res = new BundleResources(LuaDocParser.class);     
    
    /** The current parser state. */
    private State state = State.VOID;

    /** The name of the source file. */
    private String srcFilename;
    
    /** The line being parsed. */
    private String line;
    
    /** The current line number. */
    private int lineNo;    
    
    /** The comment block. */
    private LuaDocCommentBlock commentBlock;
    
    /** The current module. */
    private LuaDocModule module;
    
    /** The parsed modules. */
    private List<LuaDocModule> modules;
    
    /** */
    static Matcher matches(String text,String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        
        if (matcher.matches() == false) {
            return null;
        }
        return matcher;
    }
    
    /** */
    private void parseCommentBlock() throws LuaDocException {
        String tagName = commentBlock.getFirstTagName();
        
    // module
        if (tagName.equals(LuaDocModule.TAG)) {
            module = LuaDocModule.parse(commentBlock);
            modules.add(module);
        }
        
    // function
        if (tagName.equals(LuaDocFunc.TAG)) {
            if (module == null) {
                throw new LuaDocException(res.getStr("module.prior.func",
                    LuaDocModule.TAG));
            }
            LuaDocFunc func = LuaDocFunc.parse(module,commentBlock);
            module.addFunc(func);
        }
        
    // variable
        if (tagName.equals(LuaDocVar.TAG)) {
            if (module == null) {
                throw new LuaDocException(res.getStr("module.prior.var",
                    LuaDocModule.TAG));
            }            
            LuaDocVar var = LuaDocVar.parse(module,commentBlock);
            module.addVar(var);
        }
    }
    
    /** */
    private void luaDocCommentStart(Matcher matcher) {
        state = State.LUADOC_COMMENT;
        commentBlock = new LuaDocCommentBlock(srcFilename,lineNo);
        commentBlock.appendLine(matcher.group(1));
    }
    
    /** */
    private void luaDocComment(Matcher matcher) {
        commentBlock.appendLine(matcher.group(1));
    }
    
    /** */
    private void luaDocCommentEnd() throws LuaDocException {
        parseCommentBlock();
        state = State.VOID;
    }
    
    /**
     * Parses the current line.
     *
     * @throws LuaDocException if parsing fails.     
     */
    private void parseLine() throws LuaDocException {
    // LuaDoc comment start
        Matcher luaDocCommentMatcher = matches(line,"^\\-\\- +(@.*)$");
        if (luaDocCommentMatcher != null) {
            if (state == State.VOID) {
                luaDocCommentStart(luaDocCommentMatcher);
            }
            else if (state == State.LUADOC_COMMENT) {
                luaDocComment(luaDocCommentMatcher);
            }
            return;
        }
        
    // comment
        Matcher commentMatcher = matches(line,"^\\-\\-(.*)$");
        if (commentMatcher != null) {
            if (state == State.LUADOC_COMMENT) {
                luaDocComment(commentMatcher);
            }
            return;
        }
        
    // anything else
        if (state == State.LUADOC_COMMENT) {
            luaDocCommentEnd();
            return;
        }
    }
    
    /**
     * Parses a source file.
     *
     * @param srcFilename The name of the source file.
     * @param fileSrc The source.
     * @return The parsed modules.
     * @throws LuaDocException if parsing fails.     
     */
    public List<LuaDocModule> parse(String srcFilename,String[] src)
        throws LuaDocException {
    //
        if (modules != null) {
            throw new IllegalStateException();
        }        
        modules = new ArrayList<LuaDocModule>();
        this.srcFilename = srcFilename;
        
        for (int index = 0; index < src.length; index++) {
            line = src[index];
            lineNo = index + 1;
            
            try {
                parseLine();
            } catch (LuaDocException exception) {
                throw new LuaDocException(res.getStr("error.near.line",
                    exception.getMessage(),Integer.toString(lineNo)));
            }
        }
        
        return modules;
    }
}