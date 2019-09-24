package com.andcreations.ae.lua.doc;

import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.lua.parser.LuaComment;
import com.andcreations.ae.lua.parser.LuaElement;
import com.andcreations.ae.lua.parser.LuaSingleLineComment;

/**
 * A block of single line comments.
 * 
 * @author Mikolaj Gucki
 */
class LuaCommentBlock extends LuaComment {
    /** */
    private List<LuaSingleLineComment> comments;
    
    /** */
    LuaCommentBlock(List<LuaSingleLineComment> comments,int beginLine,
        int endLine) {
    //
        super(beginLine,endLine);
        this.comments = comments;
    }
    
    /** */
    List<LuaSingleLineComment> getComments() {
        return comments;
    }
    
    /** */
    int getLineCount() {
        return comments.size();
    }
    
    /** */
    String getLine(int index) {
        return comments.get(index).getComment();
    }
    
    /** */
    @Override
    public String toString() {
        return String.format("comment block [lines from %d to %d]",
            getBeginLine(),getEndLine());
    }
    
    /** */
    static List<LuaElement> foldComments(List<LuaElement> elements) {
        List<LuaElement> newElements = new ArrayList<>();
        
        int beginLine = -1;
        int endLine = -1;
        List<LuaSingleLineComment> comments = null;
        
        int index = 0;
        while (index < elements.size()) {
            LuaElement element = elements.get(index);
            index++;
            
        // single-line comment
            if (element instanceof LuaSingleLineComment) {
                LuaSingleLineComment last = null;
                if (comments != null) {
                    last = comments.get(comments.size() - 1);
                // if there is a non-comment line between comments...
                    if (element.getBeginLine() - last.getBeginLine() > 1) {
                    // ...flush the comment block
                        newElements.add(
                            new LuaCommentBlock(comments,beginLine,endLine));
                        comments = null;
                    }
                }
                
            // add the single-line comment to the list
                if (comments == null) {
                    beginLine = element.getBeginLine();
                    comments = new ArrayList<>();
                }
                comments.add((LuaSingleLineComment)element);
                endLine = element.getBeginLine();
            }
            else {
            // flush the comment block
                if (comments != null) {
                    newElements.add(
                        new LuaCommentBlock(comments,beginLine,endLine));
                    comments = null;
                }
                newElements.add(element);
            }
        }
        
        if (comments != null) {
            newElements.add(new LuaCommentBlock(comments,beginLine,endLine));
        }
        
        return newElements;
    }        
}