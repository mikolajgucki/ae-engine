#ifndef AE_IOS_LUA_LIB_APP_LOVIN_H
#define AE_IOS_LUA_LIB_APP_LOVIN_H

#import "ALInterstitialAd.h"
#import "AEALInterstitialAdLoadDelegate.h"
#import "AEALInterstitialAdDisplayDelegate.h"
#include "AppLovinCallback.h"
#include "LuaLibAppLovin.h"

/**
 * \brief The iOS implementation of the AppLovin Lua library.
 */
class AEiOSLuaLibAppLovin:public ::ae::applovin::LuaLibAppLovin {
    /// The load delegate.
    AEALInterstitialAdLoadDelegate *interstitialAdLoadDelegate;
    
    /// The display delegate.
    AEALInterstitialAdDisplayDelegate *interstitialAdDisplayDelegate;
    
    /** */
    void init();
    
public:
    /** */
    AEiOSLuaLibAppLovin();
    
    virtual ~AEiOSLuaLibAppLovin() {
    }
    
    /** This method is called from the Lua library.
        Do not call it manually! */
    virtual void setCallback(::ae::applovin::AppLovinCallback *callback_);
    
    /** This method is called from Lua.
        Do not call it manually! */
    virtual bool isInterstitialAdReadyForDisplay();
    
    /** This method is called from Lua.
        Do not call it manually! */
    virtual void showInterstitialAd();
};

#endif // AE_IOS_LUA_LIB_APP_LOVIN_H