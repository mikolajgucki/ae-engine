#ifndef AE_SDL_ENGINE_MUTEX_FACTORY_H
#define AE_SDL_ENGINE_MUTEX_FACTORY_H

#include "EngineMutexFactory.h"

namespace ae {
    
namespace engine {
    
/**
 * \brief SDL mutex factory.
 */
class SDLEngineMutexFactory:public EngineMutexFactory {
public:
    /** */
    SDLEngineMutexFactory():EngineMutexFactory() {
    }
    
    /** */
    virtual ~SDLEngineMutexFactory() {
    }
    
    /**
     * \brief Creates a mutex.
     * \return The new mutex.
     */
    virtual EngineMutex* createMutex();
};
    
} // namespace
    
} // namespace

#endif //  AE_SDL_ENGINE_MUTEX_FACTORY_H