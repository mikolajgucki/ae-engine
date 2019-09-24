#import <Foundation/Foundation.h>
#import <VungleSDK/VungleSDK.h>
#include "LuaLibVungle.h"

@interface AEVungleSDKDelegate : NSObject<VungleSDKDelegate>

/** */
@property (nonatomic) ae::vungle::LuaLibVungle *luaLibVungle;

@end