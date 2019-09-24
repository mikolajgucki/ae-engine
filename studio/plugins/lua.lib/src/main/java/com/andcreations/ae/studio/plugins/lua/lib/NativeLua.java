package com.andcreations.ae.studio.plugins.lua.lib;

import java.awt.Frame;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.problems.ProblemSeverity;
import com.andcreations.ae.studio.plugins.problems.Problems;
import com.andcreations.ae.studio.plugins.ui.common.MessageDialog;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class NativeLua {
    /** */
    private static final String PROBLEM_SOURCE_ID =
        NativeLua.class.getName();
        
    /**
     * The pattern which matches an error. The 1st group in the pattern
     * is the chunk name, the 2nd one is the line number, the 3rd one is the
     * error message.
     */
    public static final String ERROR_PATTERN = 
        "\\[[^ ]* \"([a-zA-Z0-9/\\._]*)\"\\]:([0-9]+):(.*)";
        
    /** */
    public static native String compile(String src);
    
    /** */
    private static final BundleResources res =
        new BundleResources(NativeLua.class);      
    
    /** */
    private static boolean libLoaded;
     
    /** */
    private static void libLoadFailed(Throwable exception) {
        final String msg = res.getStr("failed.to.load.lib",
            exception.getMessage());
        Log.error(msg);
        
        Problems.get().add(PROBLEM_SOURCE_ID,ProblemSeverity.ERROR,msg);
        UICommon.invoke(new Runnable() {
            /** */
            @Override
            public void run() {
                MessageDialog.error((Frame)null,
                    res.getStr("failed.to.load.lib.title"),msg);
            }
        });
    }
    
    /** */
    static void loadLib() {
        try {
            System.loadLibrary("lua_util");
        } catch (Throwable exception) {
            libLoadFailed(exception);
            return;
        }
        libLoaded = true;        
    }
     
    /**
     * Indicates if the native library is loaded.
     *
     * @return <code>true</code> if loaded, <code>false</code> otherwise.
     */
    public static boolean isLoaded() {
        return libLoaded;
    }
    
    /**
     * Gets the pattern which matches an error. The 1st group in the pattern
     * is the chunk name, the 2nd one is the line number, the 3rd one is the
     * error message.
     *
     * @return The error pattern.
     */
    public static Pattern getErrorPattern() {
        return Pattern.compile(ERROR_PATTERN);
    }
    
    /** */
    private static Matcher matchError(String error)
        throws NativeLuaException {
    //
        Pattern pattern = getErrorPattern();
        Matcher matcher = pattern.matcher(error);
        if (matcher.matches() == false) {
            throw new NativeLuaException(res.getStr(
                "failed.to.parse.error",error));
        }
        return matcher;
    }
    
    /** */
    public static int getErrorLineNumber(String error)
        throws NativeLuaException {
    //
        Matcher matcher = matchError(error);        
        int lineNumber = -1;
        try {
            lineNumber = Integer.parseInt(matcher.group(2));
            if (lineNumber < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException exception) {
            throw new NativeLuaException(res.getStr(
                "invalid.line.number.error",error));            
        }
        return lineNumber;
    }
    
    /** */
    public static String getErrorMessage(String error)
        throws NativeLuaException {
    //
        Matcher matcher = matchError(error);        
        return matcher.group(3);
    }
}
