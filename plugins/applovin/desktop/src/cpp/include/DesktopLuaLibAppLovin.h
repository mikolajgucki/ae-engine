#ifndef AE_DESKTOP_LUA_LIB_APP_LOVIN_H
#define AE_DESKTOP_LUA_LIB_APP_LOVIN_H

#include "DLog.h"
#include "LuaLibAppLovin.h"

namespace ae {
    
namespace applovin {
 
/**
 * \brief Desktop AppLovin implementation.
 */
class DesktopLuaLibAppLovin:public LuaLibAppLovin {
    /// The log.
    DLog *log;
    
public:
    /** */
    DesktopLuaLibAppLovin(DLog *log_):LuaLibAppLovin(),log(log_) {
    }
    
    /** */
    virtual ~DesktopLuaLibAppLovin() {
    }
    
    /** */
    virtual void init(const std::string &bannerSize);
    
    /** */
    virtual bool isInterstitialAdReadyForDisplay();
    
    /** */
    virtual void showInterstitialAd();
};
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_LUA_LIB_APP_LOVIN_H