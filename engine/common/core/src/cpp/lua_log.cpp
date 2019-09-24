/*
-- @module log
-- @brief Provides log functions.
-- @full Provides log functions. This module is a C library loaded by default.
*/
#include <iostream>

#include "Log.h"
#include "lua_log.h"

namespace ae {
    
namespace lua {
    
/// The library name.
static const char *log_lib_name = "log";

/// The log tag.
static const char *log_tag = "ae.lua";

/*
-- @name .trace
-- @func
-- @brief Logs a message at the trace level.
-- @param message The message.
*/
static int log_trace(lua_State *L) {
    const char *msg = lua_tostring(L,1);
    lua_pop(L,1);
    Log::trace(log_tag,msg);
    
    return 0;
}

/*
-- @name .debug
-- @func
-- @brief Logs a message at the debug level.
-- @param message The message.
*/
static int log_debug(lua_State *L) {
    const char *msg = lua_tostring(L,1);
    lua_pop(L,1);
    Log::debug(log_tag,msg);
    
    return 0;
}

/*
-- @name .info
-- @func
-- @brief Logs a message at the info level.
-- @param message The message.
*/
static int log_info(lua_State *L) {
    const char *msg = lua_tostring(L,1);
    lua_pop(L,1);
    Log::info(log_tag,msg);
    
    return 0;
}

/*
-- @name .warning
-- @func
-- @brief Logs a message at the warning level.
-- @param message The message.
*/
static int log_warning(lua_State *L) {
    const char *msg = lua_tostring(L,1);
    lua_pop(L,1);
    Log::warning(log_tag,msg);
    
    return 0;
}

/*
-- @name .error
-- @func
-- @brief Logs a message at the error level.
-- @param message The message.
*/
static int log_error(lua_State *L) {
    const char *msg = lua_tostring(L,1);
    lua_pop(L,1);
    Log::error(log_tag,msg);
    
    return 0;
}

/** The library functions. */
static const struct luaL_Reg log_lib_funcs[] = {
    {"trace",log_trace},
    {"debug",log_debug},
    {"info",log_info},
    {"warning",log_warning},
    {"error",log_error},
    {0,0}
};

/**
 * \brief The function for luaL_requiref
 * \param L The Lua state. 
 */
static int log_lib_require(lua_State *L) {
    luaL_newlib(L,log_lib_funcs);
    return 1;    
}

/** */
void loadLogLib(lua_State *L) {
    luaL_requiref(L,log_lib_name,log_lib_require,1);
}
    
} // namespace
    
} // namespace