#ifndef AE_LUA_CHARTBOOST_CALLBACK_H
#define AE_LUA_CHARTBOOST_CALLBACK_H

#include "lua.hpp"
#include "LuaEngine.h"
#include "ChartboostCallback.h"

namespace ae {
    
namespace chartboost {
  
/**
 * \brief The Lua implementation of the Chartboost callback.
 */
class LuaChartboostCallback:public ChartboostCallback {
    /// The Lua engine.
    ::ae::engine::LuaEngine *luaEngine;
    
    /**
     * \brief Calls a function with a string argument.
     * \param funcName The function name.
     * \param arg The function argument.
     */
    void callStrFunc(const char *funcName,const char *arg);
    
public:
    /** */
    LuaChartboostCallback(::ae::engine::LuaEngine *luaEngine_):
        ChartboostCallback(),luaEngine(luaEngine_) {
    }
    
    /** */
    virtual ~LuaChartboostCallback() {
    }
    
    /** */
    virtual void didCacheInterstitial(const char *location);
    
    /** */
    virtual void didFailToLoadInterstitial(const char *location);
    
    /** */
    virtual void didClickInterstitial(const char *location);
    
    /** */
    virtual void didCloseInterstitial(const char *location);
    
    /** */
    virtual void didDismissInterstitial(const char *location);
    
    /** */
    virtual void didDisplayInterstitial(const char *location);
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_CHARTBOOST_CALLBACK_H