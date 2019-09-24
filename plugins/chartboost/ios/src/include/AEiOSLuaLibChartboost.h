#ifndef AE_IOS_LUA_LIB_CHARTBOOST_H
#define AE_IOS_LUA_LIB_CHARTBOOST_H

#include <string>

#include "AEChartboostDelegate.h"
#include "LuaLibChartboost.h"

/**
 * \brief The iOS implementation of the Chartboost Lua library.
 */
class AEiOSLuaLibChartboost:public ::ae::chartboost::LuaLibChartboost {
    /// The application identifier.
    std::string appId;
    
    /// The application signature.
    std::string appSignature;
    
    /// The chartboost delegate.
    AEChartboostDelegate *aeChartboostDelegate;
    
public:
    /** */
    AEiOSLuaLibChartboost(const std::string &appId_,
        const std::string &appSignature_):LuaLibChartboost(),appId(appId_),
        appSignature(appSignature_),
        aeChartboostDelegate((AEChartboostDelegate *)0) {
    }
    
    /** */
    virtual ~AEiOSLuaLibChartboost() {
    }
    
    /**
     * \brief Sets application information.
     * \param appId_ The application identifier.
     * \param appSignature_ The application signature.
     */
    void setAppInfo(const std::string &appId_,const std::string &appSignature_) {
        appId = appId_;
        appSignature = appSignature_;
    }
    
    /** This method is called from Lua.
        Do not call it manually! */
    virtual void init();
    
    /** This method is called from Lua.
        Do not call it manually! */
    virtual void cacheInterstitial(const std::string &location);
    
    /** This method is called from Lua.
        Do not call it manually! */
    virtual void showInterstitial(const std::string &location);
};

#endif //  AE_IOS_LUA_LIB_CHARTBOOST_H