#ifndef AE_SDL_SLEEP_FUNC_H
#define AE_SDL_SLEEP_FUNC_H

#include "SleepFunc.h"

namespace ae {
    
namespace engine {
    
/**
 * \brief Provides the sleep function.
 */
class SDLSleepFunc:public SleepFunc {
public:
    /** */
    SDLSleepFunc():SleepFunc() {
    }
    
    /** */
    virtual ~SDLSleepFunc() {
    }
    
    /** */
    virtual void sleep(int milliseconds);
};
    
} // namespace
    
} // namespace

#endif // AE_SDL_SLEEP_FUNC_H