package com.andcreations.ae.issue;

import java.io.File;

/**
 * @author Mikolaj Gucki
 */
public class Issue {
    /** */
    public static final int NO_LINE = -1;
    
    /** */
    private String type;
    
    /** */
    private IssueSeverity severity;
    
    /** */
    private String message;
    
    /** */
    private File file;
    
    /** */
    private int line = NO_LINE;
    
    /** */
    public Issue(String type,IssueSeverity severity,String message,File file) {
        this.type = type;
        this.severity = severity;
        this.file = file;
        this.message = message;
    }
    
    /** */
    public Issue(String type,IssueSeverity severity,String message,File file,
        int line) {
    //
        this(type,severity,message,file);
        this.line = line;
    }
    
    /** */
    public Issue(String type,IssueSeverity severity,String message) {
        this(type,severity,message,null);
    }
    
    
    /** */
    public Issue(String type,IssueSeverity severity,String message,int line) {
        this(type,severity,message);
        this.line = line;
    }    
    
    /** */
    public String getType() {
        return type;
    }   
    
    /** */
    public IssueSeverity getSeverity() {
        return severity;
    }
    
    /** */
    public String getMessage() {
        return message;
    }
    
    /** */
    public File getFile() {
        return file;
    }
    
    /** */
    public int getLine() {
        return line;
    }
    
    /** */
    @Override
    public String toString() {
        if (file != null) {
            return String.format("%s[%s@%d]: %s [%s]",
                type,severity,line,message,file.getAbsolutePath());
        }        
        return String.format("%s[%s@%d]: %s",type,severity,line,message);
    }
    
    /** */
    public static Issue warning(String type,String message,File file) {
        return new Issue(type,IssueSeverity.WARNING,message,file);
    }
    
    /** */
    public static Issue warning(String type,String message,File file,int line) {
        return new Issue(type,IssueSeverity.WARNING,message,file,line);
    }
    
    /** */
    public static Issue warning(String type,String message) {
        return new Issue(type,IssueSeverity.WARNING,message);
    }
    
    /** */
    public static Issue warning(String type,String message,int line) {
        return new Issue(type,IssueSeverity.WARNING,message,line);
    }
    
    /** */
    public static Issue error(String type,String message,File file) {
        return new Issue(type,IssueSeverity.ERROR,message,file);
    }
    
    /** */
    public static Issue error(String type,String message,File file,int line) {
        return new Issue(type,IssueSeverity.ERROR,message,file,line);
    }
    
    /** */
    public static Issue error(String type,String message) {
        return new Issue(type,IssueSeverity.ERROR,message);
    }
    
    /** */
    public static Issue error(String type,String message,int line) {
        return new Issue(type,IssueSeverity.ERROR,message,line);
    }
}