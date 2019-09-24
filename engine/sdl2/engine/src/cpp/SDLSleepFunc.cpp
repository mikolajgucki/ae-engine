#include "SDL.h"
#include "SDLSleepFunc.h"

namespace ae {
    
namespace engine {
    
/** */
void SDLSleepFunc::sleep(int milliseconds) {
    SDL_Delay(milliseconds);
}
    
} // namespace
    
} // namespace