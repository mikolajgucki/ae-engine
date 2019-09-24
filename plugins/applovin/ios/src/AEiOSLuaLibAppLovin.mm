#import <Foundation/Foundation.h>
#import "ALSdk.h"
#import "AEALInterstitialAdLoadDelegate.h"
#import "AEALInterstitialAdDisplayDelegate.h"
#include "Log.h"
#include "AppLovinCallback.h"
#include "AEiOSLuaLibAppLovin.h"

using namespace ae;
using namespace ae::applovin;

/// The log tag.
static const char *logTag = "AEAppLovin";

/** */
AEiOSLuaLibAppLovin::AEiOSLuaLibAppLovin():LuaLibAppLovin(),
    interstitialAdLoadDelegate((AEALInterstitialAdLoadDelegate *)0),
    interstitialAdDisplayDelegate((AEALInterstitialAdDisplayDelegate *)0) {
//
    init();
}

/** */
void AEiOSLuaLibAppLovin::init() {
    Log::trace(logTag,"AEiOSLuaLibAppLovin::init()");
    
// initialize SDK
    [ALSdk initializeSdk];
    
// interstitial ad load delegate
    interstitialAdLoadDelegate = [[AEALInterstitialAdLoadDelegate alloc] init];
    [ALInterstitialAd shared].adLoadDelegate = interstitialAdLoadDelegate;
    
// interstitial display delegate    
    interstitialAdDisplayDelegate =
        [[AEALInterstitialAdDisplayDelegate alloc] init];
    [ALInterstitialAd shared].adDisplayDelegate = interstitialAdDisplayDelegate;
}

/** */
void AEiOSLuaLibAppLovin::setCallback(AppLovinCallback *callback_) {
    LuaLibAppLovin::setCallback(callback_);
    
    [interstitialAdLoadDelegate setAppLovinCallback:callback_];
    [interstitialAdDisplayDelegate setAppLovinCallback:callback_];
}
    
/** */
bool AEiOSLuaLibAppLovin::isInterstitialAdReadyForDisplay() {
    Log::trace(logTag,"AEiOSLuaLibAppLovin::isInterstitialAdReadyForDisplay()");
    if ([ALInterstitialAd isReadyForDisplay] == YES) {
        return true;
    }
    else {
        return false;
    }
}
    
/** */
void AEiOSLuaLibAppLovin::showInterstitialAd() {
    Log::trace(logTag,"AEiOSLuaLibAppLovin::showInterstitialAd()");
    [ALInterstitialAd show];
}