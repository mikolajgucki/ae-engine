package com.andcreations.ae.desktop;

/**
 * Log level.
 *
 * @author Mikolaj Gucki
 */
public enum LogLevel {
    /** */
    TRACE(1,"TRCE"),
    
    /** */
    DEBUG(2,"DEBG"),
    
    /** */
    INFO(3,"INFO"),
    
    /** */
    WARNING(4,"WARN"),
    
    /** */
    ERROR(5,"EROR");

    /** The log level shortcut. */
    private String shortcut;
    
    /** The log level identifier. */
    private int id;
    
    /** */
    private LogLevel(int id,String shortcut) {
        this.id = id;
        this.shortcut = shortcut;
    }
    
    /** */
    @Override
    public String toString() {
        return shortcut;
    }
    
    /** */
    public static LogLevel getById(int id) {
        for (LogLevel level:values()) {
            if (level.id == id) {
                return level;
            }
        }
        
        return null;
    }
}