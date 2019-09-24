/*
-- @module gpgs
-- @group Services
*/
#include <string>
#include <memory>
#include <iostream>

#include "lua_common.h"
#include "LuaGPGSCallback.h"
#include "lua_gpgs.h"

using namespace std;
using namespace ae;
using namespace ae::engine;

namespace ae {

namespace gpgs {
    
namespace lua {
    
/// The log tag.
static const char *logTag = "AE/GPGS";    

/// The name of the GPGS library.
static const char *gpgsName = "gpgs";

/// The name of the Lua global holding the GPGS Lua library implementation.
static const char *gpgsGlobalLuaLibGpgs = "ae_gpgs";
    
/**
 * \brief Gets the GPGS from the Lua state.
 * \param L The Lua state.
 * \return The GPGS implementation.
 */
static LuaLibGPGS *getGPGS(lua_State *L) {
    lua_getglobal(L,gpgsGlobalLuaLibGpgs);
    LuaLibGPGS *lib = (LuaLibGPGS *)lua_touserdata(L,-1);
    lua_pop(L,1);
    return lib;    
}

/*
-- @name .signIn
-- @func
-- @brief Signs in.
*/
static int gpgsSignIn(lua_State *L) {
    LuaLibGPGS *gpgs = getGPGS(L);
    gpgs->signIn();
    
    return 0;
}

/*
-- @name .signOut
-- @func
-- @brief Signs out.
*/
static int gpgsSignOut(lua_State *L) {
    LuaLibGPGS *gpgs = getGPGS(L);
    gpgs->signOut();
    
    return 0;
}

/*
-- @name .isSignedIn
-- @func
-- @brief Indicates if a player is signed in.
-- @return `true` if signed in, `false` otherwise.
*/
static int gpgsIsSignedIn(lua_State *L) {
    LuaLibGPGS *gpgs = getGPGS(L);
    
    if (gpgs->isSignedIn() == true) {
        lua_pushboolean(L,AE_LUA_TRUE);
    }
    else {
        lua_pushboolean(L,AE_LUA_FALSE);
    }
    
    return 1;
}

/*
-- @name .unlockAchievement
-- @func
-- @brief Unlocks an achievement.
-- @param achievement The achievement identifier.
*/
static int gpgsUnlockAchievement(lua_State *L) {
    LuaLibGPGS *gpgs = getGPGS(L);
    const char *achievement = lua_tostring(L,1);
    
    gpgs->unlockAchievement(string(achievement));    
    return 0;
}

/*
-- @name .incrementAchievement
-- @func
-- @brief Increments an achievement by 1 step.
-- @param achievement The achievement identifier.
-- @func
-- @brief Increments an achievement.
-- @param achievement The achievement identifier.
-- @param steps The number of steps to increment.
*/
static int gpgsIncrementAchievement(lua_State *L) {
    LuaLibGPGS *gpgs = getGPGS(L);
    
    const char *achievement = lua_tostring(L,1);
    int steps = 1;
    if (lua_isnoneornil(L,2) == 0) {
        steps = (int)luaL_checknumber(L,2);
    }
    
    gpgs->incrementAchievement(string(achievement),steps);    
    return 0;
}

/*
-- @name .setAchievementSteps
-- @func
-- @brief Sets an achievement to have steps.
-- @param achievement The achievement identifier.
-- @param steps The number of steps.
*/
static int gpgsSetAchievementSteps(lua_State *L) {
    LuaLibGPGS *gpgs = getGPGS(L);
    
    const char *achievement = lua_tostring(L,1);
    int steps = (int)luaL_checknumber(L,2);
    
    gpgs->setAchievementSteps(string(achievement),steps);    
    return 0;
}

/*
-- @name .submitScore
-- @func
-- @brief Submits score.
-- @param leaderboardId The leaderboard identifier.
-- @param score The score.
*/
static int gpgsSubmitScore(lua_State *L) {
    LuaLibGPGS *gpgs = getGPGS(L);
    
    const char *leaderboardId = lua_tostring(L,1);
    long score = (long)luaL_checknumber(L,2);
    
    gpgs->submitScore(string(leaderboardId),score);    
    return 0;
}

/*
-- @name .displayAchievements
-- @func
-- @brief Displays achievements.
*/
static int gpgsDisplayAchievements(lua_State *L) {
    LuaLibGPGS *gpgs = getGPGS(L);
    gpgs->displayAchievements();
    
    return 0;
}

/*
-- @name .displayLeaderboard
-- @func
-- @brief Displays a leaderboard.
-- @param leaderboardId The leaderboard identifier.
*/
static int gpgsDisplayLeaderboard(lua_State *L) {
    LuaLibGPGS *gpgs = getGPGS(L);
    
    const char *leaderboardId = lua_tostring(L,1);
    gpgs->displayLeaderboard(string(leaderboardId));

    return 0;    
}

/** */
static const struct luaL_Reg gpgsFuncs[] = {
    {"signIn",gpgsSignIn},
    {"signOut",gpgsSignOut},
    {"isSignedIn",gpgsIsSignedIn},
    {"unlockAchievement",gpgsUnlockAchievement},
    {"incrementAchievement",gpgsIncrementAchievement},
    {"setAchievementSteps",gpgsSetAchievementSteps},
    {"submitScore",gpgsSubmitScore},
    {"displayAchievements",gpgsDisplayAchievements},
    {"displayLeaderboard",gpgsDisplayLeaderboard},
    {0,0}
};

/** */
static int gpgsRequireFunc(lua_State *L) {
    luaL_newlib(L,gpgsFuncs);
    return 1;
}

/** */
void loadGPGSLib(DLog *log,LuaEngine *luaEngine,LuaLibGPGS *luaLibGpgs,
    Error *error) {
//
    log->trace(logTag,"loadGPGSLib()");
    lua_State *L = luaEngine->getLuaState();
    
// Lua global with GPGS
    lua_pushlightuserdata(L,luaLibGpgs);
    lua_setglobal(L,gpgsGlobalLuaLibGpgs);

// load the library
    luaL_requiref(L,gpgsName,gpgsRequireFunc,1);
    lua_pop(L,1);

// callback
    GPGSCallback *callback = new (nothrow) LuaGPGSCallback(luaEngine);
    if (callback == (GPGSCallback *)0) {
        log->error(logTag,"Failed to create Lua GPGS callback. No memory.");
        error->setNoMemoryError();        
        return;        
    }
    luaLibGpgs->setCallback(callback);
}
 
/** */
void unloadGPGSLib(DLog *log,LuaLibGPGS *luaLibGpgs) {
    log->trace(logTag,"unloadGPGSLib()");
    luaLibGpgs->setCallback((GPGSCallback *)0);
}

} // namespace

} // namespace

} // namespace