#ifndef AE_SDL_GET_TICKS_FUNC_H
#define AE_SDL_GET_TICKS_FUNC_H

#include "GetTicksFunc.h"

namespace ae {
    
namespace engine {
    
/**
 * \brief Provides the get-ticks function.
 */
class SDLGetTicksFunc:public GetTicksFunc {
public:
    /** */
    SDLGetTicksFunc():GetTicksFunc() {
    }
    
    /** */
    virtual ~SDLGetTicksFunc() {
    }
    
    /** */
    virtual long getTicks();
};
    
} // namespace
    
} // namespace

#endif // AE_SDL_GET_TICKS_FUNC_H