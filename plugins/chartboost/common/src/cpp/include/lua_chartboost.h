#ifndef AE_LUA_CHARTBOOST_H
#define AE_LUA_CHARTBOOST_H

#include "DLog.h"
#include "Error.h"
#include "LuaEngine.h"
#include "LuaLibChartboost.h"

namespace ae {
    
namespace chartboost {
    
namespace lua {
    
/**
 * \brief Loads the Chartboost library.
 * \param log The log.
 * \param luaEngine The Lua engine.
 * \param luaLibChartboost The implementation of the Chartboost Lua library.
 * \param error The error to set if loading fails. 
 */
void loadChartboostLib(DLog *log,::ae::engine::LuaEngine *luaEngine,
    LuaLibChartboost *luaLibChartboost,Error *error);
  
/**
 * \brief Unloads the Chartboost library.
 * \param log The log.
 * \param luaLibChartboost The implementation of the Chartboost Lua library.
 */
void unloadChartboostLib(DLog *log,LuaLibChartboost *luaLibChartboost);

} // namespace
    
} // namespace
    
} // namespace

#endif // AE_LUA_CHARTBOOST_H