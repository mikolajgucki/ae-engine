#ifndef AE_DUMMY_ENGINE_MUTEX_H
#define AE_DUMMY_ENGINE_MUTEX_H

#include "EngineMutex.h"

namespace ae {
    
namespace engine {
  
/**
 * \brief Mutual exclusion.
 */
class DummyEngineMutex:public EngineMutex {
public:
    /** */
    DummyEngineMutex():EngineMutex() {
    }
    
    /** */
    virtual ~DummyEngineMutex() {
    }
    
    /** */
    virtual void lock() {
    }
    
    /** */
    virtual void unlock() {
    }
};
    
} // namspace
    
} // namespace

#endif // AE_DUMMY_ENGINE_MUTEX_H