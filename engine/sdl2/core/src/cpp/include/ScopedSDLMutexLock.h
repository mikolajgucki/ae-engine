#ifndef AE_SCOPED_SDL_MUTEX_LOCK_H
#define AE_SCOPED_SDL_MUTEX_LOCK_H

#include "SDL.h"

namespace ae {
    
namespace sdl {
 
/**
 * \brief Keeps a lock as long as the object exists. That is, locks in the
 *   constructor, unlocks in the destructor.
 */
class ScopedSDLMutexLock {
    /// The mutex to lock.
    SDL_mutex *mutex;
    
public:
    /**
     * \brief Locks the given mutex.
     * \param mutex The mutex.
     */
    ScopedSDLMutexLock(SDL_mutex *mutex_):mutex(mutex_) {
        SDL_LockMutex(mutex);
    }
    
    /**
     * \brief Unlocks the mutex.
     */
    ~ScopedSDLMutexLock() {
        SDL_UnlockMutex(mutex);
    }
};

} // namespace

} // namespace

#endif // AE_SCOPED_SDL_MUTEX_LOCK_H