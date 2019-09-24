package com.andcreations.ae.doc;

/**
 * Represents a TODO.
 *
 * @author Mikolaj Gucki
 */
public class Todo {
    /** The name of the source file. */
    private String filename;
    
    /** The line number at which the to-do was found. */
    private int lineNo;
    
    /** The to-do description. */
    private String desc;
    
    /**
     * Constructs a {@link Todo}.
     *
     * @param filename The name of the source file.
     * @param lineNo The line number at which the to-do was found.
     * @param desc The to-do description.
     */
    public Todo(String filename,int lineNo,String desc) {
        this.filename = filename;
        this.lineNo = lineNo;
        this.desc = desc;
    }
    
    /** */
    public String getFilename() {
        return filename;
    }
    
    /** */
    public int getLineNo() {
        return lineNo;
    }
    
    /** */
    public String getDesc() {
        return desc;
    }
    
    /** */
    public void setDesc(String desc) {
        this.desc = desc;
    }
}