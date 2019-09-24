#import <Foundation/Foundation.h>
#include "SDL.h"
#include "Log.h"
#include "ae_sdl2.h"
#include "ae_sdl2_ios.h"
#include "AEiOSLuaLibVungle.h"

using namespace ae;

/// The log tag.
static const char *logTag = "AE/Vungle";

/** */
void AEiOSLuaLibVungle::init() {
    Log::trace(logTag,"AEiOSLuaLibVungle::init()");
    
// application identifier
    const char *appIdCStr = appId.c_str();
    NSString *nsAppId = [NSString stringWithUTF8String:appIdCStr];
    
// delegate
    aeDelegate = [[AEVungleSDKDelegate alloc] init];
    [aeDelegate setLuaLibVungle:this];
    
// start
    VungleSDK *sdk = [VungleSDK sharedSDK];
    [sdk setDelegate:aeDelegate];
    [sdk startWithAppId:nsAppId];
}

/** */
void AEiOSLuaLibVungle::playAd() {
    Log::trace(logTag,"AEiOSLuaLibVungle::playAd()");
    
// get UIViewController
    UIViewController *uiViewController = aeGetTopViewController();
    
// play ad
    VungleSDK *sdk = [VungleSDK sharedSDK];
    NSError *error;
    if ([sdk playAd:uiViewController error:&error] == NO) {
        getCallback()->failedToPlayAd();
    }
}
