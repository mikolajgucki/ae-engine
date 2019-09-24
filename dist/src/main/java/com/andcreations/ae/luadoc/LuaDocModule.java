package com.andcreations.ae.luadoc;

import java.util.ArrayList;
import java.util.List;

import com.andcreations.resources.BundleResources;
import com.andcreations.resources.StrResources;

/**
 * Represents a module.
 *
 * @author Mikolaj Gucki
 */
public class LuaDocModule {
    /** The tag identyfing a module comment block. */
    static final String TAG = "module";
    
    /** The tag identyfing the group to which the module belongs. */
    private static final String TAG_GROUP = "group";
    
    /** The tag identyfing the supermodule. */
    private static final String TAG_SUPER = "super";
    
    /** The string resources. */
    private static StrResources res = new BundleResources(LuaDocModule.class);    
    
    /** The original comment block. */
    private LuaDocCommentBlock commentBlock;     
    
    /** The module name. */
    private String name;
    
    /** The group to which the module belongs. */
    private String group;    
    
    /** The supermodule name. */
    private String superModuleName;
    
    /** The brief description. */
    private String brief;
    
    /** The full description. */
    private String full;
    
    /** The functions contained in the module. */
    private List<LuaDocFunc> funcs = new ArrayList<LuaDocFunc>();
    
    /** The variabes contained in the module. */
    private List<LuaDocVar> vars = new ArrayList<LuaDocVar>();
    
    /** */
    public LuaDocModule(LuaDocCommentBlock commentBlock,String name) {
        this.commentBlock = commentBlock;
        this.name = name;
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
    
    /** */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the group to which the module belongs.
     *
     * @param group The group.
     */
    public void setGroup(String group) {
        this.group = group;
    }
    
    /**
     * Gets the group to which the module belongs.
     *
     * @return The group.
     */
    public String getGroup() {
        return group;
    }
    
    /**
     * Sets the supermodule name.
     *
     * @param superModuleName The supermodule name.
     */
    public void setSuperModuleName(String superModuleName) {
        this.superModuleName = superModuleName;
    }
    
    /**
     * Gets the supermodule name.
     *
     * @return The supermodule name.
     */
    public String getSuperModuleName() {
        return superModuleName;
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
     * Adds a module function.
     *
     * @param func The function.
     */
    public void addFunc(LuaDocFunc func) {
        funcs.add(func);
    }
    
    /**
     * Gets the module functions.
     *
     * @return The functions.
     */
    public List<LuaDocFunc> getFuncs() {
        return funcs;
    }
    
    /**
     * Indicates if the module has functions.
     *
     * @return <code>true</code> if has, <code>false</code> otherwise.
     */
    public boolean hasFuncs() {
        return funcs.isEmpty() == false;
    }
    
    /**
     * Adds a variable to the module.
     *
     * @param var The variable.
     */
    public void addVar(LuaDocVar var) {
        vars.add(var);
    }
    
    /**
     * Gets the module variables.
     *
     * @return The variables.
     */
    public List<LuaDocVar> getVars() {
        return vars;
    }
        
    /**
     * Indicates if the module has variables.
     *
     * @return <code>true</code> if has, <code>false</code> otherwise.
     */
    public boolean hasVars() {
        return vars.isEmpty() == false;
    }    
    
    /**
     * Parses a module from a comment block.
     *
     * @param commentBlock The comment block.
     * @return The module.
     * @throws LuaDocException if the function cannot be parsed.
     */
    public static LuaDocModule parse(LuaDocCommentBlock commentBlock)
        throws LuaDocException {
    //
        LuaDocCommentBlockParser parser =
            new LuaDocCommentBlockParser(commentBlock);
        
    // the first tag must be a module
        LuaDocTag firstTag = parser.getFirstTag();
        if (TAG.equals(firstTag.getName()) == false) {
            throw new LuaDocException(res.getStr("not.a.module"));
        }
        String name = firstTag.getValue();
        if (name == null || name.trim().length() == 0) {
            throw new LuaDocException(res.getStr("no.module.name"));
        }
        
    // validate
        parser.validateTagNames(new String[]{
            TAG,TAG_GROUP,TAG_SUPER,LuaDocTag.BRIEF,LuaDocTag.FULL});
    
    // module
        LuaDocModule module = new LuaDocModule(commentBlock,name);
        module.setGroup(parser.getTagValue(TAG_GROUP));
        module.setBriefDesc(parser.getTagMarkdownValue(LuaDocTag.BRIEF));
        module.setFullDesc(parser.getTagMarkdownValue(LuaDocTag.FULL));
        
    // supermodule
        module.setSuperModuleName(parser.getTagValue(TAG_SUPER));
        if (module.getName().equals(module.getSuperModuleName()) == true) {
            throw new LuaDocException(res.getStr("module.super.module",
                module.getName()));
        }
        
        return module;
    }
}