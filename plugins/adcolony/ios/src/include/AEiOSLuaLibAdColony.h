#ifndef AE_IOS_LUA_LIB_AD_COLONY_H
#define AE_IOS_LUA_LIB_AD_COLONY_H

#import <Foundation/Foundation.h>
#import <AdColony/AdColony.h>
#include <string>
#include <vector>
#include "LuaLibAdColony.h"

/**
 * \brief The iOS implementation of the AdColony Lua library.
 */
class AEiOSLuaLibAdColony:public ::ae::adcolony::LuaLibAdColony {
    /// The application identifier.
    std::string appId;
    
    /// The zone identifiers.
    std::vector<std::string> zoneIds;   
    
    /// The interstitial ads.
    NSMutableArray *interstitialAds;
    
    /** */
    AdColonyInterstitial *findInterstitialAdByZoneId(const std::string& zoneId);
    
    /** */
    void removeInterstitital(AdColonyInterstitial *ad);
    
    /** */
    void interstitialDidClose(const std::string& zoneId);

public:
    
    /** */    
    AEiOSLuaLibAdColony(const std::string &appId_,
        std::vector<std::string> zoneIds_):LuaLibAdColony(),appId(appId_),
        zoneIds(zoneIds_) {
    }
    
    /** */
    virtual ~AEiOSLuaLibAdColony() {
    }
    
    /**
     * \brief Initializes the library and AdColony itself. Must be called after
     *   creating the library object.
     * \param appOptions The AdColony application options.
     */
    void init(AdColonyAppOptions *appOptions);
    
    /**
     * \brief Initializes the library and AdColony itself. Must be called after
     *   creating the library object.
     */
    void init();
    
    /** This method is called from Lua.
        Do not call it manually! */
    virtual void requestInterstitial(const std::string &zoneId);
    
    /** This method is called from Lua.
        Do not call it manually! */
    virtual bool isInterstitialExpired(const std::string &zoneId);
    
    /** This method is called from Lua.
        Do not call it manually! */
    virtual void showInterstitial(const std::string &zoneId);
};

#endif // AE_IOS_LUA_LIB_AD_COLONY_H