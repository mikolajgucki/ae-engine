#ifndef AE_IOS_LUA_LIB_GAME_ANALYTICS_H
#define AE_IOS_LUA_LIB_GAME_ANALYTICS_H

#import <Foundation/Foundation.h>
#include "GameAnalytics.h"
#include "LuaLibGameAnalytics.h"

/**
 * \brief The iOS implementation of the Game Analytics Lua library.
 */
class AEiOSLuaLibGameAnalytics:public ::ae::gameanalytics::LuaLibGameAnalytics {
    /// The game key.
    const std::string gameKey;
    
    /// The secret key.
    const std::string secretKey;
    
    /** */
    GAProgressionStatus progressionStatusToGAStatus(ProgressionStatus status);
    
    /** */
    GAResourceFlowType flowTypeToGAFlowType(FlowType flowType);
    
public:
    /** */
    AEiOSLuaLibGameAnalytics(const std::string& gameKey_,
        const std::string& secretKey_):LuaLibGameAnalytics(),
        gameKey(gameKey_),secretKey(secretKey_) {
    //
    }
    
    /** */
    virtual ~AEiOSLuaLibGameAnalytics() {
    }
    
    /** */
    void enableLogging();
    
    /** */
    void configureAvailableResourceCurrencies(
        const std::vector<const std::string> currencies);
    
    /** */
    void configureAvailableResourceItemTypes(
        const std::vector<const std::string> itemTypes);
    
    /** */
    void initGameAnalytics();
    
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
        const std::string* store,const std::string *signature);
    
    /** */
    virtual void addResourceEvent(FlowType flowType,const std::string &currency,
        float amount,const std::string& itemType,const std::string &itemId);
};

#endif // AE_IOS_LUA_LIB_GAME_ANALYTICS_H