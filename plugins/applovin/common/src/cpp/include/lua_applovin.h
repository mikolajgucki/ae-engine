#ifndef AE_LUA_APP_LOVIN_H
#define AE_LUA_APP_LOVIN_H

#include "DLog.h"
#include "Error.h"
#include "LuaEngine.h"
#include "LuaLibAppLovin.h"

namespace ae {
    
namespace applovin {
    
namespace lua {
    
/**
 * \brief Loads the AppLovin library.
 * \param log The log.
 * \param luaEngine The Lua engine.
 * \param luaLibAppLovin The implementation of the AppLovin Lua library.
 * \param error The error to set if loading fails.
 */
void loadAppLovinLib(DLog *log,::ae::engine::LuaEngine *luaEngine,
    LuaLibAppLovin *luaLibAppLovin,Error *error);

/**
 * \brief Unloads the AppLovin library.
 * \param log The log.
 * \param luaLibAppLovin The implementation of the AppLovin Lua library.
 */
void unloadAppLovinLib(DLog *log,LuaLibAppLovin *luaLibAppLovin);
    
} // namespace
    
} // namespace
    
} // namespace

#endif // AE_LUA_APP_LOVIN_H