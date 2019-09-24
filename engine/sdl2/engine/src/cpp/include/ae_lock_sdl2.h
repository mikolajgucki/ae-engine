#ifndef AE_LOCK_SDL2_H
#define AE_LOCK_SDL2_H

#include "EngineMutex.h"

namespace ae {
    
/** */
void aeSetGlobalLock(::ae::engine::EngineMutex *mutex_);
    
} // namespace

#endif // AE_LOCK_SDL2_H