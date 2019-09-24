#ifndef AE_IOS_LUA_LIB_GAME_CENTER_H
#define AE_IOS_LUA_LIB_GAME_CENTER_H

#include "AEGKGameCenterControllerDelegate.h"
#include "LuaLibGameCenter.h"

/**
 * \brief The iOS implementation of the Game Center Lua library.
 */
class AEiOSLuaLibGameCenter:public ::ae::gamecenter::LuaLibGameCenter {
    /** */
    bool isGameCenterAvailable();
    
    /// The controller delegate.
    AEGKGameCenterControllerDelegate *aeControllerDelegate;
    
    /// The loaded achievements.
    NSArray *achievements;
    
public:
    /** */
    AEiOSLuaLibGameCenter():LuaLibGameCenter(),
        aeControllerDelegate((AEGKGameCenterControllerDelegate *)0),
        achievements(nil) {
    }
    
    /** */
    virtual ~AEiOSLuaLibGameCenter() {
    }
    
    /** */
    virtual void init();
    
    /** */
    virtual void authenticate();
    
    /** */
    virtual void show();
    
    /** */
    virtual void reportScore(const std::string& leaderboardId,long score);
    
    /** */
    virtual void reportAchievement(const std::string& achievementId,
        double percentComplete);
    
    /** */
    virtual bool isAchievementCompleted(const std::string& achievementId,
        bool& completed);
    
    /** */
    virtual bool getAchievementProgress(const std::string& achievementId,
        double& progress);
    
    /** */
    virtual void loadAchievements();    
    
    /** */
    virtual void resetAchievements();    
};

#endif // AE_IOS_LUA_LIB_GAME_CENTER_H
