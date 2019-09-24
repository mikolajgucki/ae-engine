/*
-- @module adcolony
-- @group Advert
*/
#include <string>
#include <iostream>
#include <memory>

#include "DLog.h"
#include "LuaPCall.h"
#include "LuaAdColonyCallback.h"
#include "lua_adcolony.h"

using namespace std;
using namespace ae::lua;
using namespace ae::engine;

namespace ae {

namespace adcolony {
    
namespace lua {
    
/// The log tag.
static const char *logTag = "AE/AdColony";

/// The name of the AdColony library.
static const char *adcolonyName = "adcolony";

/// The name of the Lua global holding the AdColony implementation.
static const char *adcolonyGlobalLuaLibAdColony = "ae_adcolony";
    
/**
 * \brief Gets the AdColony implementation from the Lua state.
 * \param L The Lua state.
 * \return The AdColony implementation.
 */
static LuaLibAdColony *getLuaLibAdColony(lua_State *L) {
    lua_getglobal(L,adcolonyGlobalLuaLibAdColony);
    LuaLibAdColony *lib = (LuaLibAdColony *)lua_touserdata(L,-1);  
    lua_pop(L,1);
    return lib;    
}

/*
-- @name .requestInterstitial
-- @func
-- @brief Requests an interstitial to be loaded.
-- @param zoneId The zone identifier.
 */
static int adColonyRequestInterstitial(lua_State *L) {
    LuaLibAdColony *luaLibAdColony = getLuaLibAdColony(L);
    const char *zoneId = luaL_checkstring(L,1);
    luaLibAdColony->requestInterstitial(string(zoneId));
    
    return 0;
}

/*
-- @name .showInterstitial
-- @func
-- @brief Shows an interstitial.
-- @param zoneId The zone identifier.
 */
static int adColonyShowInterstitial(lua_State *L) {
    LuaLibAdColony *luaLibAdColony = getLuaLibAdColony(L);
    const char *zoneId = luaL_checkstring(L,1);
    luaLibAdColony->showInterstitial(string(zoneId));
    
    return 0;
}

/*
-- @name .isInterstitialExpired
-- @func
-- @brief Checks if an interstitial is expired.
-- @param zoneId The zone identifier.
-- @return `true` if expired, `false` otherwise.
 */
static int adColonyIsInterstitialExpired(lua_State *L) {
    LuaLibAdColony *luaLibAdColony = getLuaLibAdColony(L);
    const char *zoneId = luaL_checkstring(L,1);
    
    if (luaLibAdColony->isInterstitialExpired(string(zoneId)) == true) {
        return AE_LUA_TRUE;
    }
    return AE_LUA_FALSE;
}

/** */
static const struct luaL_Reg adcolonyFuncs[] = {
    {"requestInterstitial",adColonyRequestInterstitial},
    {"showInterstitial",adColonyShowInterstitial},
    {"isInterstitialExpired",adColonyIsInterstitialExpired},
    {0,0}
};

/** */
static int adcolonyRequireFunc(lua_State *L) {
    luaL_newlib(L,adcolonyFuncs);
    return 1;
}

/** */
void loadAdColonyLib(DLog *log,LuaEngine *luaEngine,
    LuaLibAdColony *luaLibAdColony,Error *error) {
//
    log->trace(logTag,"loadAdColonyLib()");
    lua_State *L = luaEngine->getLuaState();
    
// global with AdColony
    lua_pushlightuserdata(L,luaLibAdColony);
    lua_setglobal(L,adcolonyGlobalLuaLibAdColony);
    
// load the library
    luaL_requiref(L,adcolonyName,adcolonyRequireFunc,1);
    lua_pop(L,1);    
    
// log
    luaLibAdColony->setDLog(log);
    
// callback
    AdColonyCallback *callback = new (nothrow) LuaAdColonyCallback(luaEngine);
    if (callback == (AdColonyCallback *)0) {
        log->error(logTag,"Failed to create Lua AdColony callback. No memory.");
        error->setNoMemoryError();        
        return;        
    }
    luaLibAdColony->setCallback(callback);
    
// load the Lua source (the global 'adcolony' already exists at this point so
// the Lua sources must be kept in the directory 'ae')
    LuaPCall luaPCall;
    if (luaPCall.require(L,"ae.adcolony") == false) {
        error->setError(luaPCall.getError());
        return;
    }
}

/** */
void unloadAdColonyLib(DLog *log,LuaLibAdColony *luaLibAdColony) {
    log->trace(logTag,"unloadAdColonyLib()");    
    luaLibAdColony->setCallback((AdColonyCallback *)0);
}
    
} // namespace

} // namespace

} // namespace