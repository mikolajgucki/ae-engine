package com.andcreations.ae.doc.nav;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mikolaj Gucki
 */
public class AEDocNavParser {
    /** */
    private static final int NO_INDENT = -1;
    
    /** */
    private List<String> lines;
    
    /** */
    private int level;    
    
    /** */
    private int prevIndent = NO_INDENT;
    
    /** */
    AEDocNavParser(List<String> lines) {
        this.lines = lines;
    }
    
    /** */
    private int getIndent(String line) {
        int count = 0;
        while (line.charAt(count) == ' ' && count < line.length()) {
            count++;
        }
        
        return count;
    }
    
    /** */
    private String getText(String line,int indent) {
        return line.trim();
    }
    
    /** */
    private void validate(AEDocNavEntry entry) throws IOException {
        if (entry.getTitle() == null) {
            throw new IOException("Navigation entry without title");
        }
    }
    
    /** */
    private AEDocNavEntry parseEntry(int level,String text) throws IOException {
        Map<String,String> properties = new HashMap<String,String>();
        
        String[] tokens = text.split(";");
    // for each properties
        for (String token:tokens) {
            String[] nameValue = token.split("=");
            if (nameValue.length != 2) {
                throw new IOException(String.format(
                    "Invalid navigation entry: %s",text));
            }
            properties.put(nameValue[0],nameValue[1]);
        }
        
    // create, validate
        AEDocNavEntry entry = new AEDocNavEntry(level,text,properties);
        validate(entry);
        
        return entry;
    }
    
    /** */
    List<AEDocNavEntry> parse() throws IOException {
        List<AEDocNavEntry> entries = new ArrayList<>();
        
    // for each line
        for (String line:lines) {
            int indent = getIndent(line);
            String text = getText(line,indent);
            
        // level
            if (prevIndent != NO_INDENT) {
                if (indent < prevIndent) {
                    level--;
                }
                else if (indent > prevIndent) {
                    level++;
                }
            }
            prevIndent = indent;
            
        // entry
            entries.add(parseEntry(level,text));
        }
        
        return entries;
    }
}