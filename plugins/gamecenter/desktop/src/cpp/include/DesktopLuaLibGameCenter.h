#ifndef AE_DESKTOP_LUA_LIB_GAME_CENTER_H
#define AE_DESKTOP_LUA_LIB_GAME_CENTER_H

#include "DLog.h"
#include "Timer.h"
#include "DesktopPluginCfg.h"
#include "LuaLibGameCenter.h"

namespace ae {
    
namespace gamecenter {
  
/**
 * \brief The desktop implementation of the Game Center Lua libarary.
 */
class DesktopLuaLibGameCenter:public LuaLibGameCenter {
    /** */
    enum {
        /** */
        DEFAULT_LOGIN_VIEW_TIME = 1800,
        
        /** */
        DEFAULT_LOGIN_VIEW_DURATION = 2400,
        
        /** */
        DEFAULT_AUTHENTICATE_TIME = 6200,
        
        /** */
        DEFAULT_LOAD_ACHIEVEMENTS_DURATION = 1500,
    };
    
    /// The log.
    DLog *log;
    
    /// The plugin configuration.
    ::ae::engine::desktop::DesktopPluginCfg *cfg;
    
    /// The timer.
    ::ae::engine::Timer *timer;
    
    /// Indicates if to show the login view
    bool loginViewFlag;
    
    /// The time after which the login is shown.
    int loginViewTime;
    
    /// The duration for which the login view is visible
    int loginViewDuration;
    
    /// Indicates if to authenticate the player.
    bool authenticateFlag;
    
    /// The time after which the (not)authenticated event is fired.
    int authenticateTime;
    
    /// The duration for which the achievements are loaded.
    int loadAchievementsDuration;
    
public:
    /** */
    DesktopLuaLibGameCenter(DLog *log_,
        ::ae::engine::desktop::DesktopPluginCfg *cfg_,
        ::ae::engine::Timer *timer_):LuaLibGameCenter(),log(log_),cfg(cfg_),
        timer(timer_),loginViewFlag(false),
        loginViewTime(DEFAULT_LOGIN_VIEW_TIME),
        loginViewDuration(DEFAULT_LOGIN_VIEW_DURATION),
        authenticateFlag(false),
        authenticateTime(DEFAULT_AUTHENTICATE_TIME),
        loadAchievementsDuration(DEFAULT_LOAD_ACHIEVEMENTS_DURATION) {
    }
    
    /** */
    virtual ~DesktopLuaLibGameCenter() {
    }
    
    /** */
    bool shouldAuthenticate() const {
        return authenticateFlag;
    }
    
    /** */
    int getAuthenticateTime() const {
        return authenticateTime;
    }
    

    /** */
    virtual void init();
    
    /** */
    virtual void show();
    
    /** */
    virtual void reportScore(const std::string& leaderboardId,long score);
    
    /** */
    virtual void reportAchievement(const std::string& achievementId,
        double percentComplete);
    
    /** */
    virtual bool isAchievementCompleted(const std::string& achievementId,
        bool& completed) {
    //
        return false;
    }
    
    /** */
    virtual bool getAchievementProgress(const std::string& achievementId,
        double& progress) {
    //
        return false;
    }
    
    /** */
    virtual void authenticate();
    
    /** */
    virtual void loadAchievements();
    
    /** */
    virtual void resetAchievements();
};
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_LUA_LIB_GAME_CENTER_H