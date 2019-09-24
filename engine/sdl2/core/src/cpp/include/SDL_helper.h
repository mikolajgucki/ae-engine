#ifndef AE_SDL_SDL_HELPER_H
#define AE_SDL_SDL_HELPER_H

#include "SDL_keycode.h"

#include "Key.h"

namespace ae {
    
namespace sdl {
 
/**
 * \brief Gets the SDL error.
 * \return The error or null if there is no error.
 */
const char *getSDLError();

/**
 * \brief Maps an SDL key code onto a key.
 * \param keycode The SDL keycode.
 * \return The key or null if there is no such mapping.
 */
const ae::engine::Key *mapSDLKey(SDL_Keycode keycode);
    
} // namespace
    
} // namespace

#endif // AE_SDL_SDL_HELPER_H