#include <sstream>
#include "DesktopLuaLibVungle.h"

using namespace std;
using namespace ae;

namespace ae {
    
namespace vungle {
  
/**
 * \brief The alarm which fires 'ad-playable-changed' event.
 */
class AdPlayableChangedAlarm:public ::ae::engine::TimerAlarm {
    /// The Vungle Lua library.
    DesktopLuaLibVungle *luaLibVungle;
    
    /// The state to change to.
    bool isAdPlayable;
    
public:
    AdPlayableChangedAlarm(DesktopLuaLibVungle *luaLibVungle_,
        bool isAdPlayable_,int time_):TimerAlarm(time_),
        luaLibVungle(luaLibVungle_),isAdPlayable(isAdPlayable_) {
    }
    
    /** */
    virtual ~AdPlayableChangedAlarm() {
    }
    
    /** */
    virtual void fire() {
        luaLibVungle->adPlayableChanged(isAdPlayable);
        delete this;
    }
};

/**
 * \brief The alarm which fire 'will-close-ad' event. 
 */
class WillCloseAdAlarm:public ::ae::engine::TimerAlarm {
    /// The Vungle Lua library.
    DesktopLuaLibVungle *luaLibVungle;
    
public:
    /** */
    WillCloseAdAlarm(DesktopLuaLibVungle *luaLibVungle_,int time_):
        TimerAlarm(time_),luaLibVungle(luaLibVungle_) {
    }
    
    /** */
    virtual ~WillCloseAdAlarm() {
    }
    
    /** */
    virtual void fire() {
        luaLibVungle->willCloseAd();
        delete this;
    }
};
    
/// The log tag.
static const char *logTag = "Desktop/Vungle";
    
/** */
void DesktopLuaLibVungle::init() {
    log->trace(logTag,"DesktopLuaLibVungle::init()");
}

/** */
bool DesktopLuaLibVungle::initLuaLib() {
// read configuration
    Json::Value json;
    if (cfg->readJSONCfg(json) == false) {
        setError(cfg->getError());
        return false;
    }  
    
// change playable delay
    Json::Value jsonDelay = json["adPlayableChangeDelay"];
    if (jsonDelay.isNull() == false) {
        if (jsonDelay.isInt() == false) {
            setError("Ad playable change delay must be integer "
                "(adPlayableChangeDelay)");
            return false;
        }
        adPlayableChangeDelay = jsonDelay.asInt();
    }
    
// change playable alarm
    if (adPlayableChangeDelay > 0) {
        AdPlayableChangedAlarm *alarm = new AdPlayableChangedAlarm(
            this,true,adPlayableChangeDelay);
        timer->addAlarm(alarm);
    }
    
    return true;
}

/** */
bool DesktopLuaLibVungle::isCachedAdAvailable() {
    log->trace(logTag,"DesktopLuaLibVungle::isCachedAdAvailable()");
    return false;
}

/** */
void DesktopLuaLibVungle::playAd() {
    log->trace(logTag,"DesktopLuaLibVungle::playAd()");
    
// show
    willShowAd();
    
// close
    WillCloseAdAlarm *alarm = new WillCloseAdAlarm(this,3000);
    timer->addAlarm(alarm);
}    
    
} // namespace
    
} // namespace