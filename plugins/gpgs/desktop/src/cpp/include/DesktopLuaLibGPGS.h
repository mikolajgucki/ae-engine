#ifndef AE_DESKTOP_LUA_LIB_GPGS_H
#define AE_DESKTOP_LUA_LIB_GPGS_H

#include "DLog.h"
#include "Timer.h"
#include "LuaLibGPGS.h"
#include "GPGSLuaExtraLib.h"

namespace ae {
    
namespace gpgs {
  
/**
 * \brief The desktop implementation of the Google Play Game Services Lua
 *   library.
 */
class DesktopLuaLibGPGS:public LuaLibGPGS {
    /// The log.
    DLog *log;
    
    /// The timer.
    ::ae::engine::Timer *timer;  
    
    /// Indicates if the player is signed in.
    bool signedInFlag;
    
public:
    /** */
    DesktopLuaLibGPGS(DLog *log_,::ae::engine::Timer *timer_):LuaLibGPGS(),
        log(log_),timer(timer_),signedInFlag(false) {
    }
    
    /** */
    virtual ~DesktopLuaLibGPGS() {
    }
    
    /** */
    virtual void signIn();
    
    /** */
    virtual void signOut();
    
    /** */
    virtual bool isSignedIn();
    
    /** */
    virtual void unlockAchievement(const std::string &id);
    
    /** */
    virtual void incrementAchievement(const std::string &id,int steps);
    
    /** */
    virtual void setAchievementSteps(const std::string &id,int steps);    
    
    /** */
    virtual void submitScore(const std::string &leaderboardId,long score);
    
    /** */
    virtual void displayAchievements();
    
    /** */
    virtual void displayLeaderboard(const std::string &leaderboardId); 
    
    /** */
    void signedIn();
};
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_LUA_LIB_GPGS_H