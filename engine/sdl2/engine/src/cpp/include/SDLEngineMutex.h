#ifndef AE_SDL_ENGINE_MUTEX_H
#define AE_SDL_ENGINE_MUTEX_H

#include "SDL_mutex.h"
#include "EngineMutex.h"

namespace ae {
    
namespace engine {
  
/**
 * \brief Mutual exclusion.
 */
class SDLEngineMutex:public EngineMutex {
    /// The SDL mutex.
    SDL_mutex *sdlMutex;
    
    /**
     * \brief Creates the mutex.
     */
    void create();
    
    /**
     * \brief Destroys the mutex.
     */
    void destroy();    
    
public:
    /** */
    SDLEngineMutex():EngineMutex() {
        create();
    }
    
    /** */
    virtual ~SDLEngineMutex() {
        destroy();
    }
    
    /** */
    virtual void lock();
    
    /** */
    virtual void unlock();
};
    
} // namspace
    
} // namespace

#endif // AE_SDL_ENGINE_MUTEX_H