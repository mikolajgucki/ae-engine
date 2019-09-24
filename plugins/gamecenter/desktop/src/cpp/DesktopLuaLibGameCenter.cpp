#include <sstream>
#include "json/json.h"

#include "DesktopLuaLibGameCenter.h"

using namespace std;

namespace ae {
    
namespace gamecenter {

/**
 * \brief The alarm which fires the 'willShowLoginView' event.
 */
class WillShowLoginViewAlarm:public ::ae::engine::TimerAlarm {
    /// The Game Center Lua library.
    DesktopLuaLibGameCenter *luaLibGameCenter;
    
public:
    /** */
    WillShowLoginViewAlarm(DesktopLuaLibGameCenter *luaLibGameCenter_,int time_):
        TimerAlarm(time_),luaLibGameCenter(luaLibGameCenter_) {
    }
    
    /** */
    virtual ~WillShowLoginViewAlarm() {
    }
    
    /** */
    virtual void fire() {
        luaLibGameCenter->willShowLoginView();
        delete this;
    }
};     

/**
 * \brief The alarm which fires the 'hidden' event.
 */
class HiddenAlarm:public ::ae::engine::TimerAlarm {
    /// The Game Center Lua library.
    DesktopLuaLibGameCenter *luaLibGameCenter;
    
public:
    /** */
    HiddenAlarm(DesktopLuaLibGameCenter *luaLibGameCenter_,int time_):
        TimerAlarm(time_),luaLibGameCenter(luaLibGameCenter_) {
    }
    
    /** */
    virtual ~HiddenAlarm() {
    }
    
    /** */
    virtual void fire() {
        luaLibGameCenter->loginViewHidden();
        delete this;
    }
};     

/**
 * \brief The alarm which fires the 'authenticated' event.
 */
class AuthenticatedAlarm:public ::ae::engine::TimerAlarm {
    /// The Game Center Lua library.
    DesktopLuaLibGameCenter *luaLibGameCenter;
    
public:
    /** */
    AuthenticatedAlarm(DesktopLuaLibGameCenter *luaLibGameCenter_,int time_):
        TimerAlarm(time_),luaLibGameCenter(luaLibGameCenter_) {
    }
    
    /** */
    virtual ~AuthenticatedAlarm() {
    }
    
    /** */
    virtual void fire() {
        if (luaLibGameCenter->shouldAuthenticate()) {
            luaLibGameCenter->authenticated();
        }
        else {
            luaLibGameCenter->notAuthenticated();
        }
        delete this;
    }
};  

/**
 * \brief The alarm which fires the 'achievementsLoaded' event.
 */
class AchievementsLoadedAlarm:public ::ae::engine::TimerAlarm {
    /// The Game Center Lua library.
    DesktopLuaLibGameCenter *luaLibGameCenter;
    
public:
    /** */
    AchievementsLoadedAlarm(DesktopLuaLibGameCenter *luaLibGameCenter_,
        int time_):TimerAlarm(time_),luaLibGameCenter(luaLibGameCenter_) {
    }
    
    /** */
    virtual ~AchievementsLoadedAlarm() {
    }
    
