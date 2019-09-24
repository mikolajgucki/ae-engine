#import <GameKit/GameKit.h>
#include "Log.h"
#include "ae_sdl2.h"
#include "ae_sdl2_ios.h"
#include "AEiOSLuaLibGameCenter.h"

// http://comments.gmane.org/gmane.comp.lib.sdl/65320

#define SYSTEM_VERSION_LESS_THAN(v) ([[[UIDevice currentDevice] systemVersion] \
compare:v options:NSNumericSearch] == NSOrderedAscending)

using namespace std;
using namespace ae;

/// The log tag.
static const char *logTag = "AEGameCenter";

/** */
bool AEiOSLuaLibGameCenter::isGameCenterAvailable() {
// check for presence of GKLocalPlayer API
    Class gcClass = (NSClassFromString(@"GKLocalPlayer"));

// check if the device is running iOS 4.1 or later
    NSString *reqSysVer = @"4.1";
    NSString *currSysVer = [[UIDevice currentDevice] systemVersion];
    BOOL osVersionSupported = 
        ([currSysVer compare:reqSysVer options:NSNumericSearch] != NSOrderedAscending);
    
// test if available
    BOOL available = (gcClass && osVersionSupported);
    return available == TRUE ? true : false;
}

/** */
void AEiOSLuaLibGameCenter::init() {
    Log::trace(logTag,"AEiOSLuaLibGameCenter::init()");
    ae::gamecenter::GameCenterCallback *callback = getCallback();
    
// controller delegate
    aeControllerDelegate = [[AEGKGameCenterControllerDelegate alloc] init];
    [aeControllerDelegate setGameCenterCallback:callback];
}

/** */
void AEiOSLuaLibGameCenter::authenticate() {
    Log::trace(logTag,"AEiOSLuaLibGameCenter::authenticate()");
    
// check if available
    if (isGameCenterAvailable() == false) {
        Log::trace(logTag,"GameCenter is not available");
        notAuthenticated();
        return;
    }
    
// authenticate
    GKLocalPlayer *localPlayer = [GKLocalPlayer localPlayer];
    localPlayer.authenticateHandler = ^(UIViewController *viewController,NSError *error) {
        if (error != nil) {
            ostringstream err;
            err << "Local player authentication finished with error " <<
                error.localizedDescription.UTF8String;
            Log::trace(logTag,err.str());
            notAuthenticatedWithError();
            return;
        }        
        
        GKLocalPlayer *localPlayer = [GKLocalPlayer localPlayer];
        if (viewController != nil) {
            Log::trace(logTag,"Displaying Game Center authenticate view controller");
            willShowLoginView();
             
        // get UIViewController
            UIViewController *uiViewController = aeGetTopViewController();
        
        // show the view controller
           [uiViewController presentViewController:viewController animated:YES
               completion:nil];
        }
        else if (localPlayer.isAuthenticated) {
            Log::trace(logTag,"Local player authenticated");
            authenticated();
        }
        else {
            Log::trace(logTag,"Local player not authenticated");
            notAuthenticated();
        }
    };
}

/** */
void AEiOSLuaLibGameCenter::show() {
    Log::trace(logTag,"AEiOSLuaLibGameCenter::show()");
    
// get UIViewController
    UIViewController *uiViewController = aeGetTopViewController();
    
// show the Game Center view controller
    GKGameCenterViewController *gameCenterController =
        [[GKGameCenterViewController alloc] init];
    if (gameCenterController != nil) {
       gameCenterController.gameCenterDelegate = aeControllerDelegate;
       [uiViewController presentViewController:gameCenterController
           animated:YES completion:nil];
    }
    else {
        Log::trace(logTag,"Failed to create Game Center view controller");
    }
}

/** */
void AEiOSLuaLibGameCenter::reportScore(
    const string& leaderboardId,long score) {
//
    Log::trace(logTag,"AEiOSLuaLibGameCenter::reportScore()");
    
// identifier
    const char *leaderboardIdCStr = leaderboardId.c_str();
    NSString *nsLeaderboardId =
        [NSString stringWithUTF8String:leaderboardIdCStr];    
    
// reporter
    GKScore *scoreReporter =
        [[GKScore alloc] initWithLeaderboardIdentifier:nsLeaderboardId];
    if (scoreReporter == nil) {
        ostringstream err;
        err << "Failed to initialize score with leaderboard " <<
            nsLeaderboardId.UTF8String;
        Log::trace(logTag,err.str());
        return;
    }
    scoreReporter.value = score;
    scoreReporter.context = 0;

// report
    NSArray *scores = @[scoreReporter];
    [GKScore reportScores:scores withCompletionHandler:^(NSError *error) {
        if (error != nil) {
            ostringstream err;
            err << "Failed to report score: " <<
                error.localizedDescription.UTF8String;
            Log::trace(logTag,err.str());
        }
        else {
        // success
        }
    }];    
}

