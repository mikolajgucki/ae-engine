package com.andcreations.android.adb;

import com.android.ddmlib.Log.LogLevel;
import com.android.ddmlib.logcat.LogCatMessage;

/**
 * Represents a logcat message.
 *
 * @author Mikolaj Gucki
 */
public class ADBLogCatMessage {
    /** */
    public static enum Level {
        /** */
        VERBOSE,
        
        /** */
        DEBUG,
        
        /** */
        INFO,
        
        /** */
        WARNING,
        
        /** */
        ERROR,
        
        /** */
        ASSERT
    }
    
    /** The original message. */
    private LogCatMessage message;
    
    /** Indicates if the message comes from a given process. The process is
        typically set via ADBProcessLogCat.setProcessName(). */
    private boolean processMatch;
    
    /** */
    ADBLogCatMessage(LogCatMessage message) {
        this.message = message;
    }
   
    /** */
    void setProcessMatch(boolean processMatch) {
        this.processMatch = processMatch;
    }
    
    /** */
    public ADBLogLevel getLogLevel() {
        LogLevel logLevel = message.getLogLevel();
        if (logLevel == LogLevel.VERBOSE) {
            return ADBLogLevel.VERBOSE;
        } else if (logLevel == LogLevel.DEBUG) {
            return ADBLogLevel.DEBUG;
        } else if (logLevel == LogLevel.INFO) {
            return ADBLogLevel.INFO;
        } else if (logLevel == LogLevel.WARN) {
            return ADBLogLevel.WARN;
        } else if (logLevel == LogLevel.ERROR) {
            return ADBLogLevel.ERROR;
        } else if (logLevel == LogLevel.ASSERT) {
            return ADBLogLevel.ASSERT;
        }
        
        return null;
    }
    
    /** */
    public String getPid() {
        return message.getPid();
    }
    
    /** */
    public String getTid() {
        return message.getTid();
    }
    
    /** */
    public String getAppName() {
        return message.getAppName();
    }
    
    /** */
    public String getTag() {
        return message.getTag();
    }
    
    /** */
    public String getTime() {
        return message.getTime();
    }
    
    /** */
    public String getMessage() {
        return message.getMessage();
    }
    
    /** */
    public Level getLevel() {
        LogLevel logLevel = message.getLogLevel();
        if (logLevel == LogLevel.VERBOSE) {
            return Level.VERBOSE;
        }
        if (logLevel == LogLevel.DEBUG) {
            return Level.DEBUG;
        }
        if (logLevel == LogLevel.INFO) {
            return Level.INFO;
        }
        if (logLevel == LogLevel.WARN) {
            return Level.WARNING;
        }
        if (logLevel == LogLevel.ERROR) {
            return Level.ERROR;
        }
        if (logLevel == LogLevel.ASSERT) {
            return Level.ASSERT;
        }
        
        return null;
    }
    
    /**
     * Indicates if the message comes from a given process.
     * 
     * @return <code>true</code> if the messages comes from the process,
     *   <code>false</code> otherwise.
     */
    public boolean processMatch() {
        return processMatch;
    }
}