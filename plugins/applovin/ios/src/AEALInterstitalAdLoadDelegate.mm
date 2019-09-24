#include <sstream>
#include "Log.h"
#import "ALSdk.h"
#import "AEALInterstitialAdLoadDelegate.h"

using namespace std;
using namespace ae;

/// The log tag.
static const char *logTag = "AEAppLovin";

@implementation AEALInterstitialAdLoadDelegate

/** */
-(void) adService:(ALAdService *)adService didLoadAd:(ALAd *)ad {
    Log::trace(logTag,"[AEALInterstitialAdLoadDelegate didLoadAd]");
    _cachedAd = ad;
    _appLovinCallback->interstitialAdLoaded();
}

/** */
-(void) adService:(ALAdService *)adService didFailToLoadAdWithError:(int)code {
    _cachedAd = nil;
    if (code == kALErrorCodeNoFill) {
        Log::trace(logTag,"AEALInterstitialAdLoadDelegate: no fill");
        _appLovinCallback->interstitialAdNoFill();
    }
    else {
        ostringstream msg;
        msg << "AEALInterstitialAdLoadDelegate: error " << code;
        Log::trace(logTag,msg.str());
        _appLovinCallback->interstitialAdFailed(code);
    }
}

@end
