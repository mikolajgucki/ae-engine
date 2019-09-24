#ifndef AE_LUA_VUNGLE_H
#define AE_LUA_VUNGLE_H

#include "DLog.h"
#include "Error.h"
#include "LuaEngine.h"
#include "LuaLibVungle.h"

namespace ae {
    
namespace vungle {
    
namespace lua {
    
/**
 * \brief Loads the Vungle library.
 * \param log The log.
 * \param luaEngine The Lua engine.
 * \param luaLibVungle The Vungle Lua library implementation.
 * \param error The error to set if loading fails.
 */
void loadVungleLib(DLog *log,::ae::engine::LuaEngine *luaEngine,
    LuaLibVungle *luaLibVungle,Error *error);
   
/**
 * \brief Unloads the Vungle library.
 * \param log The log.
 * \param luaLibVungle The Vungle Lua library implementation.
 */
void unloadVungleLib(DLog *log,LuaLibVungle *luaLibVungle);

} // namespace
    
} // namespace

} // namespace

#endif // AE_LUA_VUNGLE_H