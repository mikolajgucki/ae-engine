#ifndef AE_DUMMY_ENGINE_MUTEX_FACTORY_H
#define AE_DUMMY_ENGINE_MUTEX_FACTORY_H

#include "EngineMutexFactory.h"
#include "DummyEngineMutex.h"

namespace ae {
    
namespace engine {
  
/**
 * \brief Mutex factory.
 */
class DummyEngineMutexFactory:public EngineMutexFactory {
public:
    /** */
    DummyEngineMutexFactory():EngineMutexFactory() {
    }
    
    /** */
    virtual ~DummyEngineMutexFactory() {
    }
    
    /**
     * \brief Creates a mutex.
     * \return The new mutex.
     */
    virtual EngineMutex* createMutex() {
        return new DummyEngineMutex();
    }
};
    
} // namspace
    
} // namespace

#endif // AE_DUMMY_ENGINE_MUTEX_FACTORY_H