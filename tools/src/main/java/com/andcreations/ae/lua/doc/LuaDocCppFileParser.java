package com.andcreations.ae.lua.doc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.andcreations.ae.lua.parser.LuaElement;
import com.andcreations.ae.lua.parser.LuaSingleLineComment;

/**
 * @author Mikolaj Gucki
 */
public class LuaDocCppFileParser {
    /** */
    private class LuaDummyElement extends LuaElement {
        /** */
        private LuaDummyElement(int line) {
            super(line,line);
        }
    }
    
    /** */
    private List<LuaElement> parse(String src) throws IOException {
        List<LuaElement> elements = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new StringReader(src));
        
        int lineNo = 0;
        int prevCommentLineNo = Integer.MIN_VALUE;
        
        String line;
    // for each line
        while ((line = reader.readLine()) != null) {
            lineNo++;

            int indexOf = line.indexOf("--");
            if (indexOf == -1) {            
                continue;
            }
            
        // if the comments are not adjacent...
            if (lineNo - prevCommentLineNo > 1) {
            // ...add a dummy element so that the comment blocks won't be
            // folded into one
                elements.add(new LuaDummyElement(lineNo));
            }
            prevCommentLineNo = lineNo;
            
            String comment = line.substring(indexOf,line.length());
            elements.add(new LuaSingleLineComment(comment,lineNo));
        }
        elements.add(new LuaDummyElement(lineNo));
        
        return elements;
    }
    
    /**
     * Parses a C++ file.
     *
     * @param src The source.
     * @param context The context.
     * @return The info on the file.
     * @throws LuaDocException      
     * @throws IOException      
     */
    public LuaDocFileData parse(String src,LuaDocParseContext context)
        throws IOException {
    //
        List<LuaElement> elements = LuaCommentBlock.foldComments(parse(src));
        LuaDocFileParser parser = new LuaDocFileParser();
        return parser.parse(elements,context);        
    }
    
    /**
     * Parses a C++ file.
     *
     * @param file The file.
     * @return The info on the file.
     * @throws LuaDocException      
     * @throws IOException     
     */    
    public LuaDocFileData parse(File file,LuaDocParseContext context)
        throws IOException {
    // source
        String src = FileUtils.readFileToString(file);
        
    // context
        context.setFileData(new LuaDocFileData());
        
    // parse
        return parse(src,context);
    }    
}
