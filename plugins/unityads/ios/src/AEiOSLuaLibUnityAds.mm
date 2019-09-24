#import <UnityAds/UnityAds.h>
#include "Log.h"
#include "ae_sdl2_ios.h"
#include "AEiOSLuaLibUnityAds.h"

using namespace std;
using namespace ae;

/// The log tag.
static const char *logTag = "AE/UnityAds";

/** */
void AEiOSLuaLibUnityAds::init(bool debugMode) {
    Log::trace(logTag,"AEiOSLuaLibUnityAds::init()");
    
// game identifier
    const char *gameIdCStr = gameId.c_str();
    NSString *nsGameId = [NSString stringWithUTF8String:gameIdCStr];    
    
// delegate
    aeDelegate = [[AEUnityAdsDelegate alloc] init];
    [aeDelegate setLuaLibUnityAds:this];
    
// initialize
    [UnityAds initialize:nsGameId delegate:aeDelegate];
    
// debug
    BOOL objCDebugMode = debugMode ? YES : NO;
    [UnityAds setDebugMode:objCDebugMode];
}

/** */
bool AEiOSLuaLibUnityAds::isReady() {
    return [UnityAds isReady] == YES;
}

/** */
bool AEiOSLuaLibUnityAds::isReady(const string &placementId) {
// placement identifier
    const char *placementIdCStr = placementId.c_str();
    NSString *nsPlacementId = [NSString stringWithUTF8String:placementIdCStr];
    
    return [UnityAds isReady:nsPlacementId];
}

/** */
void AEiOSLuaLibUnityAds::show() {
// get UIViewController
    UIViewController *uiViewController = aeGetTopViewController();
    
// show
    [UnityAds show:uiViewController];
}

/** */
void AEiOSLuaLibUnityAds::show(const string &placementId) {
// placement identifier
    const char *placementIdCStr = placementId.c_str();
    NSString *nsPlacementId = [NSString stringWithUTF8String:placementIdCStr];
    
// get UIViewController
    UIViewController *uiViewController = aeGetTopViewController();
    
// show
    [UnityAds show:uiViewController placementId:nsPlacementId];
}