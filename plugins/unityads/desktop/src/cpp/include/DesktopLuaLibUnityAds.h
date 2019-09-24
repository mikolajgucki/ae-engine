#ifndef AE_DESKTOP_LUA_LIB_UNITY_ADS_H
#define AE_DESKTOP_LUA_LIB_UNITY_ADS_H

#include "DLog.h"
#include "DesktopPluginCfg.h"
#include "LuaLibUnityAds.h"

namespace ae {
    
namespace unityads {
  
/**
 * \brief Desktop Unity Ads implementation.
 */
class DesktopLuaLibUnityAds:public LuaLibUnityAds {
    /// The plugin configuration.
    ::ae::engine::desktop::DesktopPluginCfg *cfg;
    
    /// The log.
    DLog *dlog;
    
public: 
    /** */
    DesktopLuaLibUnityAds(::ae::engine::desktop::DesktopPluginCfg *cfg_,
        DLog *dlog_):LuaLibUnityAds(),cfg(cfg_),dlog(dlog_) {
    }
    
    /** */
    virtual ~DesktopLuaLibUnityAds() {
    }
    
    /** */
    virtual bool isReady();
    
    /** */
    virtual bool isReady(const std::string &placementId);
    
    /** */
    virtual void show();
    
    /** */
    virtual void show(const std::string &placementId);
};
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_LUA_LIB_UNITY_ADS_H