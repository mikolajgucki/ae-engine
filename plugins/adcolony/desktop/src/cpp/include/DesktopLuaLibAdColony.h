#ifndef AE_DESKTOP_LUA_LIB_AD_COLONY_H
#define AE_DESKTOP_LUA_LIB_AD_COLONY_H

#include "DLog.h"
#include "Timer.h"
#include "DesktopPluginCfg.h"
#include "LuaLibAdColony.h"

namespace ae {
    
namespace adcolony {
  
/**
 * \brief Desktop AdColony implementation.
 */
class DesktopLuaLibAdColony:public LuaLibAdColony {
    /// The plugin configuration.
    ::ae::engine::desktop::DesktopPluginCfg *cfg;
    
    /// The timer.
    ::ae::engine::Timer *timer;    
    
    /// The log.
    DLog *dlog;
    
public: 
    /** */
    DesktopLuaLibAdColony(::ae::engine::desktop::DesktopPluginCfg *cfg_,
        ::ae::engine::Timer *timer_,DLog *dlog_):LuaLibAdColony(),cfg(cfg_),
        timer(timer_),dlog(dlog_) {
    }
    
    /** */
    virtual ~DesktopLuaLibAdColony() {
    }
    
    /** */
    virtual void requestInterstitial(const std::string &zoneId);
    
    /** */
    virtual bool isInterstitialExpired(const std::string &zoneId);
    
    /** */
    virtual void showInterstitial(const std::string &zoneId);
};
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_LUA_LIB_AD_COLONY_H