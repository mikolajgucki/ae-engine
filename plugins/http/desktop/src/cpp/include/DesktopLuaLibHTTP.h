#ifndef AE_DESKTOP_LUA_LIB_HTTP_H
#define AE_DESKTOP_LUA_LIB_HTTP_H

#include <vector>
#include "json/json.h"
#include "DLog.h"
#include "Timer.h"
#include "DesktopPluginCfg.h"
#include "LuaLibHTTP.h"
#include "HTTPLuaExtraLib.h"
#include "DesktopHTTPResponse.h"

namespace ae {
    
namespace http {
    
/**
 * \brief Desktop HTTP implementation.
 */
class DesktopLuaLibHTTP:public LuaLibHTTP {
    /// The log.
    DLog *log;
    
    /// The plugin configuration.
    ::ae::engine::desktop::DesktopPluginCfg *cfg;
    
    /// The timer.
    ::ae::engine::Timer *timer;
    
    /// The responses.
    std::vector<DesktopHTTPResponse *> responses;
    
    /** */
    DesktopHTTPResponse *getResponse(const std::string& url);
    
    /** */
    bool parseResponse(Json::Value json);
    
public:
    /** */
    DesktopLuaLibHTTP(DLog *log_,::ae::engine::desktop::DesktopPluginCfg *cfg_,
        ::ae::engine::Timer *timer_):LuaLibHTTP(),log(log_),cfg(cfg_),
        timer(timer_),responses() {
    }
    
    /** */
    virtual ~DesktopLuaLibHTTP();
    
    /** */
    bool initLuaLib();
    
    /** */
    virtual void get(const std::string& id,const std::string &url);
    
    /** */
    virtual void openURL(const std::string& url);
    
    /** */
    void sendGetResponse(const std::string& id,const std::string &url);
};
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_LUA_LIB_HTTP_H