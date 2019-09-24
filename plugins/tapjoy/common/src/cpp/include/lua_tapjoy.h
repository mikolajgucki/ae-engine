#ifndef AE_LUA_TAPJOY_H
#define AE_LUA_TAPJOY_H

#include "DLog.h"
#include "Error.h"
#include "LuaEngine.h"
#include "LuaLibTapjoy.h"

namespace ae {
    
namespace tapjoy {
    
namespace lua {
    
/**
 * \brief Loads the Tapyoy library.
 * \param log The log.
 * \param luaEngine The Lua engine.
 * \param luaLibTapjoy The implementation of the Tapjoy Lua library.
 * \param error The error to set if loading fails. 
 */    
void loadTapjoyLib(DLog *log,::ae::engine::LuaEngine *luaEngine,
    LuaLibTapjoy *luaLibTapjoy,Error *error);

/**
 * \brief Unloads the Tapjoy library.
 * \param log The log.
 * \param luaLibTapjoy The implementation of the Tapjoy Lua library.
 */    
void unloadTapjoyLib(DLog *log,LuaLibTapjoy *luaLibTapjoy);
    
} // namespace

} // namespace

} // namespace

#endif // AE_LUA_TAPJOY_H