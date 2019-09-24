#import <Foundation/Foundation.h>
#import <GameKit/GameKit.h>
#include "GameCenterCallback.h"

@interface AEGKGameCenterControllerDelegate : NSObject<GKGameCenterControllerDelegate>

/** */
@property (nonatomic) ae::gamecenter::GameCenterCallback *gameCenterCallback;

@end