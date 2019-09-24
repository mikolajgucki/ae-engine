/*
-- @module vungle
-- @group Advert
*/
#include <memory>

#include "LuaPCall.h"
#include "LuaLibVungle.h"
#include "LuaVungleCallback.h"
#include "lua_vungle.h"

using namespace std;
using namespace ae;
using namespace ae::lua;
using namespace ae::engine;

namespace ae {

namespace vungle {
    
namespace lua {
    
/// The log tag.
static const char *logTag = "AE/Vungle";    
    
/// The name of the Vungle library.
static const char *vungleName = "vungle";
    
/// The name of the Lua global holding the Vungle implementation.
static const char *vungleGlobalLuaLibVungle = "ae_vungle";

/**
 * \brief Gets the Vungle Lua library implementation from the Lua state.
 * \param L The Lua state.
 * \return The Vungle Lua library implementation.
 */
static LuaLibVungle *getLuaLibVungle(lua_State *L) {
    lua_getglobal(L,vungleGlobalLuaLibVungle);
    LuaLibVungle *lib = (LuaLibVungle *)lua_touserdata(L,-1);   
    lua_pop(L,1);
    return lib;    
}

/*
-- @name .playAd
-- @func
-- @brief Plays an advert.
*/
static int vunglePlayAd(lua_State *L) {
    LuaLibVungle *luaLibVungle = getLuaLibVungle(L);
    luaLibVungle->playAd();
    
    return 0;
}

/** */
static const struct luaL_Reg vungleFuncs[] = {
    {"playAd",vunglePlayAd},
    {0,0}
};

/** */
static int vungleRequireFunc(lua_State *L) {
    luaL_newlib(L,vungleFuncs);
    return 1;
}

/** */
void loadVungleLib(DLog *log,LuaEngine *luaEngine,LuaLibVungle *luaLibVungle,
    Error *error) {
//
    log->trace(logTag,"loadVungleLib()");
    lua_State *L = luaEngine->getLuaState();
    
// global with Vungle
    lua_pushlightuserdata(L,luaLibVungle);
    lua_setglobal(L,vungleGlobalLuaLibVungle);
    
// load the library
    luaL_requiref(L,vungleName,vungleRequireFunc,1);
    lua_pop(L,1);
    
// callback
    LuaVungleCallback *callback = new (nothrow) LuaVungleCallback(luaEngine);
    if (callback == (LuaVungleCallback *)0) {
        log->error(logTag,"Failed to create Lua Vungle callback. No memory.");
        error->setNoMemoryError();
        return;        
    }
    luaLibVungle->setCallback(callback);
    
// load the Lua source (the global 'vungle' already exists at this point so
// the Lua sources must be kept in the directory 'ae')
    LuaPCall luaPCall;
    if (luaPCall.require(L,"ae.vungle") == false) {
        error->setError(luaPCall.getError());
        return;
    }    
    
// initialize    
    luaLibVungle->init();
}
 
/** */
void unloadVungleLib(DLog *log,LuaLibVungle *luaLibVungle) {
    log->trace(logTag,"unloadVungleLib()");
    luaLibVungle->setCallback((VungleCallback *)0);
}

} // namespace
    
} // namespace
    
} // namespace