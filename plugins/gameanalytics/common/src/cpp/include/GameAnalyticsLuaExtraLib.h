#ifndef AE_GAME_ANALYTICS_LUA_EXTRA_LIB_H
#define AE_GAME_ANALYTICS_LUA_EXTRA_LIB_H

#include "LuaExtraLib.h"
#include "LuaLibGameAnalytics.h"

namespace ae {
    
namespace gameanalytics {
  
/**
 * \brief The Game Analytics Lua extra library.
 */
class GameAnalyticsLuaExtraLib:public ::ae::engine::LuaExtraLib {
    /// The platform-specific implementation of the Lua library.
    LuaLibGameAnalytics *luaLibGameAnalytics;
    
public:
    /** */
    GameAnalyticsLuaExtraLib(LuaLibGameAnalytics *luaLibGameAnalytics_):
        LuaExtraLib(),luaLibGameAnalytics(luaLibGameAnalytics_) {
    }
    
    /** */
    virtual ~GameAnalyticsLuaExtraLib() {
        if (luaLibGameAnalytics != (LuaLibGameAnalytics *)0) {
            delete luaLibGameAnalytics;
        }
    }
    
    /** */
    virtual const char *getName();
    
    /** */
    virtual bool loadExtraLib(::ae::engine::LuaEngine *luaEngine);
    
    /** */
    virtual bool unloadExtraLib();
};
    
} // namespace
    
} // namespace

#endif // AE_GAME_ANALYTICS_LUA_EXTRA_LIB_H