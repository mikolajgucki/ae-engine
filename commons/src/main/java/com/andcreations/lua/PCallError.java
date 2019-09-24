package com.andcreations.lua;

import com.andcreations.system.OS;

/**
 * Represents an error message returned by <code>pcall</code>.
 * 
 * @author Mikolaj Gucki
 */
public class PCallError {
    /** */
    private String file;
    
    /** */
    private int line;
    
    /** */
    private String msg;
    
    /** */
    public PCallError(String file,int line,String msg) {
        this.file = file;
        this.line = line;
        this.msg = msg;
    }
    
    /** */
    public String getFile() {
        return file;
    }
    
    /** */
    public int getLine() {
        return line;
    }
    
    /** */
    public String getMessage() {
        return msg;
    }
    
    /** */
    @Override
    public String toString() {
        return String.format("%s:%d: %s",file,line,msg);
    }
    
    /**
     * Gets the error message from full error message returned by
     * <code>pcall</code>.
     *
     * @param pcallErrorMsg The error message returned by <code>pcall</code>.
     * @return The error message.
     */
    public static PCallError parse(String pcallErrorMsg) {
        int fromIndex = 0;
        
    // skip the first letter and colon on Windows
        if (OS.getOS() == OS.WINDOWS && pcallErrorMsg.charAt(1) == ':') {
            fromIndex = 2;
        }
        
    // file
        int fileColonIndex = pcallErrorMsg.indexOf(':',fromIndex);
        if (fileColonIndex < 0) {
            return new PCallError(null,-1,pcallErrorMsg);
        }
        String file = pcallErrorMsg.substring(0,fileColonIndex);
            
    // line
        int lineColonIndex = pcallErrorMsg.indexOf(':',fileColonIndex + 1);
        if (lineColonIndex < 0) {
            return new PCallError(file,-1,null);
        }
        String lineStr = pcallErrorMsg.substring(
            fileColonIndex + 1,lineColonIndex);
        int line = -1;
        try {
            line = Integer.parseInt(lineStr);
            if (line < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException exception) {
        }
        
    // message
        String msg = pcallErrorMsg.substring(lineColonIndex + 1);
        msg = msg.trim();
        
        return new PCallError(file,line,msg);
    }
    
    public static void main(String[] args) {
        String e = "f:/1/ae/dist/build/dist/test/lua/ae\\test\\runner.lua:135: foobar";
        System.out.println(parse(e));
    }
}
