#include "AEGKGameCenterControllerDelegate.h"

@implementation AEGKGameCenterControllerDelegate

/** */
-(void) gameCenterViewControllerDidFinish:
    (GKGameCenterViewController *)gameCenterViewController {
//
    _gameCenterCallback->loginViewHidden();
}

@end