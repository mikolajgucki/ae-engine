#ifndef AE_SDL2_IOS_H
#define AE_SDL2_IOS_H

#import <UIKit/UIKit.h>
#include "SDL.h"

/**
 * \brief Initializes SDL2 on iOS.
 */
void aeInitSDL2iOS();

/**
 * \brief Gets the UI view controller of a window.
 * \param window The window.
 * \return The UI view controller or null on error.
 */
UIViewController *aeGetUIViewController(SDL_Window *window);

/**
 * \brief Gets the top UI view controller.
 * \return The top UI view controller.
 */
UIViewController *aeGetTopViewController();

#endif // AE_SDL2_IOS_H