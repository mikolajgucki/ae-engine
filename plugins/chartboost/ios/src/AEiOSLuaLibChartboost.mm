#import <Foundation/Foundation.h>
#include "Log.h"
#include "AEiOSLuaLibChartboost.h"

using namespace std;
using namespace ae;
using namespace ae::chartboost;

/// The log tag.
static const char *logTag = "AEChartboost";

/** */
void AEiOSLuaLibChartboost::init() {
    Log::trace(logTag,"AEiOSLuaLibChartboost::init()");
    
// identifier
    const char *appIdCStr = appId.c_str();
    NSString *nsAppId = [NSString stringWithUTF8String:appIdCStr];
    
// signature
    const char *appSignatureCStr = appSignature.c_str();
    NSString *nsAppSignature = [NSString stringWithUTF8String:appSignatureCStr];
    
    ae::chartboost::ChartboostCallback *callback = getCallback();
// delegate
    aeChartboostDelegate = [[AEChartboostDelegate alloc] init];
    [aeChartboostDelegate setChartboostCallback:callback];
    
// initialize
    [Chartboost startWithAppId:nsAppId appSignature:nsAppSignature
        delegate:aeChartboostDelegate];
}

/** */
void AEiOSLuaLibChartboost::cacheInterstitial(const string &location) {
    Log::trace(logTag,"AEiOSLuaLibChartboost::cacheInterstitial()");
    
// location
    const char *locationCStr = location.c_str();
    NSString *nsLocation = [NSString stringWithUTF8String:locationCStr];
    
// cache
    dispatch_async(dispatch_get_main_queue(),^{
        ostringstream msg;
        msg << "Calling [Chartboost cacheInterstitial:"
            << nsLocation.UTF8String << "]";
        Log::trace(logTag,msg.str());
        [Chartboost cacheInterstitial:nsLocation];
    });
}

/** */
void AEiOSLuaLibChartboost::showInterstitial(const string &location) {
    Log::trace(logTag,"AEiOSLuaLibChartboost::showInterstitial()");
    
// location
    const char *locationCStr = location.c_str();
    NSString *nsLocation = [NSString stringWithUTF8String:locationCStr];
    
// show
    dispatch_async(dispatch_get_main_queue(),^{
        ostringstream msg;
        msg << "Calling [Chartboost showInterstitial:"
            << nsLocation.UTF8String << "]";
        Log::trace(logTag,msg.str());
        [Chartboost showInterstitial:nsLocation];
    });
}