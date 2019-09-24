package com.andcreations.ae.lua.doc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.andcreations.ae.lua.parser.LuaSingleLineComment;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class LuaCommentBlockParser {
    /** The string resources. */
    private BundleResources res =
        new BundleResources(LuaCommentBlockParser.class); 
    
    /** The tag name. */
    private String tagName;
    
    /** The tag value. */
    private String tagValue;    
    
    /** The tag line. */
    private int tagLine;
    
    /** The parsed tags. */
    private List<LuaDocTag> tags;
    
    /** The context. */
    LuaDocParseContext context;
    
    /** */
    public LuaCommentBlockParser(LuaDocParseContext context) {
        this.context = context;
    }
    
    /** */
    private static Matcher matcher(String text,String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        
        if (matcher.matches() == false) {
            return null;
        }
        return matcher;
    }    
    
    /** */
    private void tagStartMatched(String name,String value) {
        if (tagName != null) {
            tags.add(new LuaDocTag(tagName,tagValue,tagLine));
        }
        
        tagName = name;
        tagValue = StringUtils.stripStart(value,null);
    }       
    
    /** */
    private String processValue(String line,LuaCommentBlock block) {
        line = line.trim();
        
        final String modnameVar = "$modname";
        final String modnamePattern = "\\$modname";
    // modules name
        if (line.contains(modnameVar)) {
            LuaDocModule module = context.getFileData().getModule();
            if (module != null && module.getName() != null &&
                module.getName().length() > 0) {
            //
                line = line.replaceAll(modnamePattern,module.getName());
            }
            else {
                LuaDocIssue.addError(context,
                    LuaDocParser.NO_MODULE_NAME,null,
                    res.getStr("no.module.name"),block.getBeginLine());
            }
        }
        
        return line;
    }
    
    /** */
    synchronized LuaDocCommentBlock parse(LuaCommentBlock block) {
        tagLine = -1;
        tagName = null;
        tagValue = null;        
        tags = new ArrayList<>();
        
    // for each line in the comment
        for (LuaSingleLineComment comment:block.getComments()) {
            String line = comment.getComment();    
            line = line.trim();
            if (line.startsWith("--")) {
                line = line.substring(2,line.length());
            }
            
        // tag start
            Matcher matcher = matcher(line," *@([a-z]+)(.*)");
            if (matcher != null) {
                String value = processValue(matcher.group(2),block);
                tagStartMatched(matcher.group(1),value);
                tagLine = comment.getBeginLine();
                continue;
            }
            
            tagValue = tagValue + " " + processValue(line,block);
        }
        if (tagName != null) {
            tags.add(new LuaDocTag(tagName,tagValue,tagLine));
        }
        return new LuaDocCommentBlock(block.getBeginLine(),tags);
    }
}