    /** */
    virtual void fire() {
        luaLibGameCenter->achievementsLoaded();
        delete this;
    }
};
    
/// The log tag.
static const char *logTag = "Desktop/GameCenter";

/** */
void DesktopLuaLibGameCenter::init() {
    log->trace(logTag,"DesktopLuaLibGameCenter::init()");
    
// read the configuration
    Json::Value json;
    if (cfg->readJSONCfg(json) == false) {
        setError(cfg->getError());
        return;
    }    
    
// login view
    loginViewFlag = false;
    const char *loginViewKey = "loginView";
    if (json.isMember(loginViewKey)) {
        if (json[loginViewKey].isBool() == false) {
            ostringstream err;
            err << "JSON value of key " << loginViewKey <<
                " is not a bool value";
            setError(err.str());
            return; 
        }
        loginViewFlag = json[loginViewKey].asBool();
    } 
    
// login view time
    loginViewTime = DEFAULT_LOGIN_VIEW_TIME;
    const char *loginViewTimeKey = "loginViewTime";
    if (json.isMember(loginViewTimeKey)) {
        if (json[loginViewTimeKey].isInt() == false) {
            ostringstream err;
            err << "JSON value of key " << loginViewTimeKey <<
                " is not an integer value";
            setError(err.str());
            return; 
        }
        int time = json[loginViewTimeKey].asInt();
        if (authenticateTime < 0) {
            setError("Invalid login view time");
            return; 
        }
        loginViewTime = time;
    }     
    
// login duration
    loginViewDuration = DEFAULT_LOGIN_VIEW_DURATION;
    const char *loginViewDurationKey = "loginViewDuration";
    if (json.isMember(loginViewDurationKey)) {
        if (json[loginViewDurationKey].isInt() == false) {
            ostringstream err;
            err << "JSON value of key " << loginViewDurationKey <<
                " is not an integer value";
            setError(err.str());
            return; 
        }
        int time = json[loginViewDurationKey].asInt();
        if (authenticateTime < 0) {
            setError("Invalid login duration");
            return; 
        }
        loginViewDuration = time;
    }    
    
// authenticate
    authenticateFlag = false;
    const char *authenticateKey = "authenticate";
    if (json.isMember(authenticateKey)) {
        if (json[authenticateKey].isBool() == false) {
            ostringstream err;
            err << "JSON value of key " << authenticateKey <<
                " is not a bool value";
            setError(err.str());
            return; 
        }
        authenticateFlag = json[authenticateKey].asBool();
    } 
    
// authenticate time
    authenticateTime = DEFAULT_AUTHENTICATE_TIME;
    const char *authenticateTimeKey = "authenticateTime";
    if (json.isMember(authenticateTimeKey)) {
        if (json[authenticateTimeKey].isInt() == false) {
            ostringstream err;
            err << "JSON value of key " << authenticateTimeKey <<
                " is not an integer value";
            setError(err.str());
            return; 
        }
        int time = json[authenticateTimeKey].asInt();
        if (authenticateTime < 0) {
            setError("Invalid authenticate time");
            return; 
        }
        authenticateTime = time;
    }    
    
// login duration
    loadAchievementsDuration = DEFAULT_LOAD_ACHIEVEMENTS_DURATION;
    const char *loadAchievementsDurationKey = "loadAchievementsDuration";
    if (json.isMember(loadAchievementsDurationKey)) {
        if (json[loadAchievementsDurationKey].isInt() == false) {
            ostringstream err;
            err << "JSON value of key " << loadAchievementsDurationKey <<
                " is not an integer value";
            setError(err.str());
            return; 
        }
        int time = json[loadAchievementsDurationKey].asInt();
        if (authenticateTime < 0) {
            setError("Invalid load achievements duration");
            return; 
        }
        loadAchievementsDuration = time;
    }     
}

/** */
void DesktopLuaLibGameCenter::authenticate() {
    log->trace(logTag,"DesktopLuaLibGameCenter::authenticate()");
    
// login view
    if (loginViewFlag == true) {
    // will show login view alarm
        WillShowLoginViewAlarm *willShowLoginViewAlarm =
            new WillShowLoginViewAlarm(this,loginViewTime);
        timer->addAlarm(willShowLoginViewAlarm);
        
    // hidden alarm
        HiddenAlarm *hiddenAlarm = new HiddenAlarm(
            this,loginViewTime + loginViewDuration);
        timer->addAlarm(hiddenAlarm);
    }
    
// authenticated
    AuthenticatedAlarm *authenticatedAlarm = new AuthenticatedAlarm(
        this,authenticateTime);
    timer->addAlarm(authenticatedAlarm);
}

/** */
void DesktopLuaLibGameCenter::show() {
    log->trace(logTag,"DesktopLuaLibGameCenter::show()");
}

/** */
void DesktopLuaLibGameCenter::reportScore(
    const string& leaderboardId,long score) {
// log
    ostringstream msg;
    msg << "DesktopLuaLibGameCenter::reportScore(" << leaderboardId << 
        "," << score << ")";
    log->trace(logTag,msg.str());
}

/** */
void DesktopLuaLibGameCenter::reportAchievement(const string& achievementId,
    double percentComplete) {
// log
    ostringstream msg;
    msg << "DesktopLuaLibGameCenter::reportAchievement(" << achievementId << 
        "," << percentComplete << ")";
    log->trace(logTag,msg.str());
}

/** */
void DesktopLuaLibGameCenter::loadAchievements() {
    log->trace(logTag,"DesktopLuaLibGameCenter::loadAchievements()");
    
    AchievementsLoadedAlarm *alarm = new AchievementsLoadedAlarm(
        this,loadAchievementsDuration);
    timer->addAlarm(alarm);    
}

/** */
void DesktopLuaLibGameCenter::resetAchievements() {
    log->trace(logTag,"DesktopLuaLibGameCenter::resetAchievements()");
}
    
} // namespace
    
} // namespace