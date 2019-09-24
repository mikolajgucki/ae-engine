#ifndef AE_DESKTOP_LUA_LIB_GAME_ANALYTICS_H
#define AE_DESKTOP_LUA_LIB_GAME_ANALYTICS_H

#include "DLog.h"
#include "LuaLibGameAnalytics.h"

namespace ae {
    
namespace gameanalytics {
    
class DesktopLuaLibGameAnalytics:public LuaLibGameAnalytics {
    /** */
    DLog *log;
    
    /** */
    const char *progressionStatusToStr(ProgressionStatus status);
        
public:
    /** */
    DesktopLuaLibGameAnalytics(DLog *log_):LuaLibGameAnalytics(),log(log_) {
    }
    
    /** */
    virtual ~DesktopLuaLibGameAnalytics() {
    } 
    
    /** */
    virtual void init();
    
    /** */
    virtual void addProgressionEvent(ProgressionStatus status,
        const std::string& progression01);
    
    /** */
    virtual void addProgressionEvent(ProgressionStatus status,
        const std::string& progression01,const std::string& progression02);
    
    /** */
    virtual void addProgressionEvent(ProgressionStatus status,
        const std::string& progression01,const std::string& progression02,
        const std::string& progression03);
    
    /** */
    virtual void addDesignEvent(const std::string& eventId);
    
    /** */
    virtual void addDesignEvent(const std::string& eventId,double value);
    
    /** */
    virtual void addErrorEvent(ErrorSeverity severity,
        const std::string& errorMsg);  
        
    /** */
    virtual void addBusinessEvent(const std::string& currency,int amount,
        const std::string& itemType,const std::string& itemId,
        const std::string& cartType,const std::string *receipt,
        const std::string *store,const std::string *signature); 
    
    /** */
    virtual void addResourceEvent(FlowType flowType,const std::string &currency,
        float amount,const std::string& itemType,const std::string &itemId);

};
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_LUA_LIB_GAME_ANALYTICS_H