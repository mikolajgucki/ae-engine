package com.andcreations.ae.desktop;

import java.io.IOException;
import java.util.List;

/**
 * The interface for the desktop engine.
 *
 * @author Mikolaj Gucki
 */
public interface DesktopEngineListener {
    /**
     * Called when it fails to start the engine.
     *
     * @param exception The reason.
     */
    void failedToRunEngine(Exception exception);
    
    /**
     * Called when a log entry is received.
     *
     * @param level The level.
     * @param tag The tag.
     * @param message The message.
     */
    void log(LogLevel level,String tag,String message);
    
    /**
     * Called when it failed to stream the standard error.
     *
     * @param exception The cause.
     */
    void failedToStreamStandardError(IOException exception);
    
    /**
     * Called when a frame has been rendered.
     */
    void frameRendered();
    
    /**
     * The engine has paused.
     */
    void paused();
    
    /**
     * The engine has resumed.
     */
    void resumed();
    
    /**
     * The engine is being stopped.
     */
    void stopping();
    
    /**
     * Called when the desktop engine process has stopped.
     */
    void processStopped();
    
    /**
     * Called when the volume has been set.
     *
     * @param volume The volume.
     */
    void volumeSet(double volume);
    
    /**
     * Called when the execution has been suspended.
     *
     * @param source The source file in which suspended.
     * @param line The line in which suspended.
     */
    void suspended(String source,int line);
    
    /**
     * Called when the traceback has been received.
     *
     * @param traceback The traceback.
     */
    void receivedTraceback(LuaTraceback traceback);
    
    /**
     * Called when it fails to read traceback.
     *
     * @param exception The reason.
     */
    void failedToReadTraceback(IOException exception);
    
    /**
     * Called when a table has been received.
     *
     * @param tableValues The table values.
     * @param tablePointer The table pointer.
     * @param requestId The request identifier.
     */
    void receivedTable(List<LuaValue> tableValues,String tablePointer,
        String requestId);
    
    /**
     * Called when it fails to read a table.
     *
     * @param exception The reason.
     * @param requestId The request identifier.
     */
    void failedToReadTable(IOException exception,String requestId);    
    
    /**
     * Called when continuing execution after suspension.
     */
    void continuingExecution();
    
    /**
     * Called when globals have been received.
     *
     * @param globals The globals.
     */
    void receivedGlobals(List<LuaValue> globals);
    
    /**
     * Called when it failes to read globals.
     *
     * @param exception The reason.
     */
    void failedToReadGlobals(IOException exception);
    
    /**
     * Called when Lua code execution has finished.
     */
    void doLuaStringFinished();
    
    /**
     * Called when it failed to run Lua code given as string.
     *
     * @param errorMsg The error message.
     */
    void failedToDoLuaString(String errorMsg);
}