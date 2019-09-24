/*
-- @module flurry
-- @group Analytics
*/
#include <memory>

#include "Log.h"
#include "LuaTableUtil.h"
#include "lua_flurry.h"

using namespace std;
using namespace ae::lua;
using namespace ae::engine;

namespace ae {
    
namespace flurry {
    
namespace lua {
    
/// The name of the Flurry library.
static const char *flurryName = "flurry";

/// The name of the Lua global holding the Flurry Lua library implementation.
static const char *flurryGlobalLuaLibFlurry = "ae_flurry";    

/**
 * \brief Gets the Flurry implementation from the Lua state.
 * \param L The Lua state.
 * \return The Flurry implementation.
 */
static LuaLibFlurry *getLuaLibFlurry(lua_State *L) {
    lua_getglobal(L,flurryGlobalLuaLibFlurry);
    LuaLibFlurry *lib = (LuaLibFlurry *)lua_touserdata(L,-1);    
    lua_pop(L,1);
    return lib;
}

/*
-- @name .logEvent
-- @func
-- @brief Logs an event.
-- @param eventId The event identifier.
-- @func
-- @brief Logs an event.
-- @param eventId The event identifier.
-- @param timed Indicates if the event is timed.
-- @func
-- @brief Logs an event.
-- @param eventId The event identifier.
-- @param parameters The table with event parameters.
-- @param timed Indicates if the event is timed.

*/
static int flurryLogEvent(lua_State *L) {
    LuaLibFlurry *luaLibFlurry = getLuaLibFlurry(L);
    
// event identifier
    const char *eventId = luaL_checkstring(L,1);
    
// if has second argument
    if (lua_isnoneornil(L,2) == 0) {
    // if the second argument is a table, then it's the parameters
        if (lua_istable(L,2) != 0) {
        // convert parameter table to map
            LuaTableUtil luaTableUtil;
            map<string,string> parameters;
            if (luaTableUtil.tableToMap(L,2,parameters) == false) {
                luaPushError(L,luaTableUtil.getError().c_str());
                return 0;
            }

        // timed
            if (lua_isnoneornil(L,3) == 0) {
                bool timed = lua_toboolean(L,3) == AE_LUA_TRUE;
                luaLibFlurry->logEvent(string(eventId),parameters,timed);
            }
            else {
                luaLibFlurry->logEvent(string(eventId),parameters,false);
            }
        }
        else { // the 2nd parameter is not a table, must be the timed parameter
            bool timed = lua_toboolean(L,2) == AE_LUA_TRUE;
            luaLibFlurry->logEvent(string(eventId),timed);
        }
    }
    else { // just one argument
        luaLibFlurry->logEvent(string(eventId),false);
    }
    
    return 0;
}

/*
-- @name .endTimedEvent
-- @func
-- @brief Ends a timed event.
-- @param eventId The event identifier.
-- @func
-- @brief Ends a timed event.
-- @param eventId The event identifier.
-- @param parameters The table with event parameters.
*/
static int flurryEndTimedEvent(lua_State *L) {
    LuaLibFlurry *luaLibFlurry = getLuaLibFlurry(L);
    
// event identifier
    const char *eventId = luaL_checkstring(L,1);    

// parameters
    if (lua_isnoneornil(L,2) == 0) {
    // convert parameter table to map
        LuaTableUtil luaTableUtil;
        map<string,string> parameters;
        if (luaTableUtil.tableToMap(L,2,parameters) == false) {
            luaPushError(L,luaTableUtil.getError().c_str());
            return 0;
        }        
        
        luaLibFlurry->endTimedEvent(eventId,parameters);
    }
    else {
        luaLibFlurry->endTimedEvent(eventId);
    }
    
    return 0;
}

/** */
static const struct luaL_Reg flurryFuncs[] = {
    {"logEvent",flurryLogEvent},
    {"endTimedEvent",flurryEndTimedEvent},
    {0,0}
};

/** */
static int flurryRequireFunc(lua_State *L) {
    luaL_newlib(L,flurryFuncs);
    return 1;
}

/** */
void loadFlurryLib(::ae::engine::LuaEngine *luaEngine,
    LuaLibFlurry *luaLibFlurry,Error *error) {
//
    lua_State *L = luaEngine->getLuaState();
    
// Lua global with Flurry
    lua_pushlightuserdata(L,luaLibFlurry);
    lua_setglobal(L,flurryGlobalLuaLibFlurry);
    
// load the library
    luaL_requiref(L,flurryName,flurryRequireFunc,1);
    lua_pop(L,1);
    
// initialize the library
    luaLibFlurry->init();
}
    
} // namespace
    
} // namespace
    
} // namespace