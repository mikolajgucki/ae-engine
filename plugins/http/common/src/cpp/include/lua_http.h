#ifndef AE_LUA_HTTP_H
#define AE_LUA_HTTP_H

#include "DLog.h"
#include "Error.h"
#include "LuaEngine.h"
#include "LuaLibHTTP.h"

namespace ae {
    
namespace http {
    
namespace lua {
    
/**
 * \brief Loads the HTTP library.
 * \param log The log.
 * \param luaEngine The Lua engine.
 * \param luaLibHttp The implementation of the HTTP Lua library.
 * \param error The error to set if loading fails. 
 */    
void loadHTTPLib(DLog *log,::ae::engine::LuaEngine *luaEngine,
    LuaLibHTTP *luaLibHttp,Error *error);

/**
 * \brief Unloads the HTTP library.
 * \param log The log.
 * \param luaLibHttp The implementation of the HTTP Lua library.
 */    
void unloadHTTPLib(DLog *log,LuaLibHTTP *luaLibHttp);
    
} // namespace

} // namespace
    
} // namespace

#endif // AE_LUA_HTTP_H