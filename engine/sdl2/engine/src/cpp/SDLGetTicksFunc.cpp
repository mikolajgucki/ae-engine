#include "SDL.h"
#include "SDLGetTicksFunc.h"

namespace ae {
    
namespace engine {
 
/** */
long SDLGetTicksFunc::getTicks() {
    return (long)SDL_GetTicks();
}
    
} // namespace
    
} // namespace