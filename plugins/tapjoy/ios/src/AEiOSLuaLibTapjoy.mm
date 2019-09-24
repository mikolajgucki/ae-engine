#import <Tapjoy/Tapjoy.h>
#include "AEiOSLuaLibTapjoy.h"

using namespace std;

namespace ae {

namespace tapjoy {
    
/** */
bool AEiOSLuaLibTapjoy::debug = false;

/** */
void AEiOSLuaLibTapjoy::initDebug() {
    debug = true;
}
    
/** */
void AEiOSLuaLibTapjoy::connect(const string &sdkKey) {
    if (debug) {
        NSLog(@"AEiOSLuaLibTapjoy::connect");
        [Tapjoy setDebugEnabled:YES];
    }
   
// SDK key
    const char *sdkKeyCStr = sdkKey.c_str();
    NSString *nsSdkKey = [NSString stringWithUTF8String:sdkKeyCStr];
    
// connect call
    [Tapjoy connect:nsSdkKey];
}

/** */
void AEiOSLuaLibTapjoy::addSDLPushNotificationObservers() {
    if (debug) {
        NSLog(@"AEiOSLuaLibTapjoy::addSDLPushNotificationObservers()");
    }
    
    [[NSNotificationCenter defaultCenter] addObserverForName:@"SDL_didRegisterForRemoteNotificationsWithDeviceToken" object:nil queue:nil usingBlock:^(NSNotification *notification) {
    //
        if (debug) {
            NSLog(@"Received SDL_didRegisterForRemoteNotificationsWithDeviceToken");
        }
        NSData *deviceToken = notification.userInfo[@"deviceToken"];
        [Tapjoy setDeviceToken:deviceToken];
    }];
    [[NSNotificationCenter defaultCenter] addObserverForName:@"SDL_didFailToRegisterForRemoteNotificationsWithError" object:nil queue:nil usingBlock:^(NSNotification *notification) {
    //
        if (debug) {
            NSLog(@"Received SDL_didFailToRegisterForRemoteNotificationsWithError");
        }
    }];
    [[NSNotificationCenter defaultCenter] addObserverForName:@"SDL_didReceiveRemoteNotification" object:nil queue:nil usingBlock:^(NSNotification *notification) {
        if (debug) {
            NSLog(@"Received SDL_didReceiveRemoteNotification");
        }
        [Tapjoy setReceiveRemoteNotification:notification.userInfo];
    }];
}
    
/** */
void AEiOSLuaLibTapjoy::initPushNotifications(UIApplication *application,NSDictionary *launchOptions) {
    if (debug) {
        NSLog(@"AEiOSLuaLibTapjoy::initPushNotifications");
    }
    
    if ([application respondsToSelector:@selector(isRegisteredForRemoteNotifications)])
    {
        // iOS 8 Notifications
        [application registerUserNotificationSettings:[UIUserNotificationSettings settingsForTypes:(UIUserNotificationTypeSound | UIUserNotificationTypeAlert | UIUserNotificationTypeBadge) categories:nil]];
        
        [application registerForRemoteNotifications];
    }
    else
    {
        // iOS < 8 Notifications
        [application registerForRemoteNotificationTypes:
         (UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeAlert | UIRemoteNotificationTypeSound)];
    }
    
    [Tapjoy setApplicationLaunchingOptions:launchOptions];
}

} // namespace

} // namespace