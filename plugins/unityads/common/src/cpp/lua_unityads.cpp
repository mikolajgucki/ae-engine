/*
-- @module unityads
-- @group Advert
*/
#include <memory>

#include "Log.h"
#include "LuaPCall.h"
#include "LuaUnityAdsCallback.h"
#include "lua_unityads.h"

using namespace std;
using namespace ae::lua;
using namespace ae::engine;

namespace ae {

namespace unityads {

namespace lua {
    
/// The log tag.
static const char *logTag = "AE/UnityAds";
    
/// The name of the Unity Ads library.
static const char *unityAdsName = "unityads";

/// The name of the Lua global holding the Unity Ads implementation.
static const char *unityAdsGlobalLuaLibUnityAds = "ae_unityads";
    
/**
 * \brief Gets the Unity Ads implementation from the Lua state.
 * \param L The Lua state.
 * \return The Unity Ads implementation.
 */
static LuaLibUnityAds *getLuaLibUnityAds(lua_State *L) {
    lua_getglobal(L,unityAdsGlobalLuaLibUnityAds);
    LuaLibUnityAds *lib = (LuaLibUnityAds *)lua_touserdata(L,-1);  
    lua_pop(L,1);
    return lib;    
}

/*
-- @name .isReady
-- @func
-- @brief Checks if an advert for the default placement is ready to be shown.
-- @return `true` if ready, `false` otherwise.
-- @func
-- @brief Checks if an advert is ready to be shown.
-- @param placementId The placement identifier.
-- @return `true` if ready, `false` otherwise.
*/
static int unityAdsIsReady(lua_State *L) {
    Log::trace(logTag,"unityAdsIsReady");
    LuaLibUnityAds *luaLibUnityAds = getLuaLibUnityAds(L);
    
// check ready
    bool ready = false;
    if (lua_isnoneornil(L,1)) {
        ready = luaLibUnityAds->isReady();
    }
    else {
        const char *placementId = luaL_checkstring(L,1);
        ready = luaLibUnityAds->isReady(string(placementId));
    }
    
// result
    if (ready == true) {
        lua_pushboolean(L,AE_LUA_TRUE);
    }
    else {
        lua_pushboolean(L,AE_LUA_FALSE);
    }
    
    return 1;
}

/*
-- @name .show
-- @func
-- @brief Shows an advert for the default placement.
-- @func
-- @brief Shows an advert.
-- @param placementId The placement identifier.
*/
static int unityAdsShow(lua_State *L) {
    LuaLibUnityAds *luaLibUnityAds = getLuaLibUnityAds(L);
    
    if (lua_isnoneornil(L,1)) {
        luaLibUnityAds->show();
    }
    else {
        const char *placementId = luaL_checkstring(L,1);
        luaLibUnityAds->show(string(placementId));
    }
    
    return 0;
}

/** */
static const struct luaL_Reg unityAdsFuncs[] = {
    {"isReady",unityAdsIsReady},
    {"show",unityAdsShow},
    {0,0}
};

/** */
static int unityAdsRequireFunc(lua_State *L) {
    luaL_newlib(L,unityAdsFuncs);
    return 1;
}

/** */
void loadUnityAdsLib(DLog *log,LuaEngine *luaEngine,
    LuaLibUnityAds *luaLibUnityAds,Error *error) {
//
    log->trace(logTag,"loadUnityAdsLib()");
    lua_State *L = luaEngine->getLuaState();
    
// global with Unity Ads
    lua_pushlightuserdata(L,luaLibUnityAds);
    lua_setglobal(L,unityAdsGlobalLuaLibUnityAds);
    
// load the library
    luaL_requiref(L,unityAdsName,unityAdsRequireFunc,1);
    lua_pop(L,1);    
    
// log
    luaLibUnityAds->setDLog(log);
    
// callback
    UnityAdsCallback *callback = new (nothrow) LuaUnityAdsCallback(luaEngine);
    if (callback == (UnityAdsCallback *)0) {
        log->error(logTag,"Failed to create Lua Unity Ads callback. No memory.");
        error->setNoMemoryError();
        return;        
    }
    luaLibUnityAds->setCallback(callback);
    
// load the Lua source (the global 'unityads' already exists at this point so
// the Lua sources must be kept in the directory 'ae')
    LuaPCall luaPCall;
    if (luaPCall.require(L,"ae.unityads") == false) {
        error->setError(luaPCall.getError());
        return;
    } 
}

/** */
void unloadUnityAdsLib(DLog *log,LuaLibUnityAds *luaLibUnityAds) {
    log->trace(logTag,"unloadUnityAdsLib()");    
    luaLibUnityAds->setCallback((UnityAdsCallback *)0);   
}

} // namespace

} // namespace

} // namespace