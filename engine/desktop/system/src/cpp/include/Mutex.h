#ifndef AE_SYSTEM_MUTEX_H
#define AE_SYSTEM_MUTEX_H

#include "Error.h"

namespace ae {
    
namespace system {
  
/**
 * \brief Protects from concurrent access.
 */
class Mutex:public Error {
    /// The data set and used by particular implementations.
    void *data;    
    
public:
    /**
     * \brief Creates the mutex.
     */
    Mutex():data((void *)0) {
        create();
    }
    
    /** */
    ~Mutex();
    
    /**
     * \brief Creates the mutex data.
     * \return <code>true</code> if the mutex has been sucessfully created,
     *   <code>false</code> otherwise.
     */
    bool create();
    
    /**
     * \brief Indicates if the mutex data is created.
     * \return <code>true</code> if created, <code>false</code> otherwise.
     */
    bool isCreated() {
        return data != (void *)0;
    }        
    
    /**
     * Locks.
     */
    void lock();
    
    /**
     * Unlocks.
     */
    void unlock();
};
    
} // namespace

} // namespace

#endif // AE_SYSTEM_MUTEX_H