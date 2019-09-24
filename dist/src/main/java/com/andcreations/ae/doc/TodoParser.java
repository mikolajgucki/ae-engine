package com.andcreations.ae.doc;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses TO-DOs.
 *
 * @author Mikolaj Gucki
 */
public class TodoParser {
    /** The TO-DO key words.*/
    private static final String TODO_KEYWORD = "TOD" + "O ";
    
    /**
     * Checks if the TO-DO description should be contined in the next line.
     *
     * @param line The line of the description.
     * @return <code>true</code> if the description is continued in the next
     *   line, <code>false</code> otherwise.
     */
    private static boolean toContinue(String line) {
        if (line.isEmpty()) {
            return false;
        }
        
        // continue on backslash
        return line.charAt(line.length() - 1) == '\\';
    }
    
    /**
     * Parses TO-DOs from source file given as lines.
     *
     * @param filename The name of the source file.
     * @param lines The lines.
     * @return The parsed TO-DOs.     
     */
    public List<Todo> parse(String filename,List<String> lines) {
        List<Todo> todos = new ArrayList<Todo>();
        StringBuffer buffer = null;
        
        int lineNo = 0;       
        for (String line:lines) {
            lineNo++;
            
            if (buffer != null) {
                buffer.append(line.trim());
                if (toContinue(line) == false) {
                    todos.add(new Todo(filename,lineNo,buffer.toString()));
                    buffer = null;
                }
                continue;
            }
            
            int indexOf = line.indexOf(TODO_KEYWORD);
            if (indexOf >= 0) {                
                buffer = new StringBuffer(line.substring(
                    indexOf + TODO_KEYWORD.length(),line.length()));
                if (toContinue(line) == false) {
                    todos.add(new Todo(filename,lineNo,buffer.toString()));
                    buffer = null;
                }
            }            
        }
        
        return todos;
    }
}