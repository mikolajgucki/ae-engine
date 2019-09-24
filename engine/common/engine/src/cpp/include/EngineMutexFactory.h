#ifndef AE_ENGINE_MUTEX_FACTORY_H
#define AE_ENGINE_MUTEX_FACTORY_H

#include "Error.h" 
#include "EngineMutex.h"

namespace ae {
    
namespace engine {
  
/**
 * \brief Mutex factory.
 */
class EngineMutexFactory:public Error {
public:
    /** */
    EngineMutexFactory():Error() {
    }
    
    /** */
    virtual ~EngineMutexFactory() {
    }
    
    /**
     * \brief Creates a mutex.
     * \return The new mutex or null on error.
     */
    virtual EngineMutex* createMutex() = 0;
};
    
} // namspace
    
} // namespace

#endif // AE_ENGINE_MUTEX_FACTORY_H