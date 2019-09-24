#import <Foundation/Foundation.h>
#include "Log.h"
#include "ae_sdl2.h"
#include "ae_sdl2_ios.h"
#include "AEiOSLuaLibAdColony.h"

using namespace std;
using namespace ae;

/// The log tag.
static const char *logTag = "AEAdColony";

/** */
void AEiOSLuaLibAdColony::init(AdColonyAppOptions *appOptions) {
    Log::trace(logTag,"AEiOSLuaLibAdColony::init()");
    
// application identifier
    const char *appIdCStr = appId.c_str();
    NSString *nsAppId = [NSString stringWithUTF8String:appIdCStr];

    int capacity = (int)zoneIds.size();
    NSMutableArray *nsZoneIds = [NSMutableArray arrayWithCapacity:capacity];
// zones    
    vector<string>::iterator itr;
    for (itr = zoneIds.begin(); itr != zoneIds.end(); ++itr) {
        const char *zoneIdCStr = (*itr).c_str();
        NSString *nsZoneId = [NSString stringWithUTF8String:zoneIdCStr];
        [nsZoneIds addObject:nsZoneId];
    }    
    
// initialize
    [AdColony configureWithAppID:nsAppId zoneIDs:nsZoneIds options:appOptions
        completion:^(NSArray<AdColonyZone*>* zones) {
            Log::trace(logTag,"AdColony successfully configured");
        }
    ];
    
// interstitial ads
    interstitialAds = [[NSMutableArray alloc] init];
}

/** */
void AEiOSLuaLibAdColony::init() {
    init(nil);
}

/** */
void AEiOSLuaLibAdColony::removeInterstitital(AdColonyInterstitial *ad) {
    [interstitialAds removeObject:ad];
}

/** */
void AEiOSLuaLibAdColony::interstitialDidClose(const std::string& zoneId) {
// remove
    AdColonyInterstitial *closedAd = findInterstitialAdByZoneId(zoneId);
    removeInterstitital(closedAd);
    
    /*
    Log::trace(logTag,"Giving focus to the SDL view controller");
// get UIViewController
    SDL_Window *sdlWindow = aeGetSDLWindow();
    UIViewController *uiViewController = aeGetUIViewController(sdlWindow);
    [[UIApplication sharedApplication].keyWindow bringSubviewToFront:uiViewController.view];
    */
     
// notify
    interstitialClosed(zoneId);
}

/** */
AdColonyInterstitial *AEiOSLuaLibAdColony::findInterstitialAdByZoneId(
    const string& zoneId) {
// zone identifier
    const char *zoneIdCStr = zoneId.c_str();
    NSString *nsZoneId = [NSString stringWithUTF8String:zoneIdCStr];

// for each interstitial ad
    for (AdColonyInterstitial *ad in interstitialAds) {
        if ([nsZoneId isEqualToString:ad.zoneID]) {
            return ad;
        }
    }

    return NULL;
}

/** */
void AEiOSLuaLibAdColony::requestInterstitial(const string &zoneId) {
    Log::trace(logTag,"AEiOSLuaLibAdColony::requestInterstitial()");
    
// zone identifier
    const char *zoneIdCStr = zoneId.c_str();
    NSString *nsZoneId = [NSString stringWithUTF8String:zoneIdCStr];

// request    
    [AdColony requestInterstitialInZone:nsZoneId options:nil
        success:^(AdColonyInterstitial* ad) {
            ad.open = ^{
                interstitialOpened([nsZoneId UTF8String]);
            };
            ad.close = ^{
                interstitialDidClose([nsZoneId UTF8String]);
            };

            [interstitialAds addObject:ad];
            interstitialAvailable([nsZoneId UTF8String]);
        }
        failure:^(AdColonyAdRequestError* error) {
            // TODO Log error.
            interstitialUnavailable([nsZoneId UTF8String]);
        }
     ];
}

/** */
bool AEiOSLuaLibAdColony::isInterstitialExpired(const string &zoneId) {
    Log::trace(logTag,"AEiOSLuaLibAdColony::isInterstitialExpired()");
    
// find
    AdColonyInterstitial *ad = findInterstitialAdByZoneId(zoneId);
    if (ad == NULL) {
        return true;
    }
    
// expired?
    if (ad.expired == TRUE) {
        removeInterstitital(ad);
        return true;
    }
    return false;
}

/** */
void AEiOSLuaLibAdColony::showInterstitial(const string &zoneId) {
// find
    AdColonyInterstitial *ad = findInterstitialAdByZoneId(zoneId);
    if (ad == NULL) {
        Log::warning(logTag,"Attempt to show an unknown interstitial");
        interstitialClosed(zoneId);
        return;
    }
    
// expired?
    if (ad.expired == TRUE) {
        Log::warning(logTag,"Attempt to show an expired interstitial");
        removeInterstitital(ad);
        interstitialClosed(zoneId);
        return;
    }    
    
// get UIViewController
    UIViewController *uiViewController = aeGetTopViewController();
    
// show
    if ([NSThread isMainThread]) {
        Log::trace(logTag,"AEiOSLuaLibAdColony::showInterstitial()");
        [ad showWithPresentingViewController:uiViewController];
    }
    else {
        // run in the main queue        
        [[NSOperationQueue mainQueue] addOperationWithBlock:^{
            Log::trace(logTag,
                "AEiOSLuaLibAdColony::showInterstitial() {queued}");
            [ad showWithPresentingViewController:uiViewController];
        }];
    }
}