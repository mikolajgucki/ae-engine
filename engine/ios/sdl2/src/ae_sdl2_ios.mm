#import <Foundation/Foundation.h>
#include "SDL.h"
#include "SDL_syswm.h"
#include "ae_sdl2.h"
#include "ae_sdl2_ios.h"

/** */
void aeInitSDL2iOS() {
    NSArray *paths = NSSearchPathForDirectoriesInDomains(
        NSDocumentDirectory,NSUserDomainMask,YES);
    NSString *documentsDir = [paths objectAtIndex:0];
    
    const char *storageDirCStr = [documentsDir UTF8String]; 
    aeSetStorageDir(storageDirCStr);
}

/** */
int aeGetiOSScreenWidth() {
    CGRect screenBounds = [[UIScreen mainScreen] bounds];
    CGFloat screenScale = [[UIScreen mainScreen] scale];
    return (int)(screenBounds.size.width * screenScale);
}

/** */
int aeGetiOSScreenHeight() {
    CGRect screenBounds = [[UIScreen mainScreen] bounds];
    CGFloat screenScale = [[UIScreen mainScreen] scale];
    return (int)(screenBounds.size.height * screenScale);
}

/** */
UIViewController *aeGetUIViewController(SDL_Window *window) {
    SDL_SysWMinfo wminfo;
    SDL_VERSION(&wminfo.version);
    
    if (SDL_GetWindowWMInfo(window,&wminfo) == SDL_FALSE) {
        return nil;
    }
    
    UIWindow *uiWindow = wminfo.info.uikit.window;
    return uiWindow.rootViewController;
}

/** */
UIViewController *aeGetTopViewController() {
    UIViewController *topController =
        [UIApplication sharedApplication].keyWindow.rootViewController;
    while (topController.presentedViewController) {
        topController = topController.presentedViewController;
    }

    return topController;
}