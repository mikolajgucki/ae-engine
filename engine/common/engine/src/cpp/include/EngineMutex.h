#ifndef AE_ENGINE_MUTEX_H
#define AE_ENGINE_MUTEX_H

#include "Error.h"

namespace ae {
    
namespace engine {
  
/**
 * \brief Mutual exclusion.
 */
class EngineMutex:public Error {
public:
    /** */
    EngineMutex():Error() {
    }
    
    /** */
    virtual ~EngineMutex() {
    }
    
    /**
     * \brief Locks.
     */
    virtual void lock() = 0;
    
    /**
     * \brief Unlocks.
     */
    virtual void unlock() = 0;
};
    
} // namspace
    
} // namespace

#endif // AE_ENGINE_MUTEX_H