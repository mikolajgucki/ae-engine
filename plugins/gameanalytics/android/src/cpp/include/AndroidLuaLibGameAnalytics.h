#ifndef AE_ANDROID_LUA_LIB_GAME_ANALYTICS_H
#define AE_ANDROID_LUA_LIB_GAME_ANALYTICS_H

#include <jni.h>
#include "LuaLibGameAnalytics.h"

namespace ae {
    
namespace gameanalytics {
  
/**
 * \brief The Android implementation of the Game Analytics Lua library.
 */
class AndroidLuaLibGameAnalytics:public LuaLibGameAnalytics {
    /// The JNI environment.
    JNIEnv *env;
    
    /** */
    void getJNIEnv();
    
    /** */
    const char *progressionStatusToStr(ProgressionStatus status);
    
    /** */
    const char *flowTypeToStr(FlowType flowType);
    
public:
    /** */
    AndroidLuaLibGameAnalytics():LuaLibGameAnalytics(),env((JNIEnv *)0) {
    }
    
    /** */
    virtual ~AndroidLuaLibGameAnalytics() {
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
        const std::string& msg);    
    
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

#endif // AE_ANDROID_LUA_LIB_GAME_ANALYTICS_H