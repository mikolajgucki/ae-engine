#ifndef AE_CHARTBOOST_LUA_EXTRA_LIB_H
#define AE_CHARTBOOST_LUA_EXTRA_LIB_H

#include "lua.hpp"
#include "LuaExtraLib.h"
#include "LuaLibChartboost.h"

namespace ae {

namespace chartboost {
    
/**
 * \brief Chartboost extra library.
 */
class ChartboostLuaExtraLib:public ::ae::engine::LuaExtraLib {
    /// The Chartboost Lua library implementation.
    LuaLibChartboost *luaLibChartboost;
    
public:
    /** */
    ChartboostLuaExtraLib(LuaLibChartboost *luaLibChartboost_):LuaExtraLib(),
        luaLibChartboost(luaLibChartboost_) {
    }
    
    /** */     
    virtual ~ChartboostLuaExtraLib() {
        if (luaLibChartboost != (LuaLibChartboost *)0) {
            delete luaLibChartboost;
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

#endif // AE_CHARTBOOST_LUA_EXTRA_LIB_H