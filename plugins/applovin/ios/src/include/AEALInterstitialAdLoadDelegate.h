#import <Foundation/Foundation.h>
#import "ALAd.h"
#import "ALAdLoadDelegate.h"
#include "AppLovinCallback.h"

@interface AEALInterstitialAdLoadDelegate : NSObject<ALAdLoadDelegate>

/** */
@property (atomic) ALAd *cachedAd;

/** */
@property (nonatomic) ae::applovin::AppLovinCallback *appLovinCallback;

@end
