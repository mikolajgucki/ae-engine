#ifndef AE_LUA_AD_COLONY_H
#define AE_LUA_AD_COLONY_H

#include "DLog.h"
#include "Error.h"
#include "LuaEngine.h"
#include "LuaLibAdColony.h"

namespace ae {
    
namespace adcolony {
    
namespace lua {
    
/**
 * \brief Loads the AdColony library.
 * \param log The log.
 * \param luaEngine The Lua engine.
 * \param luaLibAdColony The AdColony library implementation.
 * \param error The error to set if loading fails. 
 */
void loadAdColonyLib(DLog *log,::ae::engine::LuaEngine *luaEngine,
    LuaLibAdColony *luaLibAdColony,Error *error);

/**
 * \brief Unloads the AdColony library.
 * \param log The log.
 * \param luaLibAdColony The AdColony library implementation.
 */
void unloadAdColonyLib(DLog *log,LuaLibAdColony *luaLibAdColony);
    
} // namespace
    
} // namespace
    
} // namespace

#endif // AE_LUA_AD_COLONY_H