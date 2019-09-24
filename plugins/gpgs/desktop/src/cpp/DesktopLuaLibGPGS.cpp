#include "Log.h"
#include "DesktopLuaLibGPGS.h"

using namespace std;

namespace ae {
    
namespace gpgs {
    
/**
 * \brief The alarm which fires 'signed in' event.
 */
class SignedInAlarm:public ::ae::engine::TimerAlarm {
    /** */
    enum {
        /** */
        ALARM_TIME = 2000
    };    
    
    /// The GPGS Lua library.
    DesktopLuaLibGPGS *luaLibGpgs;
    
public:
    /** */
    SignedInAlarm(DesktopLuaLibGPGS *luaLibGpgs_):
        TimerAlarm(ALARM_TIME),luaLibGpgs(luaLibGpgs_) {
    }
    
    /** */
    virtual ~SignedInAlarm() {
    }
    
    /** */
    virtual void fire() {
        luaLibGpgs->signedIn();
        delete this;
    }
};    
    
/// The log tag.
static const char *logTag = "Desktop/GPGS";
    
/** */
void DesktopLuaLibGPGS::signIn() {
    log->trace(logTag,"DesktopLuaLibGPGS::signIn()");
    
    SignedInAlarm *signedInAlarm = new SignedInAlarm(this);
    timer->addAlarm(signedInAlarm);
}

/** */
void DesktopLuaLibGPGS::signOut() {
    log->trace(logTag,"DesktopLuaLibGPGS::signOut()");
    signedInFlag = false;
}

/** */
bool DesktopLuaLibGPGS::isSignedIn() {
    return signedInFlag;
}

/** */
void DesktopLuaLibGPGS::unlockAchievement(const string &id) {
// log
    ostringstream msg;
    msg << "DesktopLuaLibGPGS::unlockAchievement(" << id << ")";
    log->trace(logTag,msg.str());
}

/** */
void DesktopLuaLibGPGS::incrementAchievement(const string &id,int steps) {
// log
    ostringstream msg;
    msg << "DesktopLuaLibGPGS::incrementAchievement(" << id <<
        "," << steps << ")";
    log->trace(logTag,msg.str());
}

/** */
void DesktopLuaLibGPGS::setAchievementSteps(const string &id,int steps) {
// log
    ostringstream msg;
    msg << "DesktopLuaLibGPGS::setAchievementSteps(" << id << 
        "," << steps << ")";
    log->trace(logTag,msg.str());
}    

/** */
void DesktopLuaLibGPGS::submitScore(const string &leaderboardId,long score) {
// log
    ostringstream msg;
    msg << "DesktopLuaLibGPGS::submitScore(" << leaderboardId <<
        "," << score << ")";
    log->trace(logTag,msg.str());
}

/** */
void DesktopLuaLibGPGS::displayAchievements() {
    log->trace(logTag,"DesktopLuaLibGPGS::displayAchievements()");
}

/** */
void DesktopLuaLibGPGS::displayLeaderboard(const string &leaderboardId) {
// log
    ostringstream msg;
    msg << "DesktopLuaLibGPGS::displayLeaderboard(" << leaderboardId << ")";
    log->trace(logTag,msg.str());
}

/** */
void DesktopLuaLibGPGS::signedIn() {
    signedInFlag = true;
    if (getCallback() != (GPGSCallback *)0) {
        getCallback()->signedIn();
    }
    else {
        Log::trace(logTag,"signedIn() called with null callback");
    }
}
    
} // namespace    
    
} // namespace