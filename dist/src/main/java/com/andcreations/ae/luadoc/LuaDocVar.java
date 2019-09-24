package com.andcreations.ae.luadoc;

import com.andcreations.resources.BundleResources;
import com.andcreations.resources.StrResources;

/**
 * Contains the LuaDoc for a variable.
 *
 * @author Mikolaj Gucki
 */
public class LuaDocVar {
    /** The tag identyfing a variable comment block. */
    static final String TAG = "var";
    
    /** The string resources. */
    private static StrResources res = new BundleResources(LuaDocVar.class);    
    
    /** The original comment block. */
    private LuaDocCommentBlock commentBlock;     
    
    /** The variable identifier. */
    private String id;    
    
    /** The variable syntax. */
    private String syntax;
    
    /** The brief description. */
    private String brief;  
    
    /** The full description. */
    private String full;
    
    /** */
    public LuaDocVar(LuaDocCommentBlock commentBlock,String syntax) {
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
        id = syntax;
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
     * Gets the variable syntax.
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
     * Parses a variable from a comment block.
     *
     * @param module The enclosing module.
     * @param commentBlock The comment block.
     * @return The variable.
     * @throws LuaDocException if the function cannot be parsed.
     */
    public static LuaDocVar parse(LuaDocModule module,
        LuaDocCommentBlock commentBlock) throws LuaDocException {
    //
        LuaDocCommentBlockParser parser =
            new LuaDocCommentBlockParser(commentBlock);
    
    // the first tag must be a variable
        LuaDocTag firstTag = parser.getFirstTag();
        if (TAG.equals(firstTag.getName()) == false) {
            throw new LuaDocException(res.getStr("not.a.var"));
        }
        
    // validate
        parser.validateTagNames(new String[]{
            TAG,LuaDocTag.BRIEF,LuaDocTag.FULL});        
        
    // syntax
        String syntax = LuaDocCommon.updateSyntax(module,firstTag.getValue());
        
    // variable
        LuaDocVar var = new LuaDocVar(commentBlock,syntax);
        var.setBriefDesc(parser.getTagMarkdownValue(LuaDocTag.BRIEF));
        var.setFullDesc(parser.getTagMarkdownValue(LuaDocTag.FULL));
        
        return var;
    }
}