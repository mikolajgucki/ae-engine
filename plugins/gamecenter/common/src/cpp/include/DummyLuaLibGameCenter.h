#ifndef AE_DUMMY_LUA_LIB_GAME_CENTER_H
#define AE_DUMMY_LUA_LIB_GAME_CENTER_H

#include "LuaLibGameCenter.h"

namespace ae {
    
namespace gamecenter {
  
/**
 * \brief The dummy implementation of the Game Center Lua library.
 */
class DummyLuaLibGameCenter:public LuaLibGameCenter {
public:
    /** */
    DummyLuaLibGameCenter():LuaLibGameCenter() {
    }
    
    /** */
    virtual ~DummyLuaLibGameCenter() {
    }
    
    /** */
    virtual void init() {
    }
    
    /** */
    virtual void authenticate() {
        notAuthenticated();
    }
    
    /** */
    virtual void show() {
    }
    
    /** */
    virtual void reportScore(const std::string& leaderboardId,long score) {
    }
    
    /** */
    virtual void reportAchievement(const std::string& achievementId,
        double percentComplete) {
    }
    
    /** */
    virtual void loadAchievements() {
    }
    
    /** */
    virtual void resetAchievements() {
    }
};
    
} // namespace
    
} // namespace

#endif // AE_DUMMY_LUA_LIB_GAME_CENTER_H
