package com.andcreations.ae.studio.log;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author Mikolaj Gucki
 */
public class Log {
    /** */
    private static void log(String level,String msg) {
        System.out.println("[" + level + "] " + msg);
    }
    
    /** */
    public static void trace(String msg) {
        log("TRACE",msg);
    }
    
    /** */
    public static void debug(String msg) {
        log("DEBUG",msg);
    }
    
    /** */    
    public static void info(String msg) {
        log("INFO ",msg);
    }
    
    /** */
    public static void warning(String msg) {
        log("WARN ",msg);
    }
        
    /** */
    public static void error(String msg) {
        log("ERROR",msg);
    }    
    
    /** */
    public static void error(String msg,Throwable exception) {
        log("ERROR",msg);
        exception.printStackTrace();
    }
    
    /** */
    public static void fatal(String msg) {
        log("FATAL",msg);
    }
    
    /** */
    public static void fatal(String msg,Throwable exception) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream print = new PrintStream(output);
        exception.printStackTrace(print);
        log("FATAL",String.format("%s\n%s",msg,output.toString()));
    }
}
