package com.andcreations.ae.luadoc;

import java.util.List;

import com.andcreations.resources.BundleResources;
import com.andcreations.resources.StrResources;

/**
 * Contains the LuaDoc for a function.
 *
 * @author Mikolaj Gucki
 */
public class LuaDocFunc {
    /** The tag identyfing a function comment block. */
    static final String TAG = "func";
    
    /** The name of the return tag. */
    static final String TAG_RETURN = "return";
    
    /** The string resources. */
    private static StrResources res = new BundleResources(LuaDocFunc.class);    
    
    /** The original comment block. */
    private LuaDocCommentBlock commentBlock;    
    
    /** The function identifier. */
    private String id;
    
    /** The function syntax. */
    private String syntax;
    
    /** The brief description. */
    private String brief;  
    
    /** The full description. */
    private String full;
    
    /** The parameters. */
    private LuaDocParam[] params;
    
    /** The function return. */
    private String funcReturn;
    
    /** */
    public LuaDocFunc(LuaDocCommentBlock commentBlock,String syntax) {
        this.commentBlock = commentBlock;
        this.syntax = syntax;
        
        getIdFromSyntax();
    }

    /**
     * Gets the source file name in which the element appeared.
     *
     * @return The source file name.
     */
    public String getSrcFilename() {
        return commentBlock.getSrcFilename();
    }
    
    /**
     * Gets the number of the line in the source file at which the element
     * comment block appeared.
     *
     * @return The line number.
     */
    public int getSrcLine() {
        return commentBlock.getSrcLine();
    }    
    
    /**
     * Gets the identifier from the syntax.
     */
    private void getIdFromSyntax() {
        int indexOf = syntax.indexOf('(');
        id = syntax.substring(0,indexOf);        
    }
    
    /**
     * Gets the variable identifier.
     *
     * @return The variable identifier.
     */
    public String getId() {
        return id;
    }    
    
    /**
     * Gets the function syntax.
     *
     * @return The syntax.
     */
    public String getSyntax() {
        return syntax;
    }
        
    /**
     * Sets the brief description.
     *
     * @return The brief description.
     */
    public void setBriefDesc(String brief) {
        this.brief = brief;
    }
    
    /**
     * Gets the brief description.
     *
     * @return The brief description.
     */
    public String getBriefDesc() {
        return brief;
    }    
    
    /**
     * Sets the full description.
     *
     * @param full The full description.
     */
    public void setFullDesc(String full) {
        this.full = full;
    }
    
    /**
     * Gets the full description.
     *
     * @return The full description.
     */
    public String getFullDesc() {
        return full;
    }
    
    /**
     * Sets the parameters.
     *
     * @param params The parameters.
     */
    public void setParams(LuaDocParam[] params) {
        this.params = params;
    }
    
    /** 
     * Gets the parameters.
     *
     * @return The parameters.
     */
    public LuaDocParam[] getParams() {
        return params;
    }
    
    /**
     * Indicates if the function has parameters.
     *
     * @return <code>true</code> if has parameters, <code>false</code>
     *     otherwise.
     */
    public boolean hasParams() {
        return params != null && params.length > 0;
    }
    
    /**
     * Sets the return description.
     *
     * @param funcReturn The return description.
     */
    public void setReturn(String funcReturn) {
        this.funcReturn = funcReturn;
    }
    
    /**
     * Gets the return description.
     *
     * @return The return description.
     */
    public String getReturn() {
        return funcReturn;
    }
    
    /**
     * Parses paramters from tags.
     *
     * @param tags The tags.
     * @return The parsed parameters.
     * @throws LuaDocException if the parameters cannot be parsed.
     */
    private static LuaDocParam[] parseParams(List<LuaDocTag> tags)
        throws LuaDocException {
    //
        LuaDocParam[] params = new LuaDocParam[tags.size()];
        
        for (int index = 0; index < tags.size(); index++) {
            params[index] = LuaDocParam.parse(tags.get(index));
        }
        
        return params;
    }
    
    /**
     * Parses a function from a comment block.
     *
     * @param module The enclosing module.
     * @param commentBlock The comment block.
     * @return The function.
     * @throws LuaDocException if the function cannot be parsed.
     */
    public static LuaDocFunc parse(LuaDocModule module,
        LuaDocCommentBlock commentBlock) throws LuaDocException {
    //
        LuaDocCommentBlockParser parser =
            new LuaDocCommentBlockParser(commentBlock);
    
    // the first tag must be a function
        LuaDocTag firstTag = parser.getFirstTag();
        if (TAG.equals(firstTag.getName()) == false) {
            throw new LuaDocException(res.getStr("not.a.func"));
        }
        
    // validate
        parser.validateTagNames(new String[]{
            TAG,LuaDocTag.BRIEF,LuaDocTag.FULL,LuaDocParam.TAG,TAG_RETURN});        

    // syntax
        String syntax = firstTag.getValue();
        if (LuaDocParser.matches(syntax,".+\\(.*\\)") == null) {
            throw new LuaDocException(res.getStr("invalid.func.syntax"));
        }
        syntax = LuaDocCommon.updateSyntax(module,syntax);
        
    // function
        LuaDocFunc func = new LuaDocFunc(commentBlock,syntax);
        func.setBriefDesc(parser.getTagMarkdownValue(LuaDocTag.BRIEF));
        func.setFullDesc(parser.getTagMarkdownValue(LuaDocTag.FULL));
        func.setReturn(parser.getTagMarkdownValue(TAG_RETURN));
        
    // parameters
        List<LuaDocTag> paramTags = parser.getTagsByName(LuaDocParam.TAG);
        if (paramTags != null) {
            func.setParams(parseParams(paramTags));
        }
        
        return func;
    }
}