/** */
void AEiOSLuaLibGameCenter::reportAchievement(const string& achievementId,
    double percentComplete) {
//
    Log::trace(logTag,"AEiOSLuaLibGameCenter::reportAchievement()");
    if (achievements == nil) {
        Log::trace(logTag,"Achievements not loaded");
        return;
    }
    
// identifier
    const char *achievementIdCStr = achievementId.c_str();
    NSString *nsAchievementId =
        [NSString stringWithUTF8String:achievementIdCStr];    
    
    GKAchievement *achievement = nil;
    for (int index = 0; index < [achievements count]; index++) {
        if ([nsAchievementId isEqualToString:[achievements[index] identifier]]) {
            achievement = achievements[index];
            break;
        }
    }
    if (achievement == nil) {
        achievement = [[GKAchievement alloc] initWithIdentifier:nsAchievementId];
    }
        
    if (achievement && percentComplete > achievement.percentComplete) {
        achievement.showsCompletionBanner = YES;
        achievement.percentComplete = percentComplete;
        [GKAchievement reportAchievements:@[achievement]
            withCompletionHandler:^(NSError *error) {
        //
            if (error != nil) {
                ostringstream err;
                err << "Failed to report achievements " <<
                    error.localizedDescription.UTF8String;
                Log::trace(logTag,err.str());
            }
            else {
            // success
            }
        }];
    }
    else {
        ostringstream err;
        err << "Failed to initialize achievement " << nsAchievementId.UTF8String;
        Log::trace(logTag,err.str());
    }
}

/** */
bool AEiOSLuaLibGameCenter::isAchievementCompleted(
    const std::string& achievementId,bool& completed) {
//
    if (achievements == nil) {
        return false;
    }
    
// identifier
    const char *achievementIdCStr = achievementId.c_str();
    NSString *nsAchievementId =
       [NSString stringWithUTF8String:achievementIdCStr];
    
    for (int index = 0; index < [achievements count]; index++) {
        GKAchievement *achievement = achievements[index];
        if ([nsAchievementId isEqualToString:[achievement identifier]]) {
            completed = [achievement isCompleted] == YES ? true : false;
            return true;
        }
    }
    return false;
}

/** */
bool AEiOSLuaLibGameCenter::getAchievementProgress(
    const std::string& achievementId,double& progress) {
//
    if (achievements == nil) {
        return false;
    }
    
// identifier
    const char *achievementIdCStr = achievementId.c_str();
    NSString *nsAchievementId =
        [NSString stringWithUTF8String:achievementIdCStr];
    
    for (int index = 0; index < [achievements count]; index++) {
        GKAchievement *achievement = achievements[index];
        if ([nsAchievementId isEqualToString:[achievement identifier]]) {
            progress = [achievement percentComplete];
            return true;
        }
    }
    
    return false;
}

/** */
void AEiOSLuaLibGameCenter::loadAchievements() {
    Log::trace(logTag,"AEiOSLuaLibGameCenter::loadAchievements()");
    
    [GKAchievement loadAchievementsWithCompletionHandler:^(
        NSArray *loadedAchievements, NSError *error) {
    // error
        if (error != nil) {
            ostringstream err;
            err << "Failed to load achievements " <<
                error.localizedDescription.UTF8String;
            Log::trace(logTag,err.str());
            return;
        }
        
    // loaded
        if (loadedAchievements != nil) {
            Log::trace(logTag,"Loaded achievements:");
            for (int index = 0; index < [loadedAchievements count]; index++) {
                GKAchievement *achievement = loadedAchievements[index];
                ostringstream msg;
                msg << "  " << [[achievement identifier] UTF8String] <<
                    ", progress = " << [achievement percentComplete] << "%";
                Log::trace(logTag,msg.str());
            }
            achievements = loadedAchievements;
            achievementsLoaded();
            return;
        }
        
        Log::trace(logTag,"No achievements");
    }];
}

/** */
void AEiOSLuaLibGameCenter::resetAchievements() {
    Log::trace(logTag,"AEiOSLuaLibGameCenter::resetAchievements()");
    
    [GKAchievement resetAchievementsWithCompletionHandler:^(NSError *error) {
        if (error != nil) {
            Log::trace(logTag,"Failed to reset achievements");
        }
    }];
}
