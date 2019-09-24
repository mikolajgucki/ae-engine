#include <string>

#include "Log.h"
#include "SDL_mixer.h"
#include "SDLSound.h"

using namespace std;

namespace ae {
    
namespace audio {
 
/// The log tag.
const char *logTag = "ae.sdl";
    
/** */
bool SDLSound::play() {
    int freeChannels = Mix_AllocateChannels(-1) - Mix_Playing(-1);
    if (freeChannels <= 0) {
        Log::warning(logTag,"Cannot play sound due to no free channels");        
        return true;
    }
    
    if (Mix_PlayChannel(-1,mixChunk,0) == -1) {
        setError(string(Mix_GetError()));
        return false;
    }
    
    return true;
}
    
} // namespace
    
} // namespace