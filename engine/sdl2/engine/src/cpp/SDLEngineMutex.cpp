#include "Log.h"
#include "SDLEngineMutex.h"

using namespace ae;

namespace ae {
    
namespace engine {

/// The log tag.
static const char *logTag = "SDLEngineMutex";
    
/** */
void SDLEngineMutex::create() {
    sdlMutex = SDL_CreateMutex();
    if (sdlMutex == (SDL_mutex *)0) {
        setError(SDL_GetError());
        return;
    }
}

/** */
void SDLEngineMutex::destroy() {
    if (sdlMutex != (SDL_mutex *)0) {
        SDL_DestroyMutex(sdlMutex);
        sdlMutex = (SDL_mutex *)0;
    }
}
    
/** */
void SDLEngineMutex::lock() {
    if (SDL_LockMutex(sdlMutex) != 0) {
        Log::error(logTag,SDL_GetError());
    }
}
    
/** */
void SDLEngineMutex::unlock() {
    if (SDL_UnlockMutex(sdlMutex) != 0) {
        Log::error(logTag,SDL_GetError());
    }
}
    
} // namspace
    
} // namespace