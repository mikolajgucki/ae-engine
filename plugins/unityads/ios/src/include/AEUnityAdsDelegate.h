#import <UnityAds/UnityAds.h>
#include "LuaLibUnityAds.h"

@interface AEUnityAdsDelegate : NSObject<UnityAdsDelegate>

@property (nonatomic) ae::unityads::LuaLibUnityAds *luaLibUnityAds;

@end