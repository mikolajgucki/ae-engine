#ifndef AE_LUA_GPGS_H
#define AE_LUA_GPGS_H

#include "DLog.h"
#include "LuaEngine.h"
#include "LuaLibGPGS.h"

namespace ae {

namespace gpgs {
    
namespace lua {

/**
 * \brief Loads the Google Play Game Services library.
 * \param log The log.
 * \param luaEngine The Lua engine.
 * \param luaLibGpgs The implementation of the Google Play Game Serivces
 *   library. 
 * \param error The error to set if loading fails.
 */
void loadGPGSLib(DLog *log,::ae::engine::LuaEngine *luaEngine,
    LuaLibGPGS *luaLibGpgs,Error *error);    
    
/**
 * \brief Unloads the Google Play Game Services library.
 * \param log The log.
 * \param luaLibGpgs The implementation of the Google Play Game Serivces
 *   library. 
 */
void unloadGPGSLib(DLog *log,LuaLibGPGS *luaLibGpgs);

} // namespace

} // namespace

} // namespace

#endif //  AE_LUA_GPGS_H