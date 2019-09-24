/*
-- @module gamecenter
-- @group Services
*/
#include "LuaPCall.h"
#include "LuaGameCenterCallback.h"
#include "lua_gamecenter.h"

using namespace std;
using namespace ae;
using namespace ae::lua;
using namespace ae::engine;

namespace ae {

namespace gamecenter {
    
namespace lua {
    
/// The log tag.
static const char *logTag = "AE/GameCenter";    

/// The name of the Game Center library.
static const char *gameCenterName = "gamecenter";

/// The name of the Lua global holding the Game Center Lua library
/// implementation.
static const char *gameCenterGlobalLuaLibGameCenter = "ae_gamecenter";        

/**
 * \brief Gets the Game Center implementation from the Lua state.
 * \param L The Lua state.
 * \return The Game Center implementation.
 */
static LuaLibGameCenter *getLuaLibGameCenter(lua_State *L) {
    lua_getglobal(L,gameCenterGlobalLuaLibGameCenter);
    LuaLibGameCenter *lib = (LuaLibGameCenter *)lua_touserdata(L,-1);    
    lua_pop(L,1);
    return lib;
}

/*
-- @name .show
-- @func
-- @brief Shows the Game Center UI.
*/
static int gameCenterShow(lua_State *L) {
    LuaLibGameCenter *gameCenter = getLuaLibGameCenter(L);
    gameCenter->show();
    
    return 0;
}

/*
-- @name .authenticate
-- @func
-- @brief Authenticates the local player.
*/
static int gameCenterAuthenticate(lua_State *L) {
    LuaLibGameCenter *gameCenter = getLuaLibGameCenter(L);
    gameCenter->authenticate();
    
    return 0;
}

/*
-- @name .reportScore
-- @func
-- @brief Reports score.
-- @param leaderboardId The leaderboard identifier.
-- @param score The score.
*/
static int gameCenterReportScore(lua_State *L) {
    LuaLibGameCenter *gameCenter = getLuaLibGameCenter(L);
    
// arguments
    const char *leaderboardIdCStr = luaL_checkstring(L,1);
    long score = (long)luaL_checknumber(L,2);
    
// report
    string leaderboardId(leaderboardIdCStr);
    gameCenter->reportScore(leaderboardId,score);
    
    return 0;
}

/*
-- @name .reportAchievement
-- @func
-- @brief Reports progress on achievement with 100% progress.
-- @param achievementId The achievement identifier.
-- @func
-- @brief Reports progress on achievement.
-- @param achievementId The achievement identifier.
-- @param percentComplete The achievement progress in percents.
*/
static int gameCenterReportAchievement(lua_State *L) {
    LuaLibGameCenter *gameCenter = getLuaLibGameCenter(L);
    
// arguments
    const char *achievementIdCStr = luaL_checkstring(L,1);
    double percentComplete = 100;
    if (lua_isnoneornil(L,2) == 0) {
        percentComplete = (double)luaL_checknumber(L,2);
    }        
    
// report
    string achievementId(achievementIdCStr);
    gameCenter->reportAchievement(achievementId,percentComplete);
    
    return 0;
}

/*
-- @name .isAchievementCompleted
-- @func
-- @brief Checks if an achievement is completed.
-- @param achievementId The achievement identifier.
-- @return `true` if completed, `false` if not completed,
--   `nil` if there is no such achievement or achievements
--   haven't been loaded.
 */
static int gameCenterIsAchievementCompleted(lua_State *L) {
    LuaLibGameCenter *gameCenter = getLuaLibGameCenter(L);
    
// arguments
    const char *achievementIdCStr = luaL_checkstring(L,1);
    string achievementId(achievementIdCStr);

// check
    bool completed;
    if (gameCenter->isAchievementCompleted(
        achievementId,completed) == false) {
    //
        lua_pushnil(L);
    }
    else {
        lua_pushboolean(L,completed ? AE_LUA_TRUE : AE_LUA_FALSE);
    }
    
    return 1;
}

/*
-- @name .getAchievementProgress
-- @func
-- @brief Gets achievement progress.
-- @param achievementId The achievement identifier.
-- @return The achievement progress is percent or
--   `nil` if there is no such achievement or achievements
--   haven't been loaded.
*/
static int gameCenterGetAchievementProgress(lua_State *L) {
    LuaLibGameCenter *gameCenter = getLuaLibGameCenter(L);
        
// arguments
    const char *achievementIdCStr = luaL_checkstring(L,1);
    string achievementId(achievementIdCStr);
        
// check
    double progress;
    if (gameCenter->getAchievementProgress(
        achievementId,progress) == false) {
    //
        lua_pushnil(L);
    }
    else {
        lua_pushnumber(L,progress);
    }
    
    return 1;
}
    
/*
-- @name .loadAchievements
-- @func
-- @brief Loads the achievements.
*/
static int gameCenterLoadAchievements(lua_State *L) {
    LuaLibGameCenter *gameCenter = getLuaLibGameCenter(L);
    gameCenter->loadAchievements();
    
    return 0;
}

/*
-- @name .resetAchievements
-- @func
-- @brief Resets the achievements.
*/
static int gameCenterResetAchievements(lua_State *L) {
    LuaLibGameCenter *gameCenter = getLuaLibGameCenter(L);
    gameCenter->resetAchievements();
    
    return 0;
}

/** */
static const struct luaL_Reg gameCenterFuncs[] = {
    {"show",gameCenterShow},
    {"authenticate",gameCenterAuthenticate},
    {"reportScore",gameCenterReportScore},
    {"reportAchievement",gameCenterReportAchievement},
    {"isAchievementCompleted",gameCenterIsAchievementCompleted},
    {"getAchievementProgress",gameCenterGetAchievementProgress},
    {"loadAchievements",gameCenterLoadAchievements},
    {"resetAchievements",gameCenterResetAchievements},
    {0,0}
};
    
/** */
static int gameCenterRequireFunc(lua_State *L) {
    luaL_newlib(L,gameCenterFuncs);
    return 1;
}

/** */
void loadGameCenterLib(DLog *log,LuaEngine *luaEngine,
    LuaLibGameCenter *luaLibGameCenter,Error *error) {
//
    log->trace(logTag,"loadGameCenterLib()");
    lua_State *L = luaEngine->getLuaState();

// Lua global with Game Center
    lua_pushlightuserdata(L,luaLibGameCenter);
    lua_setglobal(L,gameCenterGlobalLuaLibGameCenter);
    
// load the library
    luaL_requiref(L,gameCenterName,gameCenterRequireFunc,1);
    lua_pop(L,1);
    
// callback
    GameCenterCallback *callback =
        new (nothrow) LuaGameCenterCallback(luaEngine);
    if (callback == (GameCenterCallback *)0) {
        log->error(logTag,
            "Failed to create Lua Game Center callback. No memory.");
        error->setNoMemoryError();        
        return;                
    }
    luaLibGameCenter->setCallback(callback);
    
// load the Lua source (the global 'gamecenter' already exists at this point so
// the Lua sources must be kept in the directory 'ae')
    LuaPCall luaPCall;
    if (luaPCall.require(L,"ae.gamecenter") == false) {
        error->setError(luaPCall.getError());
        return;
    }    
    
    luaLibGameCenter->init();
}
    
/** */
void unloadGameCenterLib(DLog *log,LuaLibGameCenter *luaLibGameCenter) {
    log->trace(logTag,"unloadGameCenterLib()");
    luaLibGameCenter->setCallback((GameCenterCallback *)0);
}

} // namespace

} // namespace

} // namespace
