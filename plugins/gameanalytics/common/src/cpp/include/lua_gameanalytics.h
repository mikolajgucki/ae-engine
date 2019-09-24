#ifndef AE_LUA_GAME_ANALYTICS_H
#define AE_LUA_GAME_ANALYTICS_H

#include "Error.h"
#include "LuaEngine.h"
#include "LuaLibGameAnalytics.h"

namespace ae {
    
namespace gameanalytics {
    
namespace lua {

/**
 * \brief Loads the Game Analytics library.
 * \param luaEngine The Lua engine.
 * \param luaLibFlurry The Game Analytics Lua library implementation.
 * \param error The error to set if loading fails.
 */
void loadGameAnalyticsLib(::ae::engine::LuaEngine *luaEngine,
    LuaLibGameAnalytics *luaLibGameAnalytics,Error *error);

} // namespace
    
} // namespace
    
} // namespace

#endif // AE_LUA_GAME_ANALYTICS_H