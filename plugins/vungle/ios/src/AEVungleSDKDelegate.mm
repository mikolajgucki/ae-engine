#include "AEVungleSDKDelegate.h"

@implementation AEVungleSDKDelegate

/** */
-(void) vungleSDKwillShowAd {
    _luaLibVungle->willShowAd();
}

/** */
-(void) vungleSDKwillCloseAdWithViewInfo:(NSDictionary *)viewInfo
    willPresentProductSheet:(BOOL)willPresentProductSheet {
//
    if (willPresentProductSheet == NO) {
        _luaLibVungle->willCloseAd();
        
        if ([viewInfo objectForKey:@"completedView"]) {
            _luaLibVungle->viewCompleted();
        }
    }
    
    NSNumber *didDownload = (NSNumber *)viewInfo[@"didDownload"]; 
    if ([didDownload boolValue]) {
        _luaLibVungle->adClicked();
    }
}

/** */
-(void) vungleSDKwillCloseProductSheet:(id)productSheet {
    _luaLibVungle->willCloseAd();
}

/** */
-(void)vungleSDKAdPlayableChanged:(BOOL)isAdPlayable {
    _luaLibVungle->adPlayableChanged(isAdPlayable == NO ? false : true);
}

@end