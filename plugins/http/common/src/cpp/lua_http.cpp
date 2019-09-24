/*
-- @module http
-- @group Net
*/
#include <string>
#include <memory>

#include "LuaPCall.h"
#include "LuaHTTPCallback.h"
#include "lua_http.h"

using namespace std;
using namespace ae::lua;
using namespace ae::engine;

namespace ae {
    
namespace http {
    
namespace lua {
    
/// The log tag.
static const char *logTag = "AE/HTTP";
 
/// The library name.
static const char *httpName = "http";

/// The name of the Lua global holding the HTTP Lua library implementation.
static const char *httpGlobalLuaLibHttp = "ae_http";

/**
 * \brief Gets the HTTP from the Lua state.
 * \param L The Lua state.
 * \return The HTTP implementation.
 */
static LuaLibHTTP *getLuaLibHTTP(lua_State *L) {
    lua_getglobal(L,httpGlobalLuaLibHttp);
    LuaLibHTTP *lib = (LuaLibHTTP *)lua_touserdata(L,-1);
    lua_pop(L,1);
    return lib;    
}

/*
-- @name .get
-- @func
-- @brief Asynchronously sends a GET request.
-- @param id The request identifier.
-- @param url The URL.
*/
static int httpGet(lua_State *L) {
    LuaLibHTTP *luaLibHttp = getLuaLibHTTP(L);
    const char *id = luaL_checkstring(L,1);
    const char *url = luaL_checkstring(L,2);    
    luaLibHttp->get(string(id),string(url));
    
    return 0;
}

/*
-- @name .openURL
-- @func
-- @brief Opens an URL.
-- @param url The URL.
*/
static int httpOpenURL(lua_State *L) {
    LuaLibHTTP *luaLibHttp = getLuaLibHTTP(L);
    const char *url = luaL_checkstring(L,1);    
    luaLibHttp->openURL(string(url));
    
    return 0;
}

/** */
static const struct luaL_Reg httpFuncs[] = {
    {"get",httpGet},
    {"openURL",httpOpenURL},
    {0,0}
};

/** */
static int httpRequireFunc(lua_State *L) {
    luaL_newlib(L,httpFuncs);
    return 1;
}

/** */
void loadHTTPLib(DLog *log,LuaEngine *luaEngine,LuaLibHTTP *luaLibHttp,
    Error *error) {
//
    log->trace(logTag,"loadHTTPLib()");
    lua_State *L = luaEngine->getLuaState();
    
// global with HTTP
    lua_pushlightuserdata(L,luaLibHttp);
    lua_setglobal(L,httpGlobalLuaLibHttp);    
    
// load the library
    luaL_requiref(L,httpName,httpRequireFunc,1);
    lua_pop(L,1);    
    
// callback
    LuaHTTPCallback *callback = new (nothrow) LuaHTTPCallback(luaEngine);
    if (callback == (LuaHTTPCallback *)0) {
        log->error(logTag,"Failed to create Lua HTTP callback. No memory.");
        error->setNoMemoryError();
        return;
    }
    luaLibHttp->setCallback(callback);        
    
// load the Lua source
    LuaPCall luaPCall;
    if (luaPCall.require(L,"ae.http") == false) {
        error->setError(luaPCall.getError());
        return;
    }    
}

/** */
void unloadHTTPLib(DLog *log,LuaLibHTTP *luaLibHttp) {
    log->trace(logTag,"unloadHTTPLib()");
    luaLibHttp->setCallback((HTTPCallback *)0);  
}
    
} // namespace

} // namespace
    
} // namespace