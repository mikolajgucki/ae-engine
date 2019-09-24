/*
-- @module applovin
-- @group Advert
*/
#include <string>

#include "Log.h"
#include "LuaPCall.h"
#include "LuaEngine.h"
#include "LuaLibAppLovin.h"
#include "LuaAppLovinCallback.h"
#include "lua_applovin.h"

using namespace std;
using namespace ae::lua;
using namespace ae::engine;

namespace ae {

namespace applovin {
    
namespace lua {
    
/// The log tag.
static const char *logTag = "AE/AppLovin";
    
/// The name of the AppLovin library.
static const char *applovinName = "applovin";
    
/// The name of the Lua global holding the AppLovin Lua library implementation.
static const char *applovinGlobalLuaLibApplovin = "ae_applovin";

/**
 * \brief Gets the AppLovin from the Lua state.
 * \param L The Lua state.
 * \return The AppLovin implementation.
 */
static LuaLibAppLovin *getLuaLibAppLovin(lua_State *L) {
    lua_getglobal(L,applovinGlobalLuaLibApplovin);
    LuaLibAppLovin *lib = (LuaLibAppLovin *)lua_touserdata(L,-1);
    lua_pop(L,1);
    return lib;    
}

/*
-- @name .applovinIsInterstitalAdReady
-- @func
-- @brief Checks if an interstitial ad is ready for display.
-- @return `true` if ready, `false` otherwise.
*/
static int applovinIsInterstitalAdReady(lua_State *L) {
    LuaLibAppLovin *appLovin = getLuaLibAppLovin(L);
    if (appLovin->isInterstitialAdReadyForDisplay() == true) {
        lua_pushboolean(L,AE_LUA_TRUE);
    }
    else {
        lua_pushboolean(L,AE_LUA_FALSE);
    }
    
    return 1;
}

/*
-- @name .showInterstitalAd
-- @func
-- @brief Shows the interstital ad.
*/
static int applovinShowInterstitalAd(lua_State *L) {
    LuaLibAppLovin *luaLibAppLovin = getLuaLibAppLovin(L);
    luaLibAppLovin->showInterstitialAd();
    
    return 0;
}

/** */
static const struct luaL_Reg applovinFuncs[] = {
    {"isInterstitialAdReady",applovinIsInterstitalAdReady},
    {"showInterstitialAd",applovinShowInterstitalAd},
    {0,0}
};

/** */
static int applovinRequireFunc(lua_State *L) {
    luaL_newlib(L,applovinFuncs);
    return 1;
}

/** */
void loadAppLovinLib(DLog *log,LuaEngine *luaEngine,
    LuaLibAppLovin *luaLibAppLovin,Error *error) {
//
    log->trace(logTag,"loadAppLovinLib()");
    lua_State *L = luaEngine->getLuaState();
    
// global with AppLovin
    lua_pushlightuserdata(L,luaLibAppLovin);
    lua_setglobal(L,applovinGlobalLuaLibApplovin);
    
// load the library
    luaL_requiref(L,applovinName,applovinRequireFunc,1);
    lua_pop(L,1);
    
// log
    luaLibAppLovin->setDLog(log);

// callback
    LuaAppLovinCallback *callback =
        new (nothrow) LuaAppLovinCallback(luaEngine);
    if (callback == (LuaAppLovinCallback *)0) {
        log->error(logTag,"Failed to create Lua AppLovin callback. No memory.");
        error->setNoMemoryError();
        return;
    }
    luaLibAppLovin->setCallback(callback);
    
// load the Lua source (the global 'applovin' already exists at this point so
// the Lua sources must be kept in the directory 'ae')
    LuaPCall luaPCall;
    if (luaPCall.require(L,"ae.applovin") == false) {
        error->setError(luaPCall.getError());
        return;
    }    
}

/** */
void unloadAppLovinLib(DLog *log,LuaLibAppLovin *luaLibAppLovin) {
    log->trace(logTag,"unloadAppLovinLib()");
    luaLibAppLovin->setCallback((AppLovinCallback *)0);
}
    
} // namespace
    
} // namespace
    
} // namespace