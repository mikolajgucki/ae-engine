#include "SDL.h"
#include "SDL_helper.h"

using namespace ae::engine;

/** */
struct SDLKeycodeToKeyMapping {
    /** */
    SDL_Keycode keycode;
    
    /** */
    const Key *key;
};
typedef struct SDLKeycodeToKeyMapping SDLKeycodeToKeyMapping;

/**
 * \brief The SDL key code to engine key mapping.
 */
const SDLKeycodeToKeyMapping keyMapping[] = {
    { SDLK_MENU, &Key::MENU },
    { SDLK_AC_BACK, &Key::BACK },
    { SDLK_UNKNOWN, (const Key *)0 }
};

namespace ae {
    
namespace sdl {
    
/** */
const char *getSDLError() {
    const char *error = SDL_GetError();
    if (error == (const char *)0) {
        return (const char *)0;
    }
    if (strcmp(error,"") == 0) {
        return (const char *)0;
    }
    
    return error;
}
    
/** */
const Key *mapSDLKey(SDL_Keycode keycode) {
    for (int index = 0; keyMapping[index].key != (const Key *)0; index++) {
    //
        if (keyMapping[index].keycode == keycode) {
            return keyMapping[index].key;
        }
    }
        
    
    return (const Key *)0;
}

} // namespace
    
} // namespace