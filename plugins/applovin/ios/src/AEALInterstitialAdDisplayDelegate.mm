#include "Log.h"
#import "ALAd.h"
#import "AEALInterstitialAdDisplayDelegate.h"

using namespace ae;

/// The log tag.
static const char *logTag = "AEAppLovin";

@implementation AEALInterstitialAdDisplayDelegate

/** */
- (void)ad:(ALAd *)ad wasClickedIn:(UIView *)view {
    Log::trace(logTag,"[AEALInterstitialAdDisplayDelegate wasClickedIn]");
    _appLovinCallback->interstitialAdClicked();
}

/** */
- (void)ad:(ALAd *)ad wasDisplayedIn:(UIView *)view {
    Log::trace(logTag,"[AEALInterstitialAdDisplayDelegate wasDisplayedIn]");
    _appLovinCallback->interstitialAdDisplayed();
}

/** */
- (void)ad:(ALAd *)ad wasHiddenIn:(UIView *)view {
    Log::trace(logTag,"[AEALInterstitialAdDisplayDelegate wasHiddenIn]");
    _appLovinCallback->interstitialAdHidden();
}

@end