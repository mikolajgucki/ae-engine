/*
-- @module chartboost
-- @group Advert
*/
#include <memory>

#include "LuaPCall.h"
#include "LuaLibChartboost.h"
#include "LuaChartboostCallback.h"
#include "lua_chartboost.h"

using namespace std;
using namespace ae;
using namespace ae::lua;
using namespace ae::engine;

namespace ae {

namespace chartboost {
    
namespace lua {
    
/// The log tag.
static const char *logTag = "AE/Chartboost";
    
/// The name of the Chartboost library.
static const char *chartboostName = "chartboost";

/// The name of the Lua global holding the Chartboost.
static const char *chartboostGlobalLuaLibChartboost = "ae_chartboost";

/**
 * \brief Gets the Chartboost Lua library implementation from the Lua state.
 * \param L The Lua state.
 * \return The Chartboost Lua library implementation.
 */
static LuaLibChartboost *getLuaLibChartboost(lua_State *L) {
    lua_getglobal(L,chartboostGlobalLuaLibChartboost);
    LuaLibChartboost *lib = (LuaLibChartboost *)lua_touserdata(L,-1);
    lua_pop(L,1);
    return lib;
}

/*
-- @name .cacheInterstitial
-- @func
-- @brief Caches an interstitial.
-- @param location The location.
*/
static int chartboostCacheInterstitial(lua_State *L) {
    LuaLibChartboost *luaLibChartboost = getLuaLibChartboost(L);
    const char *location = lua_tostring(L,1);
    luaLibChartboost->cacheInterstitial(location);
    
    return 0;
}

/*
-- @name .showInterstitial
-- @func
-- @brief Shows an interstitial.
-- @param location The location.
*/
static int chartboostShowInterstitial(lua_State *L) {
    LuaLibChartboost *luaLibChartboost = getLuaLibChartboost(L);
    const char *location = lua_tostring(L,1);
    luaLibChartboost->showInterstitial(location);
    
    return 0;
}

/** */
static const struct luaL_Reg chartboostFuncs[] = {
    {"cacheInterstitial",chartboostCacheInterstitial},
    {"showInterstitial",chartboostShowInterstitial},
    {0,0}
};

/** */
static int chartboostRequireFunc(lua_State *L) {
    luaL_newlib(L,chartboostFuncs);
    return 1;
}

/** */
void loadChartboostLib(DLog *log,LuaEngine *luaEngine,
    LuaLibChartboost *luaLibChartboost,Error *error) {
//
    log->trace(logTag,"loadChartboostLib()");
    lua_State *L = luaEngine->getLuaState();
    
// global with Chartboost
    lua_pushlightuserdata(L,luaLibChartboost);
    lua_setglobal(L,chartboostGlobalLuaLibChartboost);
    
// load the library
    luaL_requiref(L,chartboostName,chartboostRequireFunc,1);
    
// callback
    LuaChartboostCallback *callback =
        new (nothrow) LuaChartboostCallback(luaEngine);
    if (callback == (LuaChartboostCallback *)0) {
        log->error(logTag,
            "Failed to create Lua Chartboost callback. No memory");
        error->setNoMemoryError();
        return;
    }
    luaLibChartboost->setCallback(callback);
    
// load the Lua source (the global 'chartboost' already exists at this point so
// the Lua sources must be kept in the directory 'ae')
    LuaPCall luaPCall;
    if (luaPCall.require(L,"ae.chartboost") == false) {
        error->setError(luaPCall.getError());
        return;
    }    
    
// initialize    
    luaLibChartboost->init();
}
    
/** */
void unloadChartboostLib(DLog *log,LuaLibChartboost *luaLibChartboost) {
    log->trace(logTag,"unloadChartboostLib()");
    luaLibChartboost->setCallback((ChartboostCallback *)0);
}

} // namespace
    
} // namespace
    
} // namespace