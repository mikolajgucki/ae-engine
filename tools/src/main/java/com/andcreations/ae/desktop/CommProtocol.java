package com.andcreations.ae.desktop;

import java.io.IOException;

import com.andcreations.msgpack.map.MessagePackMap;

/**
 * Provides values and method related to the communication protocol.
 *
 * @author Mikolaj Gucki
 */
public class CommProtocol {
    /** The message identifiers. */
    public static class MsgId {
        /** A log entry. */
        public static final int LOG = 1;
        
        /** Pause the engine. */
        public static final int PAUSE = 2;
        
        /** The engine has been paused. */
        public static final int PAUSED = 3;
        
        /** Resume the engine. */
        public static final int RESUME = 4;
        
        /** The engine has been resumed. */
        public static final int RESUMED = 5;
        
        /** Restart the engine. */
        public static final int RESTART = 6;
        
        /** Stop the engine. */
        public static final int STOP = 7;
        
        /** The engine is stopping. */
        public static final int STOPPING = 8;
        
        /** Sets the volume. */
        public static final int SET_VOLUME = 9;
        
        /** The volume has been set. */
        public static final int VOLUME_SET = 10;
        
        /** Paused before entering the loop. */
        public static final int PAUSED_BEFORE_ENTERING_LOOP = 11;
        
        /** Let the engine enter the loop. */
        public static final int ENTER_LOOP = 12;
        
        /** Adds a breakpoint. */
        public static final int ADD_BREAKPOINT = 13;
        
        /** Removes a breakpoint. */
        public static final int REMOVE_BREAKPOINT = 14;
        
        /** Execution has been suspended. */
        public static final int SUSPENDED = 15;
        
        /** Continue execution. */
        public static final int CONTINUE_EXECUTION = 16;
        
        /** Steps (into a function). */
        public static final int STEP_INTO = 17;
        
        /** Steps over a function. */
        public static final int STEP_OVER = 18;
        
        /** Returns from a function. */
        public static final int STEP_RETURN = 19;
        
        /** Gets the traceback. */
        public static final int GET_TRACEBACK = 20;
        
        /** The traceback. */
        public static final int TRACEBACK = 21;
        
        /** Continuing execution after suspension. */
        public static final int CONTINUING_EXECUTION = 22;
        
        /** Finds a table by pointer in the traceback. */
        public static final int FIND_TABLE_IN_TRACEBACK = 23;
        
        /** Table. */
        public static final int TABLE = 24;
        
        /** Removes all the breakpoints. */
        public static final int CLEAR_BREAKPOINTS = 25;
        
        /** Gets all the globals. */
        public static final int GET_GLOBALS = 26;
        
        /** The globals. */
        public static final int GLOBALS = 27;
        
        /** Finds a table by pointer in the globals. */
        public static final int FIND_TABLE_IN_GLOBALS = 28;
        
        /** Execution has hooked to a line. */
        public static final int HOOKED = 29;        
        
        /** A frame has been rendered. */
        public static final int FRAME_RENDERED = 30;        
        
        /** Loads and runs a Lua code given as a string. */
        public static final int LUA_DO_STRING = 31;

        /** Lua code execution finished. */
        public static final int MSG_ID_LUA_DO_STRING_FINISHED = 32;
        
        /** Failed to run Lua code given as string. */
        public static final int LUA_DO_STRING_FAILED = 33;
        
        /** Acknowledgement. */
        public static final int ACK = 100;
    }
    
    /** */
    public static final int FALSE = 0;
    
    /** */
    public static final int TRUE = 1;
    
    /** The message identifier key. */
    public static final String MSG_ID_KEY = "msg.id";
    
    /** The request identifier key. */
    public static final String REQUEST_ID_KEY = "request.id";
    
    /** The volume (in percents). */
    public static final String VOLUME_KEY = "volume";
    
    /** Indicates if to send back a notification. */
    public static final String NOTIFY_KEY = "notify";
    
    /** A source file. */
    public static final String SOURCE_KEY = "source";
    
    /** A line. */
    public static final String LINE_KEY = "line";
    
    /** An index. */
    public static final String INDEX_KEY = "index";
    
    /** A count. */
    public static final String COUNT_KEY = "count";
        
    /** A name. */
    public static final String NAME_KEY = "name";
        
    /** A value. */
    public static final String VALUE_KEY = "value";
        
    /** A type. */
    public static final String TYPE_KEY = "type";
    
    /** Indicates a type of a traceback item. */
    public static final String WHAT_KEY = "what";    

    /** Lua value scope. */
    public static final String SCOPE_KEY = "scope"; 

    /** A pointer. */
    public static final String POINTER_KEY = "pointer";
    
    /** An error message. */
    public static final String ERROR_MSG_KEY = "error.msg";
    
    /** */
    public static MessagePackMap newMap(int msgId) {
        MessagePackMap map = new MessagePackMap();
        map.put(MSG_ID_KEY,Integer.valueOf(msgId));
        return map;
    }
    
    /** */
    public static int getBoolean(boolean value) {
        return value ? TRUE : FALSE;
    }
    
    /** */
    public static LuaTracebackItem.What strToWhat(String str)
        throws IOException {
    // 
        if ("lua_func".equals(str)) {
            return LuaTracebackItem.What.LUA_FUNCTION;
        }
        if ("c_func".equals(str)) {
            return LuaTracebackItem.What.C_FUNCTION;
        }
        if ("main_chunk".equals(str)) {
            return LuaTracebackItem.What.MAIN_CHUNK;
        }
        if ("tail_func".equals(str)) {
            return LuaTracebackItem.What.TAIL_FUNCTION;
        }
        if ("unknown".equals(str)) {
            return LuaTracebackItem.What.UNKNOWN;
        }
        
        throw new IOException(String.format("Unknown what value %s",str));
    }
    
    /** */
    public static LuaValue.Scope strToValueScope(String str)    
        throws IOException {
    //
        if ("undefined".equals(str)) {
            return LuaValue.Scope.UNDEFINED;
        }
        if ("local".equals(str)) {
            return LuaValue.Scope.LOCAL;
        }
        if ("upvalue".equals(str)) {
            return LuaValue.Scope.UPVALUE;
        }
        if ("global".equals(str)) {
            return LuaValue.Scope.GLOBAL;
        }
        
        throw new IOException(String.format("Unknown Lua value scope %s",str));
    }
    
    /** */
    public static LuaValue.Type strToValueType(String str)    
        throws IOException {
    //
        if ("nil".equals(str)) {
            return LuaValue.Type.NIL;
        }
        if ("number".equals(str)) {
            return LuaValue.Type.NUMBER;
        }
        if ("boolean".equals(str)) {
            return LuaValue.Type.BOOLEAN;
        }
        if ("string".equals(str)) {
            return LuaValue.Type.STRING;
        }
        if ("table".equals(str)) {
            return LuaValue.Type.TABLE;
        }
        if ("func".equals(str)) {
            return LuaValue.Type.FUNCTION;
        }
        if ("userdata".equals(str)) {
            return LuaValue.Type.USER_DATA;
        }
        if ("thread".equals(str)) {
            return LuaValue.Type.THREAD;
        }
        if ("lightuserdata".equals(str)) {
            return LuaValue.Type.LIGHT_USER_DATA;
        }
        
        throw new IOException(String.format("Unknown Lua value type %s",str));
    }
}