#ifndef AE_DESKTOP_LUA_LIB_CHARTBOOST_H
#define AE_DESKTOP_LUA_LIB_CHARTBOOST_H

#include "DLog.h"
#include "LuaLibChartboost.h"
#include "ChartboostLuaExtraLib.h"

namespace ae {
    
namespace chartboost {
  
/**
 * \brief Desktop Chartboost Lua library implementation.
 */
class DesktopLuaLibChartboost:public LuaLibChartboost {
    /// The log.
    DLog *log;
    
public:
    /** */
    DesktopLuaLibChartboost(DLog *log_):LuaLibChartboost(),log(log_) {
    }
    
    /** */
    virtual ~DesktopLuaLibChartboost() {
    }
    
    /** */
    virtual void init();
    
    /** */
    virtual void cacheInterstitial(const std::string &location);
    
    /** */
    virtual void showInterstitial(const std::string &location);
};
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_LUA_LIB_CHARTBOOST_H