#ifndef AE_COMM_PROTOCOL_H
#define AE_COMM_PROTOCOL_H

#include <string>
#include "MessagePackMap.h"
#include "LuaValue.h"
#include "LuaTracebackItem.h"

namespace ae {
    
namespace engine {
    
namespace desktop {
  
/**
 * \brief Provides values and method related to the communication protocol.
 */
class CommProtocol {
public:
    /** */
    enum MsgId {
        /// Log entry.
        MSG_ID_LOG = 1,
        
        /// Pause the engine.
        MSG_ID_PAUSE = 2,
        
        /// The engine has been paused.
        MSG_ID_PAUSED = 3,                
        
        /// Resume the engine.
        MSG_ID_RESUME = 4,
        
        /// The engine has been resumed.
        MSG_ID_RESUMED = 5,                
        
        /// Restart the Lua engine.
        MSG_ID_RESTART = 6,
        
        /// Stop the engine.
        MSG_ID_STOP = 7,
        
        /// The engine is stopping.
        MSG_ID_STOPPING = 8,
        
        /// Sets the volume.
        MSG_ID_SET_VOLUME = 9,
        
        /// Volume has been set.
        MSG_ID_VOLUME_SET = 10,
        
        /// Paused before entering the loop.
        MSG_ID_PAUSED_BEFORE_ENTERING_LOOP = 11,
        
        /// Let the engine enter the loop.
        MSG_ID_ENTER_LOOP = 12,
        
        /// Adds a breakpoint
        MSG_ID_ADD_BREAKPOINT = 13,
        
        /// Removes a breakpoint
        MSG_ID_REMOVE_BREAKPOINT = 14,
        
        /// Execution has been suspended.
        MSG_ID_SUSPENDED = 15,
        
        /// Continue execution.
        MSG_ID_CONTINUE_EXECUTION = 16,
        
        /// Steps (into a function).
        MSG_ID_STEP_INTO = 17,
        
        /// Steps over a function.
        MSG_ID_STEP_OVER = 18,
        
        /// Returns from a function.
        MSG_ID_STEP_RETURN = 19,
        
        /// Gets the traceback.
        MSG_ID_GET_TRACEBACK = 20,
        
        /// The traceback.
        MSG_ID_TRACEBACK = 21,
        
        /// Continuing execution after suspension.
        MSG_ID_CONTINUING_EXECUTION = 22,
        
        /// Finds a table by pointer in the traceback.
        MSG_ID_FIND_TABLE_IN_TRACEBACK = 23,
        
        /// Table.
        MSG_ID_TABLE = 24,
        
        /// Removes all the breakpoints.
        MSG_ID_CLEAR_BREAKPOINTS = 25,
        
        /// Gets all the globals.
        MSG_ID_GET_GLOBALS = 26,
        
        /// The globals.
        MSG_ID_GLOBALS = 27,
        
        /// Finds a table by pointer in the globals.
        MSG_ID_FIND_TABLE_IN_GLOBALS = 28,
        
        /// Execution has hooked to a line.
        MSG_ID_HOOKED = 29,
        
        /// A frame has been rendered.
        MSG_ID_FRAME_RENDERED = 30,
        
        /// Loads and runs a Lua code given as a string.
        MSG_ID_LUA_DO_STRING = 31,
        
        /// Lua code execution finished.
        MSG_ID_LUA_DO_STRING_FINISHED = 32,
        
        /// Failed to run Lua code given as string.
        MSG_ID_LUA_DO_STRING_FAILED = 33
    };
    
    /** */
    enum LogLevel {
        LOG_LEVEL_TRACE = 1,
        LOG_LEVEL_DEBUG = 2,
        LOG_LEVEL_INFO = 3,
        LOG_LEVEL_WARNING = 4,
        LOG_LEVEL_ERROR = 5
    };
    
    /** */
    enum TrueFalse {
        FALSE = 0,
        TRUE = 1
    };        
    
    /** The message identifier key. */
    static const char* const MSG_ID_KEY;
    
    /** The request identifier key. */
    static const char* const REQUEST_ID_KEY;
    
    /** The volume in percent. */
    static const char* const VOLUME_KEY;
    
    /** Indicates if to send back a notification. */
    static const char* const NOTIFY_KEY;
    
    /** A source file. */
    static const char* const SOURCE_KEY;
    
    /** A line . */
    static const char* const LINE_KEY;
    
    /** An index. */
    static const char* const INDEX_KEY;
    
    /** A count. */
    static const char* const COUNT_KEY;
    
    /** A name. */
    static const char* const NAME_KEY;
    
    /** A value. */
    static const char* const VALUE_KEY;
    
    /** A type. */
    static const char* const TYPE_KEY;
    
    /** Indicates a type of a traceback item. */
    static const char* const WHAT_KEY;
    
    /** Lua value scope. */
    static const char* const SCOPE_KEY;
    
    /** A pointer. */
    static const char* const POINTER_KEY;
    
    /** An error message. */
    static const char* const ERROR_MSG_KEY;
    
    /** */
    static void addMsgId(ae::util::MessagePackMap &map,int id);
    
    /** */
    static bool strToBool(ae::util::MessagePackMap &map,
        const std::string &key);
    
    /** */
    static const char* whatToStr(ae::lua::LuaTracebackItem::What what);
    
    /** */
    static const char* valueScopeToStr(ae::lua::LuaValue::Scope scope);
    
    /** */
    static const char *valueTypeToStr(int type);
};
    
} // namespace
    
} // namespace
    
} // namespace

#endif // AE_COMM_PROTOCOL_H