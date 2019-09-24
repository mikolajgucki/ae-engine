#ifndef AE_LUA_IAP_H
#define AE_LUA_IAP_H

#include "DLog.h"
#include "Error.h"
#include "LuaEngine.h"
#include "LuaLibIAP.h"

namespace ae {
    
namespace iap {
    
namespace lua {
    
/**
 * \brief Loads the in-app purchases library.
 * \param log The log.
 * \param luaEngine The Lua engine.
 * \param luaLibIap The IAP library implementation.
 * \param error The error to set if loading fails.
 */
void loadIAPLib(DLog *log,::ae::engine::LuaEngine *luaEngine,
    LuaLibIAP *luaLibIap,Error *error);
 
/**
 * \brief Unloads the in-app purchases library.
 * \param log The log.
 * \param luaLibIap The IAP library implementation.
 */
void unloadIAPLib(DLog *log,LuaLibIAP *luaLibIap);

} // namespace
    
} // namespace
    
} // namespace

#endif // AE_LUA_IAP_H