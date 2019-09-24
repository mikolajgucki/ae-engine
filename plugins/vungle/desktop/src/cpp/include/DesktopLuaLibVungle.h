#ifndef AE_DESKTOP_LUA_LIB_VUNGLE_H
#define AE_DESKTOP_LUA_LIB_VUNGLE_H

#include "DLog.h"
#include "Timer.h"
#include "DesktopPluginCfg.h"
#include "LuaLibVungle.h"

namespace ae {
    
namespace vungle {
  
/**
 * \brief Desktop Vungle implementation.
 */
class DesktopLuaLibVungle:public LuaLibVungle {
    /** */
    enum {
        DEFAULT_AD_PLAYABLE_CHANGE_DELAY = 6000
    };
    
    /// The log.
    DLog *log;
    
    /// The plugin configuration.
    ::ae::engine::desktop::DesktopPluginCfg *cfg;    
    
    /// The timer.
    ::ae::engine::Timer *timer;     
    
    /// The delay in milliseconds after the playable will change to true.
    int adPlayableChangeDelay;
    
public:
    /** */
    DesktopLuaLibVungle(DLog *log_,
        ::ae::engine::desktop::DesktopPluginCfg *cfg_,
        ::ae::engine::Timer *timer_):LuaLibVungle(),log(log_),cfg(cfg_),
        timer(timer_),adPlayableChangeDelay(DEFAULT_AD_PLAYABLE_CHANGE_DELAY) {
    }
    
    /** */
    virtual ~DesktopLuaLibVungle() {
    }
    
    /** */
    bool initLuaLib();
    
    /** */
    virtual void init();
    
    /** */
    virtual bool isCachedAdAvailable();
    
    /** */
    virtual void playAd();
};
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_LUA_LIB_VUNGLE_H