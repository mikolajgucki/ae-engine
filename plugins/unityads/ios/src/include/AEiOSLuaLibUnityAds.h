#ifndef AE_IOA_LUA_LIB_UNITY_ADS_H
#define AE_IOA_LUA_LIB_UNITY_ADS_H

#include <string>
#include "LuaLibUnityAds.h"
#include "AEUnityAdsDelegate.h"

/**
 * \brief The iOS implementation of the Unity Ads Lua library.
 */
class AEiOSLuaLibUnityAds:public ::ae::unityads::LuaLibUnityAds {
    /// The game identifier.
    const std::string gameId;
    
    /// The delegate.
    AEUnityAdsDelegate *aeDelegate;    
    
public:
    /** */
    AEiOSLuaLibUnityAds(const std::string& gameId_):LuaLibUnityAds(),
        gameId(gameId_) {
    }
    
    /** */
    void init(bool debugMode);
    
    /** */
    virtual bool isReady();
    
    /** */
    virtual bool isReady(const std::string &placementId);
    
    /** */ 
    virtual void show();
    
    /** */
    virtual void show(const std::string &placementId);
};

#endif // AE_IOA_LUA_LIB_UNITY_ADS_H