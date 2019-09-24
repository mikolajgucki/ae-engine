package com.andcreations.ae.studio.plugins.text.editor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.fife.ui.rsyntaxtextarea.modes.LuaTokenMaker;

import com.andcreations.ae.lua.doc.LuaDocTag;

/**
 * @author Mikolaj Gucki
 */
public class AELuaTokenMaker extends LuaTokenMaker {
    /** */
    public static final String SYNTAX_STYLE = "text/lua";
    
    /** */
    private static final int PREFIX_GROUP = 1;
    
    /** */
    private static final int TAG_GROUP = 2;
    
    /** */
    private static final int SUFFIX_GROUP = 3;
    
    /** */
    private String[] luaDocTags;
    
    /** */
    private Pattern luaDocTagPattern;
    
    /** */
    public AELuaTokenMaker() {
        create();
    }
    
    /** */
    private void create() {
        luaDocTagPattern = Pattern.compile("(-- +)(@[a-z]+)( .*)?\\r?");
        
    // tags
        luaDocTags = new String[LuaDocTag.NAMES.length];
        for (int index = 0; index < luaDocTags.length; index++) {
            luaDocTags[index] = "@" + LuaDocTag.NAMES[index];
        }
    }
    
    /** */
    private boolean isLuaDocTag(String tag) {
        for (String luaDocTag:luaDocTags) {
            if (luaDocTag.equals(tag) == true) {
                return true;
            }
        }
        
        return false;
    }
    
    /** */
    private void createLuaDocTokens(char[] line,int offset,Matcher matcher) {
        String suffix = null;
        if (matcher.groupCount() == SUFFIX_GROUP) {
            suffix = matcher.group(SUFFIX_GROUP);
        }
        
    // begin token
        addToken(line,
            matcher.start(PREFIX_GROUP),matcher.end(PREFIX_GROUP) - 1,
            TokenTypes.COMMENT_EOL,
            offset + matcher.start(PREFIX_GROUP));
        
    // tag token
        addToken(line,
            matcher.start(TAG_GROUP),matcher.end(TAG_GROUP) - 1,
            TokenTypes.COMMENT_KEYWORD,
            offset + matcher.start(TAG_GROUP));
        
    // suffix token
        if (suffix != null) {
            addToken(line,
                matcher.start(SUFFIX_GROUP),matcher.end(SUFFIX_GROUP) - 1,
                TokenTypes.COMMENT_EOL,
                offset + matcher.start(SUFFIX_GROUP));
        }        
    }
    
    /**
     * Creates token list from a line.
     *
     * @param text The line.
     * @param initialTokenType The last token of the previous line.
     * @param offset The offset at which the line of tokens begins.
     */
    @Override
    public Token getTokenList(Segment text,int initialTokenType,int offset) {
        String line = text.toString();
    // single line comment with a LuaDoc tag
        Matcher matcher = luaDocTagPattern.matcher(line);
        if (matcher.matches() == true) {
            String tag = matcher.group(TAG_GROUP);
            if (isLuaDocTag(tag) == true) {
                resetTokenList();
                createLuaDocTokens(line.toCharArray(),offset,matcher);
                return firstToken;
            }
        }
        
        return super.getTokenList(text,initialTokenType,offset);
    }
    
    /** */
    static void install() {
        AbstractTokenMakerFactory factory =
            (AbstractTokenMakerFactory)TokenMakerFactory.getDefaultInstance();
        factory.putMapping(SYNTAX_STYLE,AELuaTokenMaker.class.getName());
    }
}