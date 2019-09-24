#ifndef AE_SYSTEM_SCOPED_LOCK_H
#define AE_SYSTEM_SCOPED_LOCK_H

#include "Mutex.h"

namespace ae {
    
namespace system {

/**
 * \brief Locks a mutex in the constructor and unlocks in the destructor.
 */
class ScopedLock {
    /// The mutex to lock.
    Mutex *mutex;
    
public:
    /**
     * \brief Constructs a ScopedLock.
     * \param mutex_ The mutex to lock.
     */
    ScopedLock(Mutex *mutex_);
    
    /**
     * \brief Unlocks the mutex.
     */
    ~ScopedLock();
};
    
} // namespace

} // namespace

#endif // AE_SYSTEM_SCOPED_LOCK_H