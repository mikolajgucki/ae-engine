#include <sstream>
#include "Log.h"
#import "AEChartboostDelegate.h"

using namespace ae;
using namespace std;

/// The log tag.
static const char *logTag = "AEChartboost";

@implementation AEChartboostDelegate

// Called before requesting an interstitial via the Chartboost API server.
-(BOOL) shouldRequestInterstitial:(CBLocation)location {
    return YES;
}

// Called before an interstitial will be displayed on the screen.
-(BOOL) shouldDisplayInterstitial:(CBLocation)location {
    return YES;
}

// Called after an interstitial has been displayed on the screen.
-(void) didDisplayInterstitial:(CBLocation)location {
    Log::trace(logTag,"[AEChartboostDelegate didDisplayInterstitial]");
    const char *locationCStr = [location UTF8String];
    _chartboostCallback->didDisplayInterstitial(locationCStr);
}

// Called after an interstitial has been loaded from the Chartboost API
// servers and cached locally.
-(void) didCacheInterstitial:(CBLocation)location {
    Log::trace(logTag,"[AEChartboostDelegate didCacheInterstitial]");
    const char *locationCStr = [location UTF8String];
    _chartboostCallback->didCacheInterstitial(locationCStr);
}

// Called after an interstitial has attempted to load from the Chartboost API
// servers but failed.
-(void) didFailToLoadInterstitial:(CBLocation)location withError:(CBLoadError)error {
    ostringstream err;
    err << "[AEChartboostDelegate didFailToLoadInterstitial withError:" << error << "]";
    Log::trace(logTag,err.str());
    
    /*
    switch (error) {
        case CBLoadErrorInternal:
            Log::trace(logTag,"Error: CBLoadErrorInternal");
            break;
        case CBLoadErrorInternetUnavailable:
            Log::trace(logTag,"Error: CBLoadErrorInternetUnavailable");
            break;
        case CBLoadErrorTooManyConnections:
            Log::trace(logTag,"Error: CBLoadErrorTooManyConnections");
            break;
        case CBLoadErrorWrongOrientation:
            Log::trace(logTag,"Error: CBLoadErrorWrongOrientation");
            break;
        case CBLoadErrorFirstSessionInterstitialsDisabled:
            Log::trace(logTag,"Error: CBLoadErrorFirstSessionInterstitialsDisabled");
            break;
        case CBLoadErrorNetworkFailure:
            Log::trace(logTag,"Error: CBLoadErrorNetworkFailure");
            break;
        case CBLoadErrorNoAdFound:
            Log::trace(logTag,"Error: CBLoadErrorNoAdFound");
            break;
        case CBLoadErrorSessionNotStarted:
            Log::trace(logTag,"Error: CBLoadErrorSessionNotStarted");
            break;
        case CBLoadErrorImpressionAlreadyVisible:
            Log::trace(logTag,"Error: CBLoadErrorImpressionAlreadyVisible");
            break;
        case CBLoadErrorUserCancellation:
            Log::trace(logTag,"Error: CBLoadErrorUserCancellation");
            break;
        case CBLoadErrorNoLocationFound:
            Log::trace(logTag,"Error: CBLoadErrorNoLocationFound");
            break;
        case CBLoadErrorAssetDownloadFailure:
            Log::trace(logTag,"Error: CBLoadErrorAssetDownloadFailure");
            break;
        case CBLoadErrorPrefetchingIncomplete:
            Log::trace(logTag,"Error: CBLoadErrorPrefetchingIncomplete");
            break;
        case CBLoadErrorWebViewScriptError:
            Log::trace(logTag,"Error: CBLoadErrorWebViewScriptError");
            break;
    }
     */
    
    const char *locationCStr = [location UTF8String];
    _chartboostCallback->didFailToLoadInterstitial(locationCStr);
}

// Called after an interstitial has been dismissed.
-(void) didDismissInterstitial:(CBLocation)location {
    Log::trace(logTag,"[AEChartboostDelegate didDismissInterstitial]");
    const char *locationCStr = [location UTF8String];
    _chartboostCallback->didDismissInterstitial(locationCStr);
}

// Called after an interstitial has been closed.
-(void) didCloseInterstitial:(CBLocation)location {
    Log::trace(logTag,"[AEChartboostDelegate didCloseInterstitial]");
    const char *locationCStr = [location UTF8String];
    _chartboostCallback->didCloseInterstitial(locationCStr);
}

// Called after an interstitial has been clicked.
-(void) didClickInterstitial:(CBLocation)location {
    Log::trace(logTag,"[AEChartboostDelegate didClickInterstitial]");
    const char *locationCStr = [location UTF8String];
    _chartboostCallback->didClickInterstitial(locationCStr);
}

@end