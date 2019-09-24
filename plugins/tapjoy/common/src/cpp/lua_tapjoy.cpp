/*
-- @module tapjoy
-- @group Advert
*/
#include <memory>
#include "LuaTapjoyCallback.h"
#include "lua_tapjoy.h"

using namespace std;
using namespace ae::lua;
using namespace ae::engine;

namespace ae {
    
namespace tapjoy {
    
namespace lua {
    
/// The log tag.
static const char *logTag = "AE/Tapjoy";

/// The library name.
static const char *tapjoyName = "tapjoy";

/// The name of the Lua global holding the Tapjoy Lua library implementation.
static const char *tapjoyGlobalLuaLibTapjoy = "ae_tapjoy";
    
/**
 * \brief Gets the Tapjoy from the Lua state.
 * \param L The Lua state.
 * \return The Tapjoy implementation.
 */
static LuaLibTapjoy *getLuaLibTapjoy(lua_State *L) {
    lua_getglobal(L,tapjoyGlobalLuaLibTapjoy);
    LuaLibTapjoy *lib = (LuaLibTapjoy *)lua_touserdata(L,-1);
    lua_pop(L,1);
    return lib;    
}

/*
-- @name .isConnected
-- @func
-- @brief Indicates if Tapjoy is connected to the server.
-- @return `true` if connected, `false` otherwise.
*/
static int tapjoyIsConnected(lua_State *L) {
    LuaLibTapjoy *luaLibTapjoy = getLuaLibTapjoy(L);
    
    if (luaLibTapjoy->isConnected()) {
        lua_pushboolean(L,AE_LUA_TRUE);
    }
    else {
        lua_pushboolean(L,AE_LUA_TRUE);
    }
    return 1;
}

/*
-- @name .requestContent
-- @func
-- @brief Reqests content for a placement.
-- @param placement The placement name.
*/
static int tapjoyRequestContent(lua_State *L) {
    LuaLibTapjoy *luaLibTapjoy = getLuaLibTapjoy(L);
    
    string placement(luaL_checkstring(L,1));
    luaLibTapjoy->requestContent(placement);
    
    return 0;
}

/*
-- @name .isContentReady
-- @func
-- @brief Checks if content is ready.
-- @param placement The placement name.
-- @return `true` if ready, `false` otherwise.
*/
static int tapjoyIsContentReady(lua_State *L) {
    LuaLibTapjoy *luaLibTapjoy = getLuaLibTapjoy(L);
    
    string placement(luaL_checkstring(L,1));
    if (luaLibTapjoy->isContentReady(placement)) {
        lua_pushboolean(L,AE_LUA_TRUE);
    }
    else {
        lua_pushboolean(L,AE_LUA_TRUE);
    }
    
    return 1;
}

/*
-- @name .showContent
-- @func
-- @brief Shows content for a placement.
-- @param placement The placement name.
*/
static int tapjoyShowContent(lua_State *L) {
    LuaLibTapjoy *luaLibTapjoy = getLuaLibTapjoy(L);
    
    string placement(luaL_checkstring(L,1));
    luaLibTapjoy->showContent(placement);
    
    return 0;
}

/** */
static const struct luaL_Reg tapjoyFuncs[] = {
    {"isConnected",tapjoyIsConnected},
    {"requestContent",tapjoyRequestContent},
    {"isContentReady",tapjoyIsContentReady},
    {"showContent",tapjoyShowContent},
    {0,0}
};

/** */
static int tapjoyRequireFunc(lua_State *L) {
    luaL_newlib(L,tapjoyFuncs);
    return 1;
}

/** */
void loadTapjoyLib(DLog *log,LuaEngine *luaEngine,LuaLibTapjoy *luaLibTapjoy,
    Error *error) {
//
    log->trace(logTag,"loadTapjoyLib()");
    lua_State *L = luaEngine->getLuaState();
    
// global with Tapjoy library
    lua_pushlightuserdata(L,luaLibTapjoy);
    lua_setglobal(L,tapjoyGlobalLuaLibTapjoy);    
    
// load the library
    luaL_requiref(L,tapjoyName,tapjoyRequireFunc,1);
    lua_pop(L,1);    
    
// log
    luaLibTapjoy->setDLog(log);    
    
// callback
    LuaTapjoyCallback *callback = new (nothrow) LuaTapjoyCallback(luaEngine);
    if (callback == (LuaTapjoyCallback *)0) {
        log->error(logTag,"Failed to create Lua Tapjoy callback. No memory.");
        error->setNoMemoryError();
        return;
    }
    luaLibTapjoy->setCallback(callback);        
    
// load the Lua source
    LuaPCall luaPCall;
    if (luaPCall.require(L,"ae.tapjoy") == false) {
        error->setError(luaPCall.getError());
        return;
    }     
}

/** */
void unloadTapjoyLib(DLog *log,LuaLibTapjoy *luaLibTapjoy) {
    log->trace(logTag,"unloadTapjoyLib()");
    luaLibTapjoy->setCallback((TapjoyCallback *)0);    
}
    
} // namespace

} // namespace

} // namespace