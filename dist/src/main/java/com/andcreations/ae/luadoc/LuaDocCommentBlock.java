package com.andcreations.ae.luadoc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

/**
 * Represents a LuaDoc comment block.
 *
 * @author Mikolaj Gucki
 */
public class LuaDocCommentBlock {
    /** The name of the source file. */
    private String srcFilename;
    
    /** The line at which the comment block started. */
    private int srcLine;
    
    /** The lines of the comment block. */    
    private List<String> lines = new ArrayList<String>();
    
    /**
     * Construrcts a {@link LuaDocCommentBlock}.
     *
     * @param srcFilename The name of the source file.
     * @param srcLine The line at which the comment block started.
     */
    public LuaDocCommentBlock(String srcFilename,int srcLine) {
        this.srcFilename = srcFilename;
        this.srcLine = srcLine;
    }
    
    /**
     * Gets the source file name in which the comment block appeared.
     *
     * @return The source file name.
     */
    public String getSrcFilename() {
        return srcFilename;
    }
    
    /**
     * Gets the line number at which the comment block started.
     *
     * @return The line number.
     */
    public int getSrcLine() {
        return srcLine;
    }    
    
    /**
     * Appends a line to the bloc.
     *
     * @param line The line.
     */
    public void appendLine(String line) {
        line = StringUtils.stripStart(line,null).replace("\\ "," ");

    // if the last line ends with a backslash, append to the last line
        if (lines.isEmpty() == false) {
            String lastLine = lines.get(lines.size() - 1);
            if (lastLine.length() > 0) {
                char lastCh = lastLine.charAt(lastLine.length() - 1);
                if (lastCh == '\\') {
                    line = lastLine.substring(0,lastLine.length() - 1) +
                        " " + line;
                    lines.remove(lines.size() - 1);
                }
            }
        }
        
        lines.add(line);        
    }
    
    /** */
    public String getFirstTagName() {
        Matcher matcher = LuaDocParser.matches(lines.get(0)," *@([a-z]+) .*");
        if (matcher == null) {
            throw new IllegalStateException();
        }
            
        return matcher.group(1);
    }
    
    /** */
    public int getLineCount() {
        return lines.size();
    }
    
    /** */
    public String getLine(int index) {
        return lines.get(index);
    }
}