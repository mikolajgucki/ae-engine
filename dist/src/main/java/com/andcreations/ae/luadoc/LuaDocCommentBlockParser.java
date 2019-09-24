package com.andcreations.ae.luadoc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import com.andcreations.resources.BundleResources;
import com.andcreations.resources.StrResources;

/**
 * @author Mikolaj Gucki
 */
public class LuaDocCommentBlockParser {
        /** The string resources. */
    private static StrResources res =
        new BundleResources(LuaDocCommentBlockParser.class);   
    
    /** The tag name. */
    private String tagName;
    
    /** The tag value. */
    private String tagValue;    

    /** The parsed tags. */
    private List<LuaDocTag> tags = new ArrayList<LuaDocTag>();
    
    /** */
    public LuaDocCommentBlockParser(LuaDocCommentBlock block)
        throws LuaDocException {
    //
        parse(block);
    }
    
    /**
     * Gets the first parsed tag.
     *
     * @return The first tag.
     */
    public LuaDocTag getFirstTag() {
        return tags.get(0);
    }
    
    /**
     * Gets the tag count.
     *
     * @return The tag count.
     */ 
    public int getTagCount() {
        return tags.size();
    }
    
    /**
     * Gets a tag by name.
     *
     * @param name The tag name.
     * @return The tag or <code>null</code> if there is no such tag.
     * @throws LuaDocException if the tag of the name is duplicated.
     */
    public LuaDocTag getTag(String name) throws LuaDocException {
        LuaDocTag matched = null;
        
        for (LuaDocTag tag:tags) {
            if (tag.getName().equals(name)) {
                if (matched != null) {
                    throw new LuaDocException(
                        res.getStr("duplicated.tag",name));
                }
                
                matched = tag;
            }
        }
        
        return matched;
    }
    
    /**
     * Gets the value of a tag.
     *
     * @param name The tag name.
     * @return The value or <code>null</code> if there is no such tag.
     * @throws LuaDocException if the tag of the name is duplicated.
     */
    public String getTagValue(String name) throws LuaDocException {
        LuaDocTag tag = getTag(name);
        if (tag == null) {
            return null;
        }
        
        return tag.getValue();
    }
    
    /**
     * Gets the value of a tag processed with a markdown processor.
     *
     * @param name The tag name.
     * @return The value or <code>null</code> if there is no such tag.
     * @throws LuaDocException if the tag of the name is duplicated or
     *     markdown processing fails.
     */
    public String getTagMarkdownValue(String name) throws LuaDocException {
        LuaDocTag tag = getTag(name);
        if (tag == null) {
            return null;
        }
        
        return tag.getMarkdownValue();
    }
    
    /**
     * Gets all the tags by name.
     *
     * @param name The tag name.
     * @return The tags with the name or <code>null</code> if there are no
     *     such tags.
     */
    public List<LuaDocTag> getTagsByName(String name) {
        List<LuaDocTag> matched = null;
        
        for (LuaDocTag tag:tags) {
            if (tag.getName().equals(name)) {
                if (matched == null) {
                    matched = new ArrayList<LuaDocTag>();
                }
                matched.add(tag);
            }
        }
        
        return matched;
    }
    
    /**
     * Gets the tag at a given index.
     *
     * @param index The index.
     * @return The tag at the index.
     */
    public LuaDocTag getTag(int index) {
        return tags.get(index);
    }
    
    /**
     * Validates the tag names.
     *
     * @param tagNames The valid names.
     * @throws LuaDocException if the parsed tags contain a tag of invalid name.
     */
    public void validateTagNames(String[] tagNames) throws LuaDocException {
        for (LuaDocTag tag:tags) {
            boolean valid = false;
            for (String tagName:tagNames) {
                if (tag.getName().equals(tagName)) {
                    valid = true;
                    break;
                }
            }
            
            if (valid == false) {
                throw new LuaDocException(res.getStr("invalid.tag",
                    tag.getName()));
            }
        }
    }
    
    /**
     * Invoked when a tag start has been matched.
     *
     * @param name The tag name.
     * @param value The tag value following the name.
     */
    private void tagStartMatched(String name,String value) {
        if (tagName != null) {
            tags.add(new LuaDocTag(tagName,tagValue));            
        }
        
        tagName = name;
        tagValue = StringUtils.stripStart(value,null);
    }    
    
    /** */
    private void parse(LuaDocCommentBlock block) throws LuaDocException {
        for (int index = 0; index < block.getLineCount(); index++) {
            String line = block.getLine(index);
            
        // tag start
            Matcher matcher = LuaDocParser.matches(line," *@([a-z]+)(.*)");
            if (matcher != null) {
                tagStartMatched(matcher.group(1),matcher.group(2));
                continue;
            }
            
            tagValue = tagValue + "\n" + line;
        }
        tags.add(new LuaDocTag(tagName,tagValue));
    }
}