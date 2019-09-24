#ifndef AE_DESKTOP_LUA_LIB_TAPJOY_H
#define AE_DESKTOP_LUA_LIB_TAPJOY_H

#include "DLog.h"
#include "DesktopPluginCfg.h"
#include "LuaLibTapjoy.h"

namespace ae {
    
namespace tapjoy {
    
/**
 * \brief Desktop Tapjoy implementation.
 */
class DesktopLuaLibTapjoy:public LuaLibTapjoy {
    /// The log.
    DLog *log;
    
    /// The plugin configuration.
    ::ae::engine::desktop::DesktopPluginCfg *cfg;
public:
    DesktopLuaLibTapjoy(DLog *log_,
        ::ae::engine::desktop::DesktopPluginCfg *cfg_):LuaLibTapjoy(),
        log(log_),cfg(cfg_) {
    }
    
    /** */
    virtual ~DesktopLuaLibTapjoy() {
    }
    
    /** */
    virtual bool isConnected();
    
    /** */
    virtual void requestContent(const std::string &placement);
    
    /** */
    virtual bool isContentReady(const std::string &placement);
    
    /** */
    virtual void showContent(const std::string &placement);
};
    
} // namespace

} // namespace

#endif // AE_DESKTOP_LUA_LIB_TAPJOY_H