#ifndef AE_LUA_GAME_CENTER_H
#define AE_LUA_GAME_CENTER_H

#include "DLog.h"
#include "LuaEngine.h"
#include "LuaLibGameCenter.h"

namespace ae {
    
namespace gamecenter {
        
namespace lua {
    
/**
 * \brief Loads the Game Center library.
 * \param log The log.
 * \param luaEngine The Lua engine.
 * \param luaLibGameCenter The implementation of the Game Center library.
 * \param error The error to set if loading fails.
 */
void loadGameCenterLib(DLog *log,::ae::engine::LuaEngine *luaEngine,
    LuaLibGameCenter *luaLibGameCenter,Error *error);
  
/**
 * \brief Unloads the Game Center library.
 * \param log The log.
 * \param luaLibGameCenter The implementation of the Game Center library.
 */
void unloadGameCenterLib(DLog *log,LuaLibGameCenter *luaLibGameCenter);

} // namespace

} // namespace

} // namespace

#endif // AE_LUA_GAME_CENTER_H