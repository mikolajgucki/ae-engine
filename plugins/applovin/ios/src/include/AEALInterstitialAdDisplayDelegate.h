#import <Foundation/Foundation.h>
#import "ALAdDisplayDelegate.h"
#include "AppLovinCallback.h"

@interface AEALInterstitialAdDisplayDelegate : NSObject<ALAdDisplayDelegate>

/** */
@property (nonatomic) ::ae::applovin::AppLovinCallback *appLovinCallback;

@